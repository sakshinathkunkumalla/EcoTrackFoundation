package com.example.ecotrack

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentUser = mutableStateOf(firebaseAuth.currentUser)
    val authError = mutableStateOf<String?>(null)
    val loading = mutableStateOf(false)

    // Email & Password Sign-In
    fun signInWithEmail(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            authError.value = "Email and password cannot be empty"
            return
        }

        loading.value = true
        authError.value = null

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                loading.value = false
                if (task.isSuccessful) {
                    currentUser.value = firebaseAuth.currentUser
                    onSuccess()
                } else {
                    authError.value = task.exception?.localizedMessage ?: "Login failed"
                }
            }
    }

    // Email & Password Registration
    fun registerWithEmail(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            authError.value = "Email and password cannot be empty"
            return
        }

        loading.value = true
        authError.value = null

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                loading.value = false
                if (task.isSuccessful) {
                    currentUser.value = firebaseAuth.currentUser
                    onSuccess()
                } else {
                    authError.value = task.exception?.localizedMessage ?: "Registration failed"
                }
            }
    }

    // Sign Out
    fun signOut() {
        firebaseAuth.signOut()
        currentUser.value = null
    }

    // Google Sign-In using ID Token
    fun firebaseAuthWithGoogle(idToken: String, onSuccess: () -> Unit) {
        loading.value = true
        authError.value = null

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                loading.value = false
                if (task.isSuccessful) {
                    currentUser.value = firebaseAuth.currentUser
                    onSuccess()
                } else {
                    authError.value = task.exception?.localizedMessage ?: "Google sign-in failed"
                }
            }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount, onSuccess: () -> Unit) {
        account.idToken?.let { idToken ->
            firebaseAuthWithGoogle(idToken, onSuccess)
        } ?: run {
            authError.value = "Google sign-in failed: ID token is null"
        }
    }

}