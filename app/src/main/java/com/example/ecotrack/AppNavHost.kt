package com.example.ecotrack

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecotrack.model.AppDatabase
import com.example.ecotrack.pages.*
import com.example.ecotrack.repository.ActivityRepository
import com.example.ecotrack.viewmodel.ActivityViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val authViewModel = AuthViewModel()

    // Get DAO from Room database
    val dao = AppDatabase.getDatabase(context).activityDao()
    val repository = ActivityRepository(dao)
    val activityViewModel = ActivityViewModel(repository)

    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen { navController.navigate("onboarding") }
        }

        composable("onboarding") {
            OnboardingScreen { navController.navigate("login") }
        }

        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { navController.navigate("home") },
                onRegisterClicked = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = { navController.navigate("home") }
            )
        }

        composable("home") {
            HomeScreen(
                activityViewModel = activityViewModel,
                onLogout = {
                    authViewModel.signOut()
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onAddActivity = { navController.navigate("activity") },
                onEditActivity = { activityId -> navController.navigate("activity/$activityId") }
            )
        }

        composable("activity") {
            ActivityScreen(
                viewModel = activityViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("activity/{activityId}") { backStackEntry ->
            val activityId = backStackEntry.arguments?.getString("activityId")?.toIntOrNull()
            ActivityScreen(
                viewModel = activityViewModel,
                activityId = activityId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}