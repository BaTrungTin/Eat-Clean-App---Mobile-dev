package com.team.eatcleanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.IconButton
import androidx.compose.material3.Divider
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.ui.navigation.BottomNavItem
import com.team.eatcleanapp.ui.navigation.BottomNavigationBar
import com.team.eatcleanapp.ui.navigation.Screen
import com.team.eatcleanapp.ui.screens.auth.LoginScreen
import com.team.eatcleanapp.ui.screens.auth.RegisterScreen
import com.team.eatcleanapp.ui.screens.dailymenu.DailyMenuScreen
import com.team.eatcleanapp.ui.screens.favorite.FavoriteScreen
import com.team.eatcleanapp.ui.screens.meal.DetailScreen
import com.team.eatcleanapp.ui.screens.meal.MenuScreen
import com.team.eatcleanapp.ui.screens.onboarding.GoalSelectionScreen
import com.team.eatcleanapp.ui.screens.onboarding.HealthCalculatorScreen
import com.team.eatcleanapp.ui.screens.profile.ProfileScreen
import com.team.eatcleanapp.ui.screens.profile.UserViewModel
import com.team.eatcleanapp.ui.screens.splash.SplashScreen
import com.team.eatcleanapp.ui.screens.home.HomeScreen
import com.team.eatcleanapp.ui.screens.settings.SettingsScreen
import com.team.eatcleanapp.ui.screens.auth.AuthViewModel
import com.team.eatcleanapp.ui.theme.EatCleanAppMobiledevTheme
import com.team.eatcleanapp.util.Result

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EatCleanAppMobiledevTheme {
                EatCleanApp()
            }
        }
    }
}

