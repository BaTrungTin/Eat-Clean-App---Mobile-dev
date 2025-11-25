package com.team.eatcleanapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.eatcleanapp.ui.theme.Black
import com.team.eatcleanapp.ui.theme.EatCleanAppMobiledevTheme
import com.team.eatcleanapp.ui.theme.LightGreen
import com.team.eatcleanapp.ui.theme.White

@Composable
fun BMICalculator(
    gender: String,
    onGenderChange: (String) -> Unit,
    onAgeChange: (Int) -> Unit,
    age: Int,
    weight: String,
    onWeightChange: (String) -> Unit,
    height: String,
    onHeightChange: (String) -> Unit
) {
    var isMetric by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGreen)
    ) {
        Text(
            "Giới tính & Tuổi",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = Black.copy(alpha = 0.6f),
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(White, RoundedCornerShape(60.dp))
                .padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GenderSwitch(
                selectedGender = gender,
                onGenderChange = onGenderChange
            )

            AgeSelector(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                age,
                onAgeSelected = onAgeChange
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        Row(
            modifier = Modifier
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.wrapContentHeight()
            ) {
                Text(
                    "Cân nặng",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = Black.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 16.dp, bottom = 10.dp)
                )

                InputWithUnit(
                    "kg",
                    "lbs",
                    isMetric,
                    onUnitChange = { isMetric = it },
                    value = weight,
                    onValueChange = onWeightChange
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.wrapContentHeight()
            ) {
                Text(
                    "Chiều cao",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = Black.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 16.dp, bottom = 10.dp)
                )

                InputWithUnit(
                    "cm",
                    "ft",
                    isMetric,
                    onUnitChange = { isMetric = it },
                    value = height,
                    onValueChange = onHeightChange
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BMICalculatorPreview()
{
    EatCleanAppMobiledevTheme {
        BMICalculator(
            gender = "male",
            onGenderChange = {},
            onAgeChange = {},
            18,
            weight = "70.0",
            onWeightChange = {},
            height = "175.0",
            onHeightChange = {}
        )
    }
}