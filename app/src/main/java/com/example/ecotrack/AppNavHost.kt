package com.example.ecotrack

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecotrack.model.AppDatabase
import com.example.ecotrack.pages.*
import com.example.ecotrack.repository.ActivityRepository
import com.example.ecotrack.viewmodel.ActivityViewModel
import com.example.ecotrack.viewmodel.DashboardViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Auth ViewModel (singleton for the whole app)
    val authViewModel: AuthViewModel = viewModel()

    // Activity ViewModel (Room + Repository)
    val dao = AppDatabase.getDatabase(context).activityDao()
    val repository = ActivityRepository(dao)
    val activityViewModel: ActivityViewModel = viewModel(factory = ActivityViewModel.Factory(repository))

    NavHost(navController = navController, startDestination = "splash") {

        // Splash Screen
        composable("splash") {
            SplashScreen {
                navController.navigate("onboarding") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        // Onboarding
        composable("onboarding") {
            OnboardingScreen {
                navController.navigate("login") {
                    popUpTo("onboarding") { inclusive = true }
                }
            }
        }

        // Login
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClicked = { navController.navigate("register") }
            )
        }

        // Register
        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        // Home / Dashboard
        composable("home") {
            HomeScreen(
                activityViewModel = activityViewModel,
                onLogout = {
                    authViewModel.signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onAddActivity = { navController.navigate("activity") },
                onEditActivity = { activityId -> navController.navigate("activity/$activityId") }
            )
        }

        // Add Activity
        composable("activity") {
            ActivityScreen(
                viewModel = activityViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Edit Activity
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