@Composable
private fun EatCleanApp() {
    val navController = rememberNavController()
    var currentUserId by rememberSaveable { mutableStateOf<String?>(null) }
    var isLoggingOut by rememberSaveable { mutableStateOf(false) }
    
    // Reset isLoggingOut flag sau khi logout (chỉ reset một lần)
    LaunchedEffect(isLoggingOut) {
        if (isLoggingOut) {
            kotlinx.coroutines.delay(2000) // Đợi 2 giây để đảm bảo logout hoàn tất
            isLoggingOut = false
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onStart = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onUserLoggedIn = { userId ->
                    // Chỉ tự động đăng nhập nếu không phải đang logout
                    if (!isLoggingOut) {
                        currentUserId = userId
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                },
                skipAutoLogin = isLoggingOut
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { userId ->
                    isLoggingOut = false
                    currentUserId = userId
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onForgotPasswordClick = {
                    navController.navigate(Screen.Onboarding.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Onboarding.route) {
            val userId = currentUserId ?: DEFAULT_USER_ID
            HealthCalculatorScreen(
                userId = userId,
                isFirstTime = true,
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = {
                    navController.navigate(Screen.Onboarding.route + "/goal") {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.HealthProfile.route + "/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: (currentUserId ?: DEFAULT_USER_ID)
            HealthCalculatorScreen(
                userId = userId,
                isFirstTime = false,
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Profile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: (currentUserId ?: DEFAULT_USER_ID)
            ProfileScreen(
                userId = userId,
                onUpdateHealthClick = {
                    navController.navigate("${Screen.HealthProfile.route}/$userId")
                },
                onGoalClick = {
                    navController.navigate("${Screen.Onboarding.route}/goal?userId=$userId")
                },
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {
            val userId = currentUserId
            if (userId == null) {
                MissingUserContent(
                    onBackToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route)
                        }
                    }
                )
            } else {
                HomeScaffold(
                    rootNavController = navController,
                    userId = userId,
                    onLogoutSuccess = {
                        // Clear currentUserId và set flag khi logout thành công
                        currentUserId = null
                        isLoggingOut = true
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(
            route = Screen.MealDetail.route,
            arguments = listOf(
                navArgument("mealId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId")
            if (mealId != null) {
                val userId = currentUserId ?: DEFAULT_USER_ID
                DetailScreen(
                    mealId = mealId,
                    userId = userId,
                    onBackClick = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }

        composable(Screen.Menu.route) {
            MenuScreen(
                navController = navController,
                onMealClick = { mealId ->
                    navController.navigate(Screen.MealDetail.createRoute(mealId))
                }
            )
        }

        composable(Screen.Favorite.route) {
            val userId = currentUserId ?: DEFAULT_USER_ID
            FavoriteScreen(
                navController = navController,
                userId = userId
            )
        }

        composable(Screen.DailyMenu.route) {
            val userId = currentUserId ?: DEFAULT_USER_ID
            DailyMenuScreen(navController = navController, userId = userId)
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                navController = navController,
                userId = currentUserId ?: DEFAULT_USER_ID
            )
        }

        composable(
            route = Screen.Onboarding.route + "/goal",
            arguments = listOf(navArgument("userId") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val userIdArg = backStackEntry.arguments?.getString("userId")
            val userId = if (userIdArg.isNullOrBlank()) (currentUserId ?: DEFAULT_USER_ID) else userIdArg
            GoalSelectionScreen(
                navController = navController,
                userId = userId
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScaffold(
    rootNavController: NavHostController,
    userId: String,
    onLogoutSuccess: () -> Unit = {}
) {
    var selectedItem by rememberSaveable { mutableStateOf(BottomNavItem.HOME) }
    val userViewModel: UserViewModel = hiltViewModel()
    val userState = userViewModel.userProfile.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val authViewModel: AuthViewModel = hiltViewModel()
    val logoutState by authViewModel.logoutState.collectAsState()

    LaunchedEffect(userId) {
        userViewModel.getUserProfile(userId)
    }
    
    // Handle logout
    LaunchedEffect(logoutState) {
        val state = logoutState // Assign to local variable for smart cast
        when (state) {
            is Result.Success<*> -> {
                authViewModel.resetLogoutState()
                onLogoutSuccess() // Gọi callback để clear currentUserId và navigate
            }
            is Result.Error -> {
                Log.e("HomeScaffold", "Logout error: ${state.message}")
                authViewModel.resetLogoutState()
            }
            else -> Unit
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Menu",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Cài đặt") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        rootNavController.navigate(Screen.Settings.route)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Logout, contentDescription = null) },
                    label = { Text("Đăng xuất") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        authViewModel.logout()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    selected = selectedItem,
                    onItemSelected = { selectedItem = it }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
            ) {
                when (selectedItem) {
                BottomNavItem.HOME -> HomeScreen(
                    userId = userId,
                    onMealClick = { mealId ->
                        rootNavController.navigate(Screen.MealDetail.createRoute(mealId))
                    },
                    onUpdateHealthClick = {
                        rootNavController.navigate("${Screen.HealthProfile.route}/$userId")
                    },
                    onProfileClick = {
                        rootNavController.navigate(Screen.Profile.createRoute(userId))
                    },
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )

                    BottomNavItem.MENU -> MenuScreen(
                        navController = rootNavController,
                        onMealClick = { mealId ->
                            rootNavController.navigate(Screen.MealDetail.createRoute(mealId))
                        },
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        }
                    )

                    BottomNavItem.FAVORITE -> FavoriteScreen(
                        navController = rootNavController,
                        userId = userId,
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        }
                    )

                    BottomNavItem.DAILY_MENU -> DailyMenuScreen(
                        navController = rootNavController,
                        userId = userId,
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeDashboard(
    userState: Result<User>,
    onUpdateHealthClick: () -> Unit,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        when (userState) {
            is Result.Loading, Result.Idle -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is Result.Error -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = userState.message ?: "Không thể tải hồ sơ người dùng.")
                    Button(
                        onClick = onRetry,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Thử lại")
                    }
                }
            }

            is Result.Success -> {
                val user = userState.data
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Xin chào, ${user.name}",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Theo dõi thực đơn, cập nhật sức khỏe và khám phá thực đơn mới mỗi ngày.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HealthMetricsCard(user = user)

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onUpdateHealthClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Cập nhật hồ sơ sức khỏe")
                    }
                }
            }
        }
    }
}

@Composable
private fun HealthMetricsCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardDefaults.shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val metrics = user.healthMetrics
            if (metrics == null) {
                Text(
                    text = "Bạn chưa nhập hồ sơ sức khỏe. Nhấn \"Cập nhật\" để bắt đầu.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = "Chỉ số gần nhất",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )

                Spacer(modifier = Modifier.padding(top = 8.dp))

                MetricRow(label = "BMI", value = metrics.bmi.formatValue())
                MetricRow(label = "BMR", value = "${metrics.bmr.formatValue()} kcal")
                MetricRow(label = "TDEE", value = "${metrics.tdee.formatValue()} kcal")
            }
        }
    }
}

@Composable
private fun MetricRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
    }
}

private fun Float.formatValue(): String = "%.1f".format(this)
private fun Double.formatValue(): String = "%.1f".format(this)

@Composable
private fun MissingUserContent(onBackToLogin: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Không tìm thấy user", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBackToLogin) {
            Text("Quay lại đăng nhập")
        }
    }
}

private const val DEFAULT_USER_ID = "demo-user"
