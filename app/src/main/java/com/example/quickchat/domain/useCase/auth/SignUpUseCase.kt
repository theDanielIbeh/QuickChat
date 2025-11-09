package com.example.quickchat.domain.useCase.auth

import com.example.quickchat.data.auth.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SignUpUseCase(
    private val accountService: FirebaseAuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
    ) {
        accountService.createUserWithEmailAndPassword(email, password, firstName, lastName)
//        val user = auth.currentUser
//        val profileUpdate =
//            UserProfileChangeRequest.Builder()
//                .setDisplayName("${firstName.trim()} ${lastName.trim()}")
//                .build()
//        user?.updateProfile(profileUpdate)

//        try {
//            accountService.linkAccountWithEmail(email, password)
//            val user = auth.currentUser
//            val profileUpdate = UserProfileChangeRequest.Builder()
//                .setDisplayName("${.firstName.trim()} ${.lastName.trim()}")
//                .build()
//            user?.updateProfile(profileUpdate)
//            signUpState.update {
//                it.copy(
//                    isRegisterSuccessful = true,
//                    isRegisterFailed = false
//                )
//            }
//        } catch (e: FirebaseAuthException) {
//            signUpState.update {
//                it.copy(
//                    isRegisterFailed = true,
//                    isRegisterSuccessful = false
//                )
//            }
//        }
    }
}