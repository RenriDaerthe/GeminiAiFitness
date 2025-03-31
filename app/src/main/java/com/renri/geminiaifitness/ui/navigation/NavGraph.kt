package com.renri.geminiaifitness.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.renri.geminiaifitness.ui.LoginScreen
import com.renri.geminiaifitness.ui.MainScreen
import com.renri.geminiaifitness.ui.RegistrationScreen
import com.renri.geminiaifitness.ui.DifficultySettings
import com.renri.geminiaifitness.ui.SettingsScreen
import com.renri.geminiaifitness.ui.viewmodels.DifficultyViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.renri.geminiaifitness.ui.WorkOutSettingsScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Registration : Screen("registration")
    object Main : Screen("main")
    object Settings : Screen("settings")
    object SettingsDifficulty : Screen("settings_difficulty")
    object WorkOutSettings : Screen("workout_settings")
}

@Composable
fun AppNavGraph(application: Application) {
    val navController: NavHostController = rememberNavController()

    //  Use a factory to create DifficultyViewModel with Application context
    val difficultyViewModel: DifficultyViewModel = viewModel(factory = viewModelFactory {
        initializer { DifficultyViewModel(application) }
    })

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController, difficultyViewModel) //  Pass ViewModel to MainScreen
        }
        composable(Screen.SettingsDifficulty.route) {
            DifficultySettings(navController, difficultyViewModel) //  Pass ViewModel to DifficultySettings
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.WorkOutSettings.route) {
            WorkOutSettingsScreen(navController)
        }
        composable(Screen.Registration.route) {
            RegistrationScreen(navController)
        }

    }
}
