package com.renri.geminiaifitness.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.renri.geminiaifitness.ui.LoginScreen
import com.renri.geminiaifitness.ui.MainScreen
import com.renri.geminiaifitness.ui.RegistrationScreen
import com.renri.geminiaifitness.ui.SettingsDifficultyScreen
import com.renri.geminiaifitness.ui.themes.SettingsScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Registration : Screen("registration")
    object Main : Screen("main")
    object Settings : Screen("Settings")
    object SettingsDifficulty : Screen("settings_difficulty")
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Registration.route) {
            RegistrationScreen(navController)
        }
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Screen.SettingsDifficulty.route){
            SettingsDifficultyScreen(navController)
        }
    }
}
