package com.team.eatcleanapp.ui.components

import android.graphics.drawable.Icon
import android.widget.Space
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.eatcleanapp.R
import com.team.eatcleanapp.ui.theme.EatCleanAppMobiledevTheme
import com.team.eatcleanapp.ui.theme.JungleGreen
import com.team.eatcleanapp.ui.theme.LightGrayGreen
import com.team.eatcleanapp.ui.theme.White

@Composable
fun GenderSwitch(
    selectedGender: String,
    onGenderChange: (String) -> Unit
) {
    val offsetX by animateDpAsState( if (selectedGender == "male") 0.dp else 45.dp)

    Box(
        modifier = Modifier
            .width(100.dp)
            .height(40.dp)
            .background(LightGrayGreen, RoundedCornerShape(60.dp))
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .offset(offsetX)
                .fillMaxHeight()
                .width(45.dp)
                .background(White, RoundedCornerShape(60.dp))
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier.width(45.dp), contentAlignment = Alignment.Center)
            {
                GenderIcon(
                    icon = R.drawable.boy,
                    isSelected = selectedGender == "male",
                    onClick = { onGenderChange("male") }
                )
            }

            Box(modifier = Modifier.width(45.dp), contentAlignment = Alignment.Center) {
                GenderIcon(
                    icon = R.drawable.girl,
                    isSelected = selectedGender == "female",
                    onClick = { onGenderChange("female") }
                )
            }
        }
    }
}

@Composable
fun GenderIcon(icon: Int, isSelected: Boolean, onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint =
                if (isSelected)
                    JungleGreen
                else
                    JungleGreen.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GenderSwitchPreview()
{
    EatCleanAppMobiledevTheme {
        GenderSwitch(
            selectedGender = "female",
            onGenderChange = {}
        )
    }
}