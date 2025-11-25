package com.team.eatcleanapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.model.nutrition.MealIntake
import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.ui.screens.dailymenu.MenuViewModel
import com.team.eatcleanapp.ui.screens.nutrition.NutritionViewModel
import com.team.eatcleanapp.ui.screens.onboarding.HealthViewModel
import com.team.eatcleanapp.ui.screens.profile.UserViewModel
import com.team.eatcleanapp.ui.theme.JungleGreen
import com.team.eatcleanapp.util.Result
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    userId: String,
    onMealClick: (String) -> Unit,
    onUpdateHealthClick: () -> Unit,
    onProfileClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}, // New parameter for menu drawer
    menuViewModel: MenuViewModel = hiltViewModel(),
    nutritionViewModel: NutritionViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    healthViewModel: HealthViewModel = hiltViewModel()
) {
    val selectedDate = remember { mutableStateOf(Date()) }
    val dateFormat = remember { SimpleDateFormat("dd 'thg' MM", Locale("vi")) }
    
    // Normalize date to start of day to ensure consistency with daily menu
    val normalizedDate = remember(selectedDate.value) {
        com.team.eatcleanapp.util.DateUtils.getStartOfDay(selectedDate.value)
    }
    
    val dailyMenuState by menuViewModel.dailyMenu.collectAsState()
    val mealIntakeState by nutritionViewModel.mealIntake.collectAsState()
    val nutritionInfoState by nutritionViewModel.nutritionInfo.collectAsState()
    val saveIntakeState by nutritionViewModel.saveIntakeState.collectAsState()
    val updateConsumedState by nutritionViewModel.updateConsumedState.collectAsState()
    val userProfileState by userViewModel.userProfile.collectAsState()
    val tdeeResult by healthViewModel.tdeeResult.collectAsState()
    val dailyCaloriesTarget by healthViewModel.dailyCaloriesTarget.collectAsState()

    // Refresh user profile when screen is composed (e.g., when coming back from GoalScreen)
    LaunchedEffect(Unit) {
        android.util.Log.d("HomeScreen", "HomeScreen composed, refreshing user profile")
        userViewModel.getUserProfile(userId)
    }
    
    LaunchedEffect(userId, normalizedDate) {
        android.util.Log.d("HomeScreen", "Loading daily menu for userId=$userId, date=${normalizedDate.time}")
        menuViewModel.getDailyMenu(userId, normalizedDate)
        nutritionViewModel.getMealIntakeByDate(userId, normalizedDate)
        userViewModel.getUserProfile(userId)
    }
    
    // Get user data
    val user = (userProfileState as? Result.Success<User>)?.data
    
    // Track user goal to detect changes
    val userGoal = user?.goal

    // Refresh user profile when it changes (e.g., after updating goal)
    LaunchedEffect(userProfileState) {
        val state = userProfileState
        if (state is Result.Success<*>) {
            android.util.Log.d("HomeScreen", "User profile updated, target calories will be recalculated")
        }
    }

    // Calculate target calories based on user's health metrics and goal
    var targetCalories by remember { mutableStateOf(2680.0) }

    // Calculate target calories when user data is available or changes
    // Include userGoal in dependencies to detect goal changes
    LaunchedEffect(user, userGoal, dailyCaloriesTarget, tdeeResult) {
        android.util.Log.d("HomeScreen", "Recalculating target calories - user: ${user?.name}, goal: $userGoal, dailyCaloriesTarget: $dailyCaloriesTarget, tdeeResult: $tdeeResult")
        
        // Always recalculate when we have a goal to ensure it's up to date
        val calculatedTarget = when {
            // If we have TDEE and goal, calculate target (priority 1)
            tdeeResult != null && userGoal != null -> {
                android.util.Log.d("HomeScreen", "Calculating from TDEE and goal - TDEE: $tdeeResult, Goal: $userGoal")
                healthViewModel.calculateDailyCaloriesTarget(tdeeResult!!, userGoal)
                kotlinx.coroutines.delay(150)
                healthViewModel.dailyCaloriesTarget.value?.toDouble() ?: tdeeResult!!.toDouble()
            }
            // If we have health metrics with TDEE, use it (priority 2)
            user?.healthMetrics?.tdee != null && userGoal != null -> {
                val tdee = user.healthMetrics.tdee
                android.util.Log.d("HomeScreen", "Calculating from healthMetrics TDEE: $tdee, Goal: $userGoal")
                healthViewModel.calculateDailyCaloriesTarget(tdee, userGoal)
                kotlinx.coroutines.delay(150)
                healthViewModel.dailyCaloriesTarget.value?.toDouble() ?: tdee.toDouble()
            }
            // If we have user data, calculate TDEE first (priority 3)
            user != null && user.weight > 0 && user.height > 0 && user.age > 0 && userGoal != null -> {
                android.util.Log.d("HomeScreen", "Calculating from user data - weight: ${user.weight}, height: ${user.height}, age: ${user.age}, goal: $userGoal")
                val genderEnum = user.gender
                healthViewModel.calculateBMR(user.weight.toFloat(), user.height.toFloat(), user.age, genderEnum)
                kotlinx.coroutines.delay(150)
                val bmr = healthViewModel.bmrResult.value
                if (bmr != null) {
                    val activityLevel = user.activityLevel
                    healthViewModel.calculateTDEE(bmr, activityLevel)
                    kotlinx.coroutines.delay(150)
                    val tdee = healthViewModel.tdeeResult.value
                    if (tdee != null) {
                        healthViewModel.calculateDailyCaloriesTarget(tdee, userGoal)
                        kotlinx.coroutines.delay(150)
                        healthViewModel.dailyCaloriesTarget.value?.toDouble() ?: tdee.toDouble()
                    } else {
                        android.util.Log.w("HomeScreen", "TDEE is null, using default")
                        2680.0
                    }
                } else {
                    android.util.Log.w("HomeScreen", "BMR is null, using default")
                    2680.0
                }
            }
            // If we have calculated daily calories target, use it (but only if goal matches)
            dailyCaloriesTarget != null && userGoal != null -> {
                // Recalculate to ensure it matches current goal
                val tdee = tdeeResult ?: user?.healthMetrics?.tdee
                if (tdee != null) {
                    healthViewModel.calculateDailyCaloriesTarget(tdee, userGoal)
                    kotlinx.coroutines.delay(150)
                    healthViewModel.dailyCaloriesTarget.value?.toDouble() ?: dailyCaloriesTarget!!.toDouble()
                } else {
                    dailyCaloriesTarget!!.toDouble()
                }
            }
            // Default fallback
            else -> {
                android.util.Log.w("HomeScreen", "No user data available, using default target: 2680.0")
                2680.0
            }
        }
        
        targetCalories = calculatedTarget
        android.util.Log.d("HomeScreen", "Final target calories: $targetCalories")
    }

    LaunchedEffect(userId, normalizedDate, targetCalories) {
        if (targetCalories > 0) {
            android.util.Log.d("HomeScreen", "Calculating nutrition with target calories: $targetCalories")
            nutritionViewModel.calculateDailyNutrition(userId, normalizedDate, targetCalories)
        }
    }

    // Refresh meal intake after save or update
    LaunchedEffect(saveIntakeState) {
        when (saveIntakeState) {
            is Result.Success<*> -> {
                nutritionViewModel.resetSaveState()
                nutritionViewModel.getMealIntakeByDate(userId, normalizedDate)
            }
            else -> Unit
        }
    }

    LaunchedEffect(updateConsumedState) {
        when (updateConsumedState) {
            is Result.Success<*> -> {
                nutritionViewModel.resetUpdateState()
                nutritionViewModel.getMealIntakeByDate(userId, normalizedDate)
            }
            else -> Unit
        }
    }
    
    // Observe addMealState to refresh when meal is added from DetailScreen
    val addMealState by menuViewModel.addMealState.collectAsState()
    LaunchedEffect(addMealState) {
        when (addMealState) {
            is Result.Success<*> -> {
                android.util.Log.d("HomeScreen", "Meal added successfully, refreshing daily menu")
                menuViewModel.getDailyMenu(userId, normalizedDate)
                menuViewModel.resetAddMealState()
            }
            else -> Unit
        }
    }

    // Calculate consumed calories from meal intake
    val consumedCalories = remember(mealIntakeState) {
        when (val state = mealIntakeState) {
            is Result.Success<List<MealIntake>> -> {
                state.data.filter { it.isConsumed }.sumOf { it.consumedCalories }
            }
            else -> 0.0
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Menu icon (3 gạch)
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color(0xFF90C695),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Center: Calendar and date
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar",
                    tint = Color(0xFF90C695),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = dateFormat.format(selectedDate.value),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF90C695),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }

            // Right: Avatar placeholder - clickable to navigate to profile
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF90C695))
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👤",
                    fontSize = 20.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Calories Progress Card - Updated design
            item {
                CaloriesProgressCard(
                    targetCalories = targetCalories,
                    consumedCalories = consumedCalories
                )
            }
            
            // Warning message if over limit
            if (consumedCalories > targetCalories) {
                item {
                    CaloriesWarningCard(
                        excessCalories = consumedCalories - targetCalories
                    )
                }
            }

            // Daily Menu Section
            item {
                when (val state = dailyMenuState) {
                    is Result.Success<com.team.eatcleanapp.domain.model.dailymenu.DailyMenuDay> -> {
                        val mealIntakes = when (val intakeState = mealIntakeState) {
                            is Result.Success<List<MealIntake>> -> intakeState.data
                            else -> emptyList()
                        }
                        DailyMenuSection(
                            userId = userId,
                            date = normalizedDate,
                            dailyMenu = state.data,
                            mealIntakes = mealIntakes,
                            onMealClick = onMealClick,
                            onToggleConsumed = { mealIntakeId, isConsumed ->
                                nutritionViewModel.updateConsumedStatus(mealIntakeId, isConsumed)
                            },
                            onCreateMealIntake = { mealIntake ->
                                nutritionViewModel.saveMealIntake(mealIntake)
                            }
                        )
                    }
                    is Result.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = JungleGreen)
                        }
                    }
                    else -> {
                        EmptyDailyMenuSection()
                    }
                }
            }
        }
    }
}

