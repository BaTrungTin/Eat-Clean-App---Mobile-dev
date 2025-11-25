package com.team.eatcleanapp.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.eatcleanapp.R
import com.team.eatcleanapp.ui.components.BMICalculator
import com.team.eatcleanapp.ui.components.TDEECalculator
import com.team.eatcleanapp.ui.theme.EatCleanAppMobiledevTheme
import com.team.eatcleanapp.ui.theme.FernGreen
import com.team.eatcleanapp.ui.theme.JungleGreen
import com.team.eatcleanapp.ui.theme.LightGreen
import com.team.eatcleanapp.ui.theme.White

fun calculateBMR(gender: String, age: Int?, weight: Double?, height: Double?): Double?
{
    return age?.let { a ->
        weight?.let { w ->
            height?.let { h ->
                when (gender)
                {
                    "male" -> { (10 * w) + (6.25 * h) - (5 * a) + 5 }
                    "female" -> { (10 * w) + (6.25 * h) - (5 * a) - 161 }

                    else -> 0.0
                }
            }
        }
    }
}

@Composable
fun HealthCalculatorScreen(
    isFirstTime: Boolean,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    var isBMI by remember { mutableStateOf(true)}

    var gender by rememberSaveable { mutableStateOf("male") }
    var age by rememberSaveable { mutableIntStateOf(18) }
    var userWeight by rememberSaveable { mutableStateOf("")}
    var userHeight by rememberSaveable { mutableStateOf("")}
    var minutesPerDay by rememberSaveable { mutableStateOf("") }
    var daysPerWeek by rememberSaveable { mutableStateOf("") }

    val weight = userWeight.toDoubleOrNull()
    val height = userHeight.toDoubleOrNull()
    val bmr = calculateBMR(gender, age, weight, height)

    val isBMIDataComplete = remember(gender, age, userWeight, userHeight)
    {
        age > 0 && userWeight.isNotEmpty() && userHeight.isNotEmpty() && (weight ?: 0.0) > 0.0 && (height ?: 0.0) > 0.0
    }

    val isTDEEDataComplete = remember(bmr, minutesPerDay, daysPerWeek) {
        bmr != null && minutesPerDay.isNotEmpty() && daysPerWeek.isNotEmpty() && (minutesPerDay.toIntOrNull() ?: 0) > 0 && (daysPerWeek.toIntOrNull() ?: 0) > 0
    }

    val isAllDataComplete = isBMIDataComplete && isTDEEDataComplete

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen)
            .padding(26.dp)
    ) {
        Spacer(modifier = Modifier.height(26.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isFirstTime) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back),
                        contentDescription = "Back",
                        tint = JungleGreen,
                        modifier = Modifier.size(38.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Hồ Sơ Sức Khỏe",
                    style = MaterialTheme.typography.titleLarge,
                    color = JungleGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row (
            modifier = Modifier.wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isBMI) "Tỷ số khối cơ thể - BMI"
                    else "BMR - TDEE",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 26.sp),
                color = FernGreen
            )

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = !isBMI,
                onCheckedChange = { isChecked ->
                    isBMI = !isChecked
                },

                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = FernGreen,
                    uncheckedTrackColor = White,
                    uncheckedBorderColor = FernGreen,
                    checkedThumbColor = White,
                    checkedTrackColor = FernGreen,
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isBMI)
            BMICalculator(
                gender = gender,
                onGenderChange = { gender = it },
                onAgeChange = { age = it },
                age = age,
                weight = userWeight,
                onWeightChange = { userWeight = it },
                height = userHeight,
                onHeightChange = { userHeight = it }
            )

        else
            TDEECalculator(
                BMR = bmr,
                gender = gender,
                value = minutesPerDay,
                onValueChange = { minutesPerDay = it },
                value_2 = daysPerWeek,
                onValueChange_2 = { daysPerWeek = it }
            )

        if (isAllDataComplete)
        {
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = onSaveSuccess,
                    modifier = Modifier
                        .size(310.dp, 75.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FernGreen),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "Hoàn thành",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 30.sp),
                        color = White
                    )
                }
            }

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HealthCalculatorPreview()
{
    EatCleanAppMobiledevTheme {
        HealthCalculatorScreen(
            isFirstTime = false,
            onBackClick = {},
            onSaveSuccess = {}
        )
    }
}