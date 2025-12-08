package com.example.ecotrack

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ecotrack.pages.*
import com.example.ecotrack.model.AppDatabase
import com.example.ecotrack.repository.*
import com.example.ecotrack.viewmodel.*

@Composable
fun AppNavHost() {

    val navController = rememberNavController()
    val context = LocalContext.current

    // Database with repositories
    val db = AppDatabase.getDatabase(context)
    val activityRepo = ActivityRepository(db.activityDao())
    val rewardRepo = RewardRepository(db.rewardDao())
    val challengeRepo = ChallengeRepository(db.challengeDao())

    // ViewModels
    val authViewModel: AuthViewModel = viewModel()
    val activityViewModel: ActivityViewModel =
        viewModel(factory = ActivityViewModel.Factory(activityRepo))
    val rewardViewModel: RewardViewModel =
        viewModel(factory = RewardViewModel.Factory(rewardRepo))
    val challengeViewModel: ChallengeViewModel =
        viewModel(factory = ChallengeViewModel.Factory(challengeRepo, rewardRepo))

    NavHost(navController = navController, startDestination = "splash") {

        //  Splash Screen
        composable("splash") {
            SplashScreen {
                if (authViewModel.currentUser.value != null) {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                } else {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
        }

        // Onboarding Screen
        composable("onboarding") {
            OnboardingScreen {
                navController.navigate("login") {
                    popUpTo("onboarding") { inclusive = true }
                }
            }
        }

        // Login Screen
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

        // Register Screen
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

        // Home Screen
        composable("home") {
            HomeScreen(
                activityViewModel = activityViewModel,
                challengeViewModel = challengeViewModel,
                rewardViewModel = rewardViewModel,
                onLogout = {
                    authViewModel.signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onAddActivity = { navController.navigate("activity/-1") },
                onEditActivity = { id -> navController.navigate("activity/$id") },
                onNavigateToChallenges = { navController.navigate("challenges") },
                onNavigateToRewards = { navController.navigate("rewards") }
            )
        }

        // Activity Add/Edit
        composable(
            "activity/{activityId}",
            arguments = listOf(navArgument("activityId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStack ->
            val activityId = backStack.arguments?.getInt("activityId")
            ActivityScreen(
                viewModel = activityViewModel,
                activityId = if (activityId != -1) activityId else null,
                navController = navController
            )
        }

        // Challenge List
        composable("challenges") {
            ChallengeListScreen(
                vm = challengeViewModel,
                onOpenChallenge = { id -> navController.navigate("challenge/$id") },
                onAddChallenge = { navController.navigate("challenge/add") },
                onBack = { navController.popBackStack() }  // ← pass back callback
            )
        }

        // Add Challenge
        composable("challenge/add") {
            AddChallengeScreen(
                vm = challengeViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Challenge Detail
        composable(
            "challenge/{challengeId}",
            arguments = listOf(navArgument("challengeId") { type = NavType.IntType })
        ) { backStack ->
            ChallengeDetailScreen(
                vm = challengeViewModel,
                rewardVm = rewardViewModel,
                challengeId = backStack.arguments?.getInt("challengeId"),
                onBack = { navController.popBackStack() }
            )
        }

        // Rewards Screen
        composable("rewards") {
            RewardsScreen(
                vm = rewardViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}