@Composable
private fun CaloriesProgressCard(
    targetCalories: Double,
    consumedCalories: Double
) {
    val isOverLimit = consumedCalories > targetCalories
    val remaining = if (isOverLimit) 0.0 else (targetCalories - consumedCalories).coerceAtLeast(0.0)
    val progress = (consumedCalories / targetCalories).coerceIn(0.0, 1.0)
    val progressColor = if (isOverLimit) Color(0xFFFF5252) else JungleGreen // Red if over limit
    val remainingColor = if (isOverLimit) Color(0xFFFF5252) else JungleGreen

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Large circle with remaining calories
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier.size(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = progress.toFloat(),
                        modifier = Modifier.size(140.dp),
                        color = progressColor,
                        strokeWidth = 8.dp,
                        trackColor = Color(0xFFD0E8D4)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isOverLimit) "+${(consumedCalories - targetCalories).toInt()}" else "${remaining.toInt()}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = remainingColor,
                            fontSize = 32.sp
                        )
                        Text(
                            text = if (isOverLimit) "vượt quá" else "cần nạp",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF666666),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Right: Small circle with consumed
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(if (isOverLimit) Color(0xFFFF5252) else JungleGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${consumedCalories.toInt()}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 28.sp
                        )
                        Text(
                            text = "đã nạp",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyMenuSection(
    userId: String,
    date: Date,
    dailyMenu: com.team.eatcleanapp.domain.model.dailymenu.DailyMenuDay,
    mealIntakes: List<MealIntake>,
    onMealClick: (String) -> Unit,
    onToggleConsumed: (String, Boolean) -> Unit,
    onCreateMealIntake: (MealIntake) -> Unit
) {
    val dayNames = listOf("Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy")
    val calendar = Calendar.getInstance().apply { time = dailyMenu.date }
    val dayName = dayNames[calendar.get(Calendar.DAY_OF_WEEK) - 1]

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = dayName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = JungleGreen,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            MealTimeRow(
                userId = userId,
                date = date,
                mealType = "Sáng",
                mealCategory = com.team.eatcleanapp.domain.model.enums.MealCategory.BREAKFAST,
                meals = dailyMenu.breakfast,
                mealIntakes = mealIntakes.filter { it.mealType == com.team.eatcleanapp.domain.model.enums.MealCategory.BREAKFAST },
                onMealClick = onMealClick,
                onToggleConsumed = onToggleConsumed,
                onCreateMealIntake = onCreateMealIntake
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            MealTimeRow(
                userId = userId,
                date = date,
                mealType = "Trưa",
                mealCategory = com.team.eatcleanapp.domain.model.enums.MealCategory.LUNCH,
                meals = dailyMenu.lunch,
                mealIntakes = mealIntakes.filter { it.mealType == com.team.eatcleanapp.domain.model.enums.MealCategory.LUNCH },
                onMealClick = onMealClick,
                onToggleConsumed = onToggleConsumed,
                onCreateMealIntake = onCreateMealIntake
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            MealTimeRow(
                userId = userId,
                date = date,
                mealType = "Tối",
                mealCategory = com.team.eatcleanapp.domain.model.enums.MealCategory.DINNER,
                meals = dailyMenu.dinner,
                mealIntakes = mealIntakes.filter { it.mealType == com.team.eatcleanapp.domain.model.enums.MealCategory.DINNER },
                onMealClick = onMealClick,
                onToggleConsumed = onToggleConsumed,
                onCreateMealIntake = onCreateMealIntake
            )
        }
    }
}

@Composable
private fun MealTimeRow(
    userId: String,
    date: Date,
    mealType: String,
    mealCategory: com.team.eatcleanapp.domain.model.enums.MealCategory,
    meals: List<DailyMenuItem>,
    mealIntakes: List<MealIntake>,
    onMealClick: (String) -> Unit,
    onToggleConsumed: (String, Boolean) -> Unit,
    onCreateMealIntake: (MealIntake) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Meal type label
        Text(
            text = mealType,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = JungleGreen,
            fontSize = 16.sp,
            modifier = Modifier.width(60.dp)
        )
        
        // Meals container
        Column(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, JungleGreen, RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (meals.isEmpty()) {
                Text(
                    text = "Thêm món ăn",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                meals.forEach { meal ->
                    val mealIntake = mealIntakes.find { it.mealId == meal.mealId }
                    MealItemRow(
                        userId = userId,
                        date = date,
                        mealCategory = mealCategory,
                        meal = meal,
                        mealIntake = mealIntake,
                        onMealClick = { onMealClick(meal.mealId) },
                        onToggleConsumed = { isConsumed ->
                            mealIntake?.let {
                                onToggleConsumed(it.id, isConsumed)
                            } ?: run {
                                // Create new meal intake if doesn't exist
                                val newMealIntake = MealIntake(
                                    dailyMenuItemId = meal.id,
                                    userId = userId,
                                    mealId = meal.mealId,
                                    mealName = meal.mealName,
                                    date = date,
                                    mealType = mealCategory,
                                    calories = meal.totalCalories,
                                    portionSize = 1.0,
                                    isConsumed = isConsumed
                                )
                                onCreateMealIntake(newMealIntake)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MealItemRow(
    userId: String,
    date: Date,
    mealCategory: com.team.eatcleanapp.domain.model.enums.MealCategory,
    meal: DailyMenuItem,
    mealIntake: MealIntake?,
    onMealClick: () -> Unit,
    onToggleConsumed: (Boolean) -> Unit
) {
    val isConsumed = mealIntake?.isConsumed ?: false
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onMealClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = meal.mealName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        
        Checkbox(
            checked = isConsumed,
            onCheckedChange = onToggleConsumed,
            colors = CheckboxDefaults.colors(
                checkedColor = JungleGreen,
                uncheckedColor = Color(0xFF90C695)
            ),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun CaloriesWarningCard(excessCalories: Double) {
    val excess = excessCalories.toInt()
    
    // Generate fun warning messages based on excess calories
    val warningMessage = when {
        excess <= 100 -> "Bạn đã vượt quá $excess kcal! Hãy đi bộ 15 phút để đốt cháy nhé! 🚶‍♀️"
        excess <= 200 -> "Vượt quá $excess kcal rồi! Bạn cần chạy bộ 20 phút hoặc nhảy dây 15 phút! 🏃‍♂️"
        excess <= 300 -> "Ồ! Vượt quá $excess kcal! Hãy tập gym 30 phút hoặc bơi lội 20 phút! 💪"
        excess <= 500 -> "Wow! Vượt quá $excess kcal! Bạn cần đạp xe 45 phút hoặc leo núi 30 phút! 🚴‍♀️"
        excess <= 700 -> "Nghiêm trọng! Vượt quá $excess kcal! Hãy chạy marathon 1 giờ hoặc tập HIIT 40 phút! 🏃‍♀️"
        else -> "Báo động đỏ! Vượt quá $excess kcal! Bạn cần tập luyện cường độ cao 1.5 giờ! 🔥💪"
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Warning icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF5252)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚠️",
                    fontSize = 24.sp
                )
            }
            
            // Warning message
            Text(
                text = warningMessage,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFC62828),
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun EmptyDailyMenuSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Chưa có thực đơn cho ngày này",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 32.dp)
        )
    }
}
