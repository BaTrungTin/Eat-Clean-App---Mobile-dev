package com.team.eatcleanapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team.eatcleanapp.ui.navigation.Destination
import com.team.eatcleanapp.ui.screens.favorite.FavoriteScreen
import com.team.eatcleanapp.ui.screens.menu.MenuScreen
import com.team.eatcleanapp.ui.theme.EatCleanAppMobiledevTheme
import com.team.eatcleanapp.ui.theme.FernGreen
import com.team.eatcleanapp.ui.theme.JungleGreen
import com.team.eatcleanapp.ui.theme.LightGrayGreen
import com.team.eatcleanapp.ui.theme.LightGreen

@Composable
fun AppEatClean(
    onNavigateToDetail: (String) -> Unit
) {
    val navController = rememberNavController()
    val startDestination = Destination.HOME
    var selectedDestination by rememberSaveable {
        mutableIntStateOf(startDestination.ordinal)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = LightGreen,
                windowInsets = NavigationBarDefaults.windowInsets,
            ) {
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            selectedDestination = index
                        },

                        icon = {
                            Icon(
                                painter = painterResource(destination.iconRes),
                                contentDescription = destination.contentDescription,
                                modifier = if (selectedDestination == index)
                                    Modifier.size(38.dp)
                                else Modifier.size(30.dp)
                            )
                        },

                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = FernGreen,
                            selectedIconColor = JungleGreen,
                            indicatorColor = LightGrayGreen
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        AppNavHost(navController, onNavigateToDetail, Modifier.padding(paddingValues))
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    onNavigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = Destination.HOME.route,
        modifier = modifier
    ) {
        composable(Destination.HOME.route) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Home Screen - Đang cập nhật")
            }
        }
        
        composable(Destination.MENU.route) {
            MenuScreen(
                navController = navController,
                onMealClick = onNavigateToDetail
            )
        }
        
        // Đã thay thế Placeholder bằng FavoriteScreen thật
        composable(Destination.FAVORITE.route) {
            FavoriteScreen(navController = navController)
        }
        
        composable(Destination.PROFILE.route) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Profile Screen - Đang cập nhật")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppBottomBarPrivew()
{
    EatCleanAppMobiledevTheme {
        AppEatClean(onNavigateToDetail = {})
    }
}
