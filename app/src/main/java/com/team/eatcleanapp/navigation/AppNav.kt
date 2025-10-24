/*
package com.team.eatcleanapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team.eatcleanapp.ui.screens.splash.SplashScreen
import com.team.eatcleanapp.ui.screens.auth.LoginScreen
import com.team.eatcleanapp.ui.screens.auth.RegisterScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Register : Screen("register")
    data object Login : Screen("login")
}

@Composable
fun AppNav() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onStart = { nav.navigate(Screen.Register.route) })
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterClick = { */
/* UI only *//*
 },
                onGoLogin = { nav.navigate(Screen.Login.route) }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { */
/* UI only *//*
 },
                onGoRegister = { nav.navigate(Screen.Register.route) }
            )
        }
    }
}
*/
