package com.team.eatcleanapp.ui.screens.onboarding

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.eatcleanapp.R
import com.team.eatcleanapp.domain.model.enums.Gender
import com.team.eatcleanapp.domain.model.enums.Goal
import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.ui.components.BMICalculator
import com.team.eatcleanapp.ui.components.TDEECalculator
import com.team.eatcleanapp.ui.theme.EatCleanAppMobiledevTheme
import com.team.eatcleanapp.ui.theme.FernGreen
import com.team.eatcleanapp.ui.theme.JungleGreen
import com.team.eatcleanapp.ui.theme.LightGreen
import com.team.eatcleanapp.ui.theme.White
import com.team.eatcleanapp.ui.screens.onboarding.HealthViewModel
import com.team.eatcleanapp.ui.screens.profile.UserViewModel
import com.team.eatcleanapp.util.Result

@Composable
fun HealthCalculatorScreen(
    userId: String,
    isFirstTime: Boolean,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    healthViewModel: HealthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userProfileState by userViewModel.userProfile.collectAsState()
    val updateStateState = userViewModel.healthMetricsState.collectAsState()
    val bmiResult by healthViewModel.bmiResult.collectAsState()
    val bmrResult by healthViewModel.bmrResult.collectAsState()
    val tdeeResult by healthViewModel.tdeeResult.collectAsState()
    val bmiCategory by healthViewModel.bmiCategory.collectAsState()
    val activityDescription by healthViewModel.activityLevelDescription.collectAsState()
    val dailyCaloriesTarget by healthViewModel.dailyCaloriesTarget.collectAsState()

    var isBMI by remember { mutableStateOf(true) }
    var gender by rememberSaveable { mutableStateOf("male") }
    var age by rememberSaveable { mutableIntStateOf(18) }
    var userWeight by rememberSaveable { mutableStateOf("") }
    var userHeight by rememberSaveable { mutableStateOf("") }
    var minutesPerDay by rememberSaveable { mutableStateOf("") }
    var daysPerWeek by rememberSaveable { mutableStateOf("") }
    var hasPrefilled by rememberSaveable { mutableStateOf(false) }

    val weightValue = userWeight.toDoubleOrNull()
    val heightValue = userHeight.toDoubleOrNull()
    val minutesPerDayInt = minutesPerDay.toIntOrNull()
    val daysPerWeekInt = daysPerWeek.toIntOrNull()

    LaunchedEffect(userId) {
        userViewModel.getUserProfile(userId)
    }

    val currentUser = (userProfileState as? Result.Success<User>)?.data

    LaunchedEffect(userProfileState) {
        val user = (userProfileState as? Result.Success)?.data ?: return@LaunchedEffect
        if (!hasPrefilled) {
            gender = if (user.gender == Gender.MALE) "male" else "female"
            if (user.age > 0) age = user.age
            if (user.weight > 0) userWeight = user.weight.toString()
            if (user.height > 0) userHeight = user.height.toString()
            if (user.activityMinutesPerDay > 0) minutesPerDay = user.activityMinutesPerDay.toString()
            if (user.activityDaysPerWeek > 0) daysPerWeek = user.activityDaysPerWeek.toString()
            hasPrefilled = true
        }
    }

    LaunchedEffect(gender, age, userWeight, userHeight) {
        val w = userWeight.toFloatOrNull()
        val h = userHeight.toFloatOrNull()
        if (w != null && h != null && age > 0) {
            healthViewModel.calculateBMI(w, h)
            val genderEnum = if (gender == "male") Gender.MALE else Gender.FEMALE
            healthViewModel.calculateBMR(w, h, age, genderEnum)
        }
    }

    LaunchedEffect(bmrResult, minutesPerDay) {
        val bmr = bmrResult
        val minutes = minutesPerDay.toIntOrNull()
        if (bmr != null && minutes != null && minutes > 0 && age > 0) {
            val activityLevel = healthViewModel.inferActivityLevel(minutes, age)
            healthViewModel.calculateTDEE(bmr, activityLevel)
        }
    }

    LaunchedEffect(tdeeResult, currentUser?.goal) {
        val tdee = tdeeResult
        val goal: Goal? = currentUser?.goal
        if (tdee != null && goal != null) {
            healthViewModel.calculateDailyCaloriesTarget(tdee, goal)
        }
    }

    LaunchedEffect(updateStateState.value) {
        when (val state = updateStateState.value) {
            is Result.Success<*> -> {
                Toast.makeText(context, "Cập nhật hồ sơ thành công", Toast.LENGTH_SHORT).show()
                userViewModel.resetHealthMetricsState()
                onSaveSuccess()
            }

            is Result.Error -> {
                val message = state.message ?: "Không thể cập nhật hồ sơ."
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                userViewModel.resetHealthMetricsState()
            }

            else -> Unit
        }
    }

    val isBMIDataComplete = weightValue != null && heightValue != null && weightValue > 0 && heightValue > 0 && age > 0
    val isTdeeDataComplete = minutesPerDayInt != null && daysPerWeekInt != null &&
        minutesPerDayInt > 0 && daysPerWeekInt > 0
    val isAllDataComplete = currentUser != null && isBMIDataComplete && isTdeeDataComplete
    val isSaving = updateStateState.value is Result.Loading
    val isLoadingProfile = userProfileState is Result.Loading || userProfileState is Result.Idle

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen)
            .padding(26.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(26.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isFirstTime) {
                    IconButton(onClick = onBackClick) {
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

            Row(
                modifier = Modifier.wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBMI) "Tỷ số khối cơ thể - BMI" else "BMR - TDEE",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 26.sp),
                    color = FernGreen
                )

                Spacer(modifier = Modifier.weight(1f))

                Switch(
                    checked = !isBMI,
                    onCheckedChange = { isBMI = !it },
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = FernGreen,
                        uncheckedTrackColor = White,
                        uncheckedBorderColor = FernGreen,
                        checkedThumbColor = White,
                        checkedTrackColor = FernGreen
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isBMI) {
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
            } else {
                TDEECalculator(
                    BMR = bmrResult?.toDouble(),
                    gender = gender,
                    value = minutesPerDay,
                    onValueChange = { minutesPerDay = it },
                    value_2 = daysPerWeek,
                    onValueChange_2 = { daysPerWeek = it }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            MetricsSummary(
                bmi = bmiResult,
                bmiCategory = bmiCategory,
                bmr = bmrResult,
                tdee = tdeeResult,
                activityDescription = activityDescription,
                dailyCaloriesTarget = dailyCaloriesTarget
            )

            if (!isAllDataComplete) {
                Text(
                    text = "Điền đầy đủ thông tin để lưu hồ sơ.",
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
        }
        
        // Floating action button with check icon at bottom right
        if (isAllDataComplete) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(
                    onClick = {
                        val genderEnum = if (gender == "male") Gender.MALE else Gender.FEMALE
                        val updatedUser = currentUser!!.copy(
                            gender = genderEnum,
                            age = age,
                            weight = weightValue ?: currentUser.weight,
                            height = heightValue ?: currentUser.height,
                            activityMinutesPerDay = minutesPerDayInt ?: currentUser.activityMinutesPerDay,
                            activityDaysPerWeek = daysPerWeekInt ?: currentUser.activityDaysPerWeek,
                            activityLevel = healthViewModel.inferActivityLevel(
                                minutesPerDayInt ?: currentUser.activityMinutesPerDay,
                                age
                            )
                        )
                        userViewModel.updateHealthMetrics(updatedUser)
                    },
                    modifier = Modifier
                        .padding(24.dp),
                    containerColor = FernGreen,
                    contentColor = White
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Hoàn thành",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }

        if (isSaving || isLoadingProfile) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun MetricsSummary(
    bmi: Float?,
    bmiCategory: String,
    bmr: Float?,
    tdee: Float?,
    activityDescription: String,
    dailyCaloriesTarget: Int?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Kết quả tạm thời",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
            color = JungleGreen
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("BMI: ${bmi?.let { "%.1f".format(it) } ?: "--"} ($bmiCategory)")
        Text("BMR: ${bmr?.let { "%.0f".format(it) } ?: "--"} kcal")
        Text("TDEE: ${tdee?.let { "%.0f".format(it) } ?: "--"} kcal")
        if (dailyCaloriesTarget != null) {
            Text("Calo mục tiêu: $dailyCaloriesTarget kcal")
        }
        if (activityDescription.isNotBlank()) {
            Text("Hoạt động: $activityDescription")
        }
    }
}

