package com.example.quickchat.data.auth

import android.app.Activity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.Flow

/**
 * Repository for handling Firebase Authentication operations.
 * Handles phone authentication, email linking, and user session management.
 */
interface FirebaseAuthRepository {
    /**
     * Current authenticated user as a Flow.
     * Emits null when user is logged out.
     */
    val currentUser: Flow<FirebaseUser?>

    /**
     * Get the current user synchronously.
     */
    fun getCurrentUser(): FirebaseUser?

    /**
     * Check if user is authenticated.
     */
    fun isUserAuthenticated(): Boolean

    /**
     * Start phone number verification process.
     *
     * @param phoneNumber Phone number in E.164 format (e.g., +1234567890)
     * @param activity The activity for SMS retriever API
     */
    fun startPhoneNumberVerification(
        phoneNumber: String,
        activity: Activity
    ): Flow<PhoneAuthResult>

    /**
     * Resend verification code.
     */
    fun resendVerificationCode(
        phoneNumber: String,
        activity: Activity,
        resendToken: PhoneAuthProvider.ForceResendingToken
    ): Flow<PhoneAuthResult>

    /**
     * Sign in with phone auth credential.
     *
     * @param verificationId The verification ID from onCodeSent
     * @param code The SMS code entered by user
     * @return Result with FirebaseUser on success
     */
    suspend fun signInWithPhoneAuthCredential(
        verificationId: String,
        code: String,
    ): Result<FirebaseUser>

    /**
     * Sign in with credential directly (for auto-verification).
     */
    suspend fun signInWithCredential(credential: PhoneAuthCredential): Result<FirebaseUser>

    /**
     * Link email to current phone-authenticated account.
     *
     * @param email User's email address
     * @param password Password for email/password authentication
     * @return Result with updated FirebaseUser
     */
    suspend fun linkEmailToAccount(email: String, password: String): Result<FirebaseUser>

    /**
     * Send email verification to current user.
     * User must have email linked to their account.
     */
    suspend fun sendEmailVerification(): Result<Unit>

    /**
     * Check if user's email is verified.
     */
    suspend fun isEmailVerified(): Boolean

    /**
     * Update user's display name.
     */
    suspend fun updateDisplayName(displayName: String): Result<Unit>

    /**
     * Update user's photo URL.
     */
    suspend fun updatePhotoUrl(photoUrl: String): Result<Unit>

    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    )

    suspend fun linkAccountWithGoogle(idToken: String)

    suspend fun signInWithGoogle(idToken: String)

    suspend fun signInWithEmail(
        email: String,
        password: String,
    )

    /**
     * Sign out current user.
     */
    fun signOut()

    /**
     * Delete current user account.
     * Note: Recent authentication required for this operation.
     */
    suspend fun deleteAccount(): Result<Unit>

    /**
     * Re-authenticate user with phone credential.
     * Required before sensitive operations like account deletion.
     */
    suspend fun reauthenticateWithPhone(
        verificationId: String,
        code: String,
    ): Result<Unit>
}