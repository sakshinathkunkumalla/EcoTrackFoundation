package com.example.ecotrack

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthViewModel : ViewModel() {

    // firebase instance
    private val firebaseAuth = FirebaseAuth.getInstance()

    // current user
    val currentUser = mutableStateOf(firebaseAuth.currentUser)
    val authError = mutableStateOf<String?>(null)
    val loading = mutableStateOf(false)

    // check user
    fun checkCurrentUser(onUserFound: () -> Unit, onUserNotFound: () -> Unit) {
        if (firebaseAuth.currentUser != null) onUserFound() else onUserNotFound()
    }

    fun signInWithEmail(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            authError.value = "Email and password cannot be empty"
            return
        }
        loading.value = true
        authError.value = null

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            loading.value = false
            if (task.isSuccessful) {
                currentUser.value = firebaseAuth.currentUser
                onSuccess()
            } else {
                authError.value = task.exception?.localizedMessage ?: "Login failed"
            }
        }
    }

    // Register with email
    fun registerWithEmail(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            authError.value = "Email and password cannot be empty"
            return
        }
        loading.value = true
        authError.value = null

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            loading.value = false
            if (task.isSuccessful) {
                currentUser.value = firebaseAuth.currentUser
                onSuccess()
            } else {
                authError.value = task.exception?.localizedMessage ?: "Registration failed"
            }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String, onSuccess: () -> Unit) {
        loading.value = true
        authError.value = null
        val credential = GoogleAuthProvider.getCredential(idToken, null) // google credential
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            loading.value = false // stop loading
            if (task.isSuccessful) {
                currentUser.value = firebaseAuth.currentUser // set user
                onSuccess()
            } else {
                authError.value = task.exception?.localizedMessage ?: "Google sign-in failed"
            }
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount, onSuccess: () -> Unit) {
        account.idToken?.let { firebaseAuthWithGoogle(it, onSuccess) } ?: run {
            authError.value = "Google sign-in failed: ID token is null" // id token null
        }
    }

    // sign out
    fun signOut() {
        firebaseAuth.signOut()
        currentUser.value = null
    }
}