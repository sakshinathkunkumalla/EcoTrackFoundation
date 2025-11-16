package com.example.ecotrack

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecotrack.pages.*
import com.example.ecotrack.AuthViewModel

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    val authViewModel = AuthViewModel()

    NavHost(navController = nav, startDestination = "splash") {

        composable("splash") {
            SplashScreen { nav.navigate("onboarding") }
        }

        composable("onboarding") {
            OnboardingScreen { nav.navigate("login") }
        }

        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { nav.navigate("home") },
                onRegisterClicked = { nav.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = { nav.navigate("home") }
            )
        }

        composable("home") {
            HomeScreen(
                onLogout = {
                    authViewModel.signOut()
                    nav.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
    }
}