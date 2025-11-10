package com.example.quickchat.data.auth.impl

import android.app.Activity
import androidx.core.net.toUri
import com.example.quickchat.data.auth.FirebaseAuthRepository
import com.example.quickchat.data.auth.PhoneAuthResult
import com.example.quickchat.ui.util.Constants.VERIFICATION_TIMEOUT_SECONDS
import com.google.firebase.FirebaseException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Firebase implementation of [FirebaseAuthRepository].
 * Handles phone authentication, email linking, and user session management.
 */
class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : FirebaseAuthRepository {

    override val currentUser: Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        firebaseAuth.addAuthStateListener(authStateListener)

        // Emit current state immediately
        trySend(firebaseAuth.currentUser)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    override fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    override fun isUserAuthenticated(): Boolean = firebaseAuth.currentUser != null

    override fun startPhoneNumberVerification(
        phoneNumber: String,
        activity: Activity
    ): Flow<PhoneAuthResult> = callbackFlow {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                val code = credential.smsCode
                if (code != null) {
                    trySend(PhoneAuthResult.VerificationCompleted(credential, code))
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                trySend(PhoneAuthResult.VerificationFailed(e))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                trySend(PhoneAuthResult.CodeSent(verificationId, token))
            }
        }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(VERIFICATION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        awaitClose {}
    }

    override fun resendVerificationCode(
        phoneNumber: String,
        activity: Activity,
        resendToken: PhoneAuthProvider.ForceResendingToken
    ): Flow<PhoneAuthResult> = callbackFlow {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                val code = credential.smsCode
                if (code != null) {
                    trySend(PhoneAuthResult.VerificationCompleted(credential, code))
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                trySend(PhoneAuthResult.VerificationFailed(e))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                trySend(PhoneAuthResult.CodeSent(verificationId, token))
            }
        }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(VERIFICATION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        awaitClose {}
    }


    override suspend fun signInWithPhoneAuthCredential(
        verificationId: String,
        code: String,
    ): Result<FirebaseUser> = try {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        val authResult = firebaseAuth.signInWithCredential(credential).await()
        authResult.user?.let {
            Result.success(it)
        } ?: Result.failure(Exception("User is null after sign in"))
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        Result.failure(Exception("Invalid verification code", e))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signInWithCredential(credential: PhoneAuthCredential): Result<FirebaseUser> = try {
        val authResult = firebaseAuth.signInWithCredential(credential).await()
        authResult.user?.let {
            Result.success(it)
        } ?: Result.failure(Exception("User is null after sign in"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun linkEmailToAccount(email: String, password: String): Result<FirebaseUser> = try {
        val currentUser = firebaseAuth.currentUser
            ?: return Result.failure(Exception("No authenticated user"))

        val credential = EmailAuthProvider.getCredential(email, password)
        val authResult = currentUser.linkWithCredential(credential).await()

        authResult.user?.let {
            Result.success(it)
        } ?: Result.failure(Exception("Failed to link email"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun sendEmailVerification(): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("No authenticated user"))

            if (currentUser.email.isNullOrEmpty()) {
                return Result.failure(Exception("No email linked to account"))
            }

            currentUser.sendEmailVerification().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isEmailVerified(): Boolean {
        firebaseAuth.currentUser?.reload()?.await()
        return firebaseAuth.currentUser?.isEmailVerified ?: false
    }

    override suspend fun updateDisplayName(displayName: String): Result<Unit> = try {
        val currentUser = firebaseAuth.currentUser
            ?: return Result.failure(Exception("No authenticated user"))

        val profileUpdates = userProfileChangeRequest {
            this.displayName = displayName
        }

        currentUser.updateProfile(profileUpdates).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updatePhotoUrl(photoUrl: String): Result<Unit> = try {
        val currentUser = firebaseAuth.currentUser
            ?: return Result.failure(Exception("No authenticated user"))

        val profileUpdates = userProfileChangeRequest {
            this.photoUri = photoUrl.toUri()
//            this.photoUri = android.net.Uri.parse(photoUrl)
        }

        currentUser.updateProfile(profileUpdates).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun updateUserProfile(name: String, photoUrl: String?) {
        val user = firebaseAuth.currentUser
        val profileUpdate =
            UserProfileChangeRequest.Builder()
                .setPhotoUri(photoUrl?.toUri())
                .setDisplayName(name.trim())
                .build()
        user?.updateProfile(profileUpdate)
    }


    override suspend fun linkAccountWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.currentUser!!.linkWithCredential(firebaseCredential).await()
    }

    override suspend fun signInWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(firebaseCredential).await()
    }

    override suspend fun signInWithEmail(
        email: String,
        password: String,
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun deleteAccount(): Result<Unit> = try {
        val currentUser = firebaseAuth.currentUser
            ?: return Result.failure(Exception("No authenticated user"))

        currentUser.delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun reauthenticateWithPhone(
        verificationId: String,
        code: String,
    ): Result<Unit> = try {
        val currentUser = firebaseAuth.currentUser
            ?: return Result.failure(Exception("No authenticated user"))

        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        currentUser.reauthenticate(credential).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}