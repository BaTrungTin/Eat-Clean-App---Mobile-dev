package com.team.eatcleanapp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team.eatcleanapp.ui.navigation.Destination
import com.team.eatcleanapp.ui.theme.EatCleanAppMobiledevTheme
import com.team.eatcleanapp.ui.theme.FernGreen
import com.team.eatcleanapp.ui.theme.JungleGreen
import com.team.eatcleanapp.ui.theme.LightGrayGreen
import com.team.eatcleanapp.ui.theme.LightGreen

@Composable
fun AppEatClean()
{
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
                            navController.navigate(destination.route)
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
        AppNavHost(navController, Modifier.padding(paddingValues))
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = Destination.HOME.route,
        modifier = modifier
    ) {}
}


@Preview(showBackground = true)
@Composable
fun AppBottomBarPrivew()
{
    EatCleanAppMobiledevTheme {
        AppEatClean()
    }
}