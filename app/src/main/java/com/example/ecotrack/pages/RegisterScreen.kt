package com.example.ecotrack.pages

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecotrack.AuthViewModel
import com.example.ecotrack.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(authViewModel: AuthViewModel, onRegisterSuccess: () -> Unit) {
    val context = LocalContext.current // get context
    val username = rememberSaveable { mutableStateOf("") } // username state
    val email = rememberSaveable { mutableStateOf("") } // email state
    val password = rememberSaveable { mutableStateOf("") } // password state
    val rePassword = rememberSaveable { mutableStateOf("") } // re-password state
    val authError by authViewModel.authError // auth error
    val loading by authViewModel.loading // loading state
    var passwordMatchError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(authViewModel.currentUser.value) {
        authViewModel.currentUser.value?.let { onRegisterSuccess() } // auto login
    }

    // Google Sign-In setup
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("389736738596-i0rr69fvvqafvvrjb9a6e0t0r3qkfdlt.apps.googleusercontent.com")
        .requestEmail()
        .build() // google options
    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso) // google client
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data) // get account
            try {
                val account = task.getResult(Exception::class.java)
                account?.let { authViewModel.firebaseAuthWithGoogle(it) { onRegisterSuccess() } } // google login
            } catch (e: Exception) {
                authViewModel.authError.value = e.message
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF80CBC4), Color(0xFFE0F2F1)))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // header
            Text("Create Account", fontSize = 28.sp, color = Color(0xFF00796B))
            Spacer(modifier = Modifier.height(8.dp))
            // subtitle
            Text("Sign up to continue", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            // Input Fields
            OutlinedTextField(value = username.value, onValueChange = { username.value = it }, label = { Text("Username") }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(12.dp))

            // email field
            OutlinedTextField(value = email.value, onValueChange = { email.value = it }, label = { Text("Email") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(12.dp))

            // password field
            OutlinedTextField(value = password.value, onValueChange = { password.value = it }, label = { Text("Password (6+ chars)") }, singleLine = true, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(12.dp))

            // re-password field
            OutlinedTextField(value = rePassword.value, onValueChange = { rePassword.value = it }, label = { Text("Re-enter Password") }, singleLine = true, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // mismatch check
                    if (password.value != rePassword.value) passwordMatchError = "Passwords do not match"
                    // register email
                    else { passwordMatchError = null; authViewModel.registerWithEmail(email.value, password.value) { onRegisterSuccess() } }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !loading
            ) {
                if (loading) CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                else Text("Register", color = Color.White) // button text
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Google Button
            OutlinedButton(
                onClick = {
                    authViewModel.signOut() // sign out
                    googleSignInClient.signOut().addOnCompleteListener { launcher.launch(googleSignInClient.signInIntent) }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    // google icon
                    Image(painter = painterResource(id = R.drawable.google_logo), contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Continue with Google", fontSize = 16.sp) // google text
                }
            }

            // Error messages
            passwordMatchError?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 12.dp)) }
            authError?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 12.dp)) }
        }
    }
}