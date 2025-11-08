package com.team.eatcleanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.team.eatcleanapp.ui.screens.Login
import com.team.eatcleanapp.ui.screens.WellnessOverview
import com.team.eatcleanapp.ui.theme.EatCleanAppMobiledevTheme

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EatCleanAppMobiledevTheme {
                WellnessOverview(
                    isFirstTime = true,
                    onBackClick = {},
                    onSaveSuccess = {}
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EatCleanAppMobiledevTheme {
        WellnessOverview(
            isFirstTime = true,
            onBackClick = {},
            onSaveSuccess = {}
        )
    }
}