package com.team.eatcleanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.team.eatcleanapp.ui.navigation.Screen
import com.team.eatcleanapp.ui.screens.auth.LoginScreen
import com.team.eatcleanapp.ui.screens.auth.RegisterScreen
import com.team.eatcleanapp.ui.screens.meal.DetailScreen
import com.team.eatcleanapp.ui.screens.onboarding.HealthCalculatorScreen
import com.team.eatcleanapp.ui.screens.splash.SplashScreen
import com.team.eatcleanapp.ui.theme.EatCleanAppMobiledevTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EatCleanAppMobiledevTheme {
                val navController = rememberNavController()

                // Bắt đầu từ màn hình Splash
                NavHost(navController = navController, startDestination = Screen.Splash.route) {
                    
                    // 1. Màn hình Chờ (Splash)
                    composable(Screen.Splash.route) {
                        SplashScreen(
                            onStart = {
                                // Chuyển sang Login
                                // (Sau này có thể check nếu đã login thì vào thẳng main_app)
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    // 2. Màn hình Đăng Nhập
                    composable(Screen.Login.route) {
                        LoginScreen(
                            onLoginSuccess = {
                                // Đăng nhập thành công -> Vào màn hình tính chỉ số sức khỏe
                                navController.navigate(Screen.Onboarding.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            },
                            onRegisterClick = {
                                navController.navigate(Screen.Register.route)
                            },
                            onForgotPasswordClick = {
                                // TODO: Xử lý quên mật khẩu
                            }
                        )
                    }

                    // 3. Màn hình Đăng Ký
                    composable(Screen.Register.route) {
                        RegisterScreen(
                            onRegisterSuccess = {
                                // Đăng ký thành công -> Vào màn hình tính chỉ số sức khỏe
                                navController.navigate(Screen.Onboarding.route) {
                                    popUpTo(Screen.Register.route) { inclusive = true }
                                }
                            },
                            onLoginClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                    
                    // 4. Màn hình Tính chỉ số sức khỏe (Onboarding)
                    composable(Screen.Onboarding.route) {
                        HealthCalculatorScreen(
                            isFirstTime = true, // Đây là lần đầu sau khi đăng ký/đăng nhập
                            onBackClick = {
                                // Không cho back về login nếu đã vào đây, hoặc có thể cho logout
                            },
                            onSaveSuccess = {
                                // Lưu xong chỉ số -> Vào màn hình chính
                                navController.navigate("main_app") {
                                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                                    // Xóa hết backstack để user không back lại màn hình auth được
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    // 5. Màn hình Chính (Bottom Navigation)
                    composable("main_app") {
                        AppEatClean(
                            onNavigateToDetail = { mealId ->
                                navController.navigate(Screen.MealDetail.createRoute(mealId))
                            }
                        )
                    }

                    // 6. Màn hình Chi tiết món ăn
                    composable(
                        route = Screen.MealDetail.route,
                        arguments = listOf(navArgument("mealId") { type = NavType.StringType })
                    ) {
                        DetailScreen(onBackClick = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}
