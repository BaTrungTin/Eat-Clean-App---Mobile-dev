package com.team.eatcleanapp.ui.screens.dailymenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.team.eatcleanapp.R
import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuDay
import com.team.eatcleanapp.domain.model.dailymenu.DailyMenuItem
import com.team.eatcleanapp.domain.model.enums.MealCategory
import com.team.eatcleanapp.ui.screens.dailymenu.MenuViewModel
import com.team.eatcleanapp.ui.theme.JungleGreen
import com.team.eatcleanapp.ui.theme.LightGreen
import com.team.eatcleanapp.util.Result
import com.team.eatcleanapp.ui.components.CommonTopBar
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DailyMenuScreen(
    navController: NavController,
    userId: String = "demo-user",
    viewModel: MenuViewModel = hiltViewModel(),
    onMenuClick: () -> Unit = {}
) {
    val weeklyMenuState by viewModel.weeklyMenu.collectAsState()
    val deleteMealState by viewModel.deleteMealState.collectAsState()
    val updatePortionState by viewModel.updatePortionState.collectAsState()

    // Calculate week start date (Monday)
    val calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val weekStartDate = remember { calendar.time }

    LaunchedEffect(userId, weekStartDate) {
        viewModel.getWeeklyMenu(userId, weekStartDate)
    }

    val context = LocalContext.current

    // Refresh after delete or update
    LaunchedEffect(deleteMealState) {
        when (val state = deleteMealState) {
            is Result.Success<*> -> {
                android.util.Log.d(
                    "DailyMenuScreen",
                    "Delete successful, showing toast and refreshing..."
                )
                android.widget.Toast.makeText(
                    context,
                    "Đã xóa món ăn thành công!",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                // Don't refresh here - deleteMultipleItems already refreshes
                viewModel.resetDeleteMealState()
            }

            is Result.Error -> {
                android.util.Log.e("DailyMenuScreen", "Delete error: ${state.message}")
                android.widget.Toast.makeText(
                    context,
                    state.message ?: "Có lỗi xảy ra khi xóa món ăn",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                viewModel.resetDeleteMealState()
            }

            else -> Unit
        }
    }

    LaunchedEffect(updatePortionState) {
        when (val state = updatePortionState) {
            is Result.Success<*> -> {
                android.widget.Toast.makeText(
                    context,
                    "Đã cập nhật khẩu phần thành công!",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                viewModel.resetUpdatePortionState()
                viewModel.getWeeklyMenu(userId, weekStartDate)
            }

            is Result.Error -> {
                android.widget.Toast.makeText(
                    context,
                    state.message ?: "Có lỗi xảy ra khi cập nhật khẩu phần",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                viewModel.resetUpdatePortionState()
            }

            else -> Unit
        }
    }

    var isDeleteMode by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var selectedMeals by remember { mutableStateOf<Set<String>>(emptySet()) }
    var selectedDays by remember { mutableStateOf<Set<Int>>(emptySet()) }

    // Get weekly data from ViewModel
    val weeklyData = when (val state = weeklyMenuState) {
        is Result.Success<com.team.eatcleanapp.domain.model.dailymenu.DailyMenuWeek> -> {
            val totalMeals =
                state.data.days.sumOf { it.breakfast.size + it.lunch.size + it.dinner.size }
            android.util.Log.d(
                "DailyMenuScreen",
                "Weekly data updated: ${state.data.days.size} days, total meals: $totalMeals"
            )
            // Log details of each day
            state.data.days.forEachIndexed { index, day ->
                val dayMeals = day.breakfast.size + day.lunch.size + day.dinner.size
                if (dayMeals > 0) {
                    android.util.Log.d(
                        "DailyMenuScreen",
                        "Day $index (${day.date.time}): breakfast=${day.breakfast.size}, lunch=${day.lunch.size}, dinner=${day.dinner.size}"
                    )
                    day.breakfast.forEach {
                        android.util.Log.d(
                            "DailyMenuScreen",
                            "  Breakfast: ${it.mealName} (${it.mealId})"
                        )
                    }
                    day.lunch.forEach {
                        android.util.Log.d(
                            "DailyMenuScreen",
                            "  Lunch: ${it.mealName} (${it.mealId})"
                        )
                    }
                    day.dinner.forEach {
                        android.util.Log.d(
                            "DailyMenuScreen",
                            "  Dinner: ${it.mealName} (${it.mealId})"
                        )
                    }
                }
            }
            state.data.days
        }

        else -> {
            android.util.Log.d("DailyMenuScreen", "Weekly data not ready, state: $state")
            emptyList()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CommonTopBar(onMenuClick = onMenuClick)

        Scaffold(
    ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (weeklyMenuState) {
                    is Result.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = JungleGreen)
                        }
                    }

                    is Result.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Có lỗi xảy ra khi tải thực đơn",
                                    color = Color.Red
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = {
                                    viewModel.getWeeklyMenu(
                                        userId,
                                        weekStartDate
                                    )
                                }) {
                                    Text("Thử lại")
                                }
                            }
                        }
                    }

                    else -> {
        DailyMenuContent(
            modifier = Modifier.padding(innerPadding),
                            weeklyData = weeklyData,
            isDeleteMode = isDeleteMode,
            isEditMode = isEditMode,
            selectedMeals = selectedMeals,
            selectedDays = selectedDays,
                            userId = userId,
                            weekStartDate = weekStartDate,
                            viewModel = viewModel,
            onDeleteClick = {
                isDeleteMode = !isDeleteMode
                isEditMode = false
                if (!isDeleteMode) {
                    selectedMeals = emptySet()
                    selectedDays = emptySet()
                }
            },
            onEditClick = {
                isEditMode = !isEditMode
                isDeleteMode = false
                if (!isEditMode) {
                    selectedMeals = emptySet()
                }
            },
                            onMealCheck = { dayIndex: Int, mealType: String, mealIndex: Int, checked: Boolean ->
                val key = "${dayIndex}_${mealType}_$mealIndex"
                selectedMeals = if (checked) {
                    selectedMeals + key
                } else {
                    selectedMeals - key
                }
            },
                            onDayCheck = { dayIndex: Int, checked: Boolean ->
                selectedDays = if (checked) {
                    selectedDays + dayIndex
                } else {
                    selectedDays - dayIndex
                }
                // Select all meals for this day
                                if (checked && dayIndex < weeklyData.size) {
                                    val day = weeklyData[dayIndex]
                        val mealKeys = mutableSetOf<String>()
                        day.breakfast.forEachIndexed { index, _ ->
                            mealKeys.add("${dayIndex}_BREAKFAST_$index")
                        }
                        day.lunch.forEachIndexed { index, _ ->
                            mealKeys.add("${dayIndex}_LUNCH_$index")
                        }
                        day.dinner.forEachIndexed { index, _ ->
                            mealKeys.add("${dayIndex}_DINNER_$index")
                        }
                        selectedMeals = selectedMeals + mealKeys
                } else {
                    // Remove all meals for this day
                    selectedMeals = selectedMeals.filterNot {
                        it.startsWith("${dayIndex}_")
                    }.toSet()
                }
            },
            onDeleteSelected = {
                                if (selectedMeals.isEmpty() && selectedDays.isEmpty()) {
                                    android.widget.Toast.makeText(
                                        context,
                                        "Vui lòng chọn món ăn hoặc ngày cần xóa",
                                        android.widget.Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    // Collect all delete operations
                                    val deleteOperations = mutableListOf<suspend () -> Unit>()

                                    // Prepare delete operations for selected meals
                                    selectedMeals.forEach { key ->
                                        val parts = key.split("_")
                                        if (parts.size == 3) {
                                            val dayIndex = parts[0].toIntOrNull()
                                            val mealTypeStr = parts[1]
                                            val mealIndex = parts[2].toIntOrNull()
                                            if (dayIndex != null && mealIndex != null && dayIndex < weeklyData.size) {
                                                val day = weeklyData[dayIndex]
                                                val mealType = when (mealTypeStr) {
                                                    "BREAKFAST" -> MealCategory.BREAKFAST
                                                    "LUNCH" -> MealCategory.LUNCH
                                                    "DINNER" -> MealCategory.DINNER
                                                    else -> null
                                                }
                                                val meal = when (mealType) {
                                                    MealCategory.BREAKFAST -> day.breakfast.getOrNull(
                                                        mealIndex
                                                    )

                                                    MealCategory.LUNCH -> day.lunch.getOrNull(
                                                        mealIndex
                                                    )

                                                    MealCategory.DINNER -> day.dinner.getOrNull(
                                                        mealIndex
                                                    )

                                                    else -> null
                                                }
                                                if (meal != null && mealType != null) {
                                                    val currentMeal = meal
                                                    val currentMealType = mealType
                                                    val currentDate = day.date
                                                    android.util.Log.d(
                                                        "DailyMenuScreen",
                                                        "Preparing to delete: mealId=${currentMeal.mealId}, mealType=$currentMealType, date=${currentDate.time}"
                                                    )
                                                    deleteOperations.add {
                                                        viewModel.deleteSpecificMealDirectly(
                                                            userId,
                                                            currentDate,
                                                            currentMeal.mealId,
                                                            currentMealType
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Prepare delete operations for selected days
                                    selectedDays.forEach { dayIndex ->
                                        if (dayIndex < weeklyData.size) {
                                            val day = weeklyData[dayIndex]
                                            val dateMealTypes = mapOf(
                                                day.date to listOf(
                                                    MealCategory.BREAKFAST,
                                                    MealCategory.LUNCH,
                                                    MealCategory.DINNER
                                                )
                                            )
                                            val currentDateMealTypes = dateMealTypes
                                            deleteOperations.add {
                                                viewModel.deleteDayMenuDirectly(
                                                    userId,
                                                    currentDateMealTypes
                                                )
                                            }
                                        }
                                    }

                                    // Execute all delete operations sequentially
                                    if (deleteOperations.isNotEmpty()) {
                                        viewModel.deleteMultipleItems(
                                            userId,
                                            weekStartDate,
                                            deleteOperations
                                        )
                                    }

                                    selectedMeals = emptySet()
                                    selectedDays = emptySet()
                                    isDeleteMode = false
                                }
                            },
                            onPortionChange = { dayIndex: Int, mealType: String, mealIndex: Int, portion: Int ->
                                if (dayIndex < weeklyData.size) {
                                    val day = weeklyData[dayIndex]
                                    val mealTypeEnum = when (mealType) {
                                        "BREAKFAST" -> MealCategory.BREAKFAST
                                        "LUNCH" -> MealCategory.LUNCH
                                        "DINNER" -> MealCategory.DINNER
                                        else -> null
                                    }
                                    if (mealTypeEnum != null) {
                                        val meal = when (mealTypeEnum) {
                                            MealCategory.BREAKFAST -> day.breakfast.getOrNull(
                                                mealIndex
                                            )

                                            MealCategory.LUNCH -> day.lunch.getOrNull(mealIndex)
                                            MealCategory.DINNER -> day.dinner.getOrNull(mealIndex)
                                        }
                                        if (meal != null) {
                                            viewModel.updatePortionSize(
                                                userId,
                                                day.date,
                                                meal.mealId,
                                                mealTypeEnum,
                                                portion.toDouble()
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DailyMenuContent(
    modifier: Modifier = Modifier,
    weeklyData: List<DailyMenuDay>,
    isDeleteMode: Boolean,
    isEditMode: Boolean,
    selectedMeals: Set<String>,
    selectedDays: Set<Int>,
    userId: String,
    weekStartDate: Date,
    viewModel: MenuViewModel,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    onMealCheck: (Int, String, Int, Boolean) -> Unit,
    onDayCheck: (Int, Boolean) -> Unit,
    onDeleteSelected: () -> Unit,
    onPortionChange: (Int, String, Int, Int) -> Unit
) {
    val context = LocalContext.current
    var showPortionDialog by remember { mutableStateOf(false) }
    var selectedMealForPortion by remember { mutableStateOf<Triple<Int, String, Int>?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        // Header với nút xóa và chỉnh sửa
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .clickable { onDeleteClick() }
                        .background(
                            if (isDeleteMode) Color.Red else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                "Xóa",
                        color = if (isDeleteMode) Color.White else Color.Red,
                fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                modifier = Modifier
                        .clickable { onEditClick() }
                        .background(
                            if (isEditMode) JungleGreen else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
            Text(
                "Chỉnh sửa",
                        color = if (isEditMode) Color.White else JungleGreen,
                fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
            )
                }
        }

        // Nút xóa khi có item được chọn
        if ((selectedMeals.isNotEmpty() || selectedDays.isNotEmpty()) && isDeleteMode) {
            Button(
                onClick = onDeleteSelected,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Xóa món đã chọn")
            }
        }

        // Lịch ăn uống cả tuần
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
                items(
                    items = weeklyData,
                    key = { day -> "${day.date.time}_${day.breakfast.size}_${day.lunch.size}_${day.dinner.size}" }
                ) { day ->
                val dayIndex = weeklyData.indexOf(day)
                DayMealSection(
                    dayMeal = day,
                    dayIndex = dayIndex,
                    isDeleteMode = isDeleteMode,
                    isEditMode = isEditMode,
                    selectedMeals = selectedMeals,
                    selectedDays = selectedDays,
                    onMealCheck = onMealCheck,
                    onDayCheck = onDayCheck,
                        onMealClick = { mealType: String, mealIndex: Int ->
                        if (isEditMode) {
                            selectedMealForPortion = Triple(dayIndex, mealType, mealIndex)
                            showPortionDialog = true
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Dialog chỉnh sửa phần ăn
    if (showPortionDialog && selectedMealForPortion != null) {
        val (dayIndex, mealType, mealIndex) = selectedMealForPortion!!
        val meal = when (mealType) {
            "BREAKFAST" -> weeklyData[dayIndex].breakfast[mealIndex]
            "LUNCH" -> weeklyData[dayIndex].lunch[mealIndex]
            "DINNER" -> weeklyData[dayIndex].dinner[mealIndex]
            else -> null
        }

        if (meal != null) {
            PortionDialog(
                currentPortion = meal.portionSize.toInt(),
                onDismiss = { showPortionDialog = false },
                onConfirm = { portion: Int ->
                    onPortionChange(dayIndex, mealType, mealIndex, portion)
                    showPortionDialog = false
                }
            )
        }
    }
}

@Composable
fun DayMealSection(
    dayMeal: DailyMenuDay,
    dayIndex: Int,
    isDeleteMode: Boolean,
    isEditMode: Boolean,
    selectedMeals: Set<String>,
    selectedDays: Set<Int>,
    onMealCheck: (Int, String, Int, Boolean) -> Unit,
    onDayCheck: (Int, Boolean) -> Unit,
    onMealClick: (String, Int) -> Unit
) {
    val dayNames =
        listOf("Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy", "Chủ Nhật")
    val dayName = dayNames.getOrElse(dayIndex) { "Ngày ${dayIndex + 1}" }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, JungleGreen, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = LightGreen)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Hàng 1: Thứ + Checkbox + Tổng kcal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    dayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = JungleGreen
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${dayMeal.totalCalories.toInt()} kcal",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    if (isDeleteMode) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Checkbox(
                            checked = selectedDays.contains(dayIndex),
                            onCheckedChange = { checked -> onDayCheck(dayIndex, checked) },
                            colors = CheckboxDefaults.colors(checkedColor = JungleGreen)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Các bữa ăn
            MealTimeSection(
                mealName = "Sáng",
                meals = dayMeal.breakfast,
                dayIndex = dayIndex,
                mealType = "BREAKFAST",
                isDeleteMode = isDeleteMode,
                isEditMode = isEditMode,
                selectedMeals = selectedMeals,
                onMealCheck = onMealCheck,
                onMealClick = onMealClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            MealTimeSection(
                mealName = "Trưa",
                meals = dayMeal.lunch,
                dayIndex = dayIndex,
                mealType = "LUNCH",
                isDeleteMode = isDeleteMode,
                isEditMode = isEditMode,
                selectedMeals = selectedMeals,
                onMealCheck = onMealCheck,
                onMealClick = onMealClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            MealTimeSection(
                mealName = "Tối",
                meals = dayMeal.dinner,
                dayIndex = dayIndex,
                mealType = "DINNER",
                isDeleteMode = isDeleteMode,
                isEditMode = isEditMode,
                selectedMeals = selectedMeals,
                onMealCheck = onMealCheck,
                onMealClick = onMealClick
            )
        }
    }
}

@Composable
fun MealTimeSection(
    mealName: String,
    meals: List<DailyMenuItem>,
    dayIndex: Int,
    mealType: String,
    isDeleteMode: Boolean,
    isEditMode: Boolean,
    selectedMeals: Set<String>,
    onMealCheck: (Int, String, Int, Boolean) -> Unit,
    onMealClick: (String, Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Tên bữa ăn
        Text(
            mealName,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = JungleGreen,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Danh sách món ăn
        if (meals.isEmpty()) {
            Text(
                "Chưa có món ăn",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            meals.forEachIndexed { mealIndex, meal ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = isEditMode) {
                                if (isEditMode) {
                            onMealClick(mealType, mealIndex)
                                }
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            meal.mealName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            "${meal.totalCalories.toInt()} kcal (${meal.portionSize.toInt()} phần)",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    if (isDeleteMode || isEditMode) {
                        Checkbox(
                            checked = selectedMeals.contains("${dayIndex}_${mealType}_$mealIndex"),
                            onCheckedChange = { checked ->
                                onMealCheck(dayIndex, mealType, mealIndex, checked)
                            },
                            colors = CheckboxDefaults.colors(checkedColor = JungleGreen)
                        )
                    }
                    }
                }
            }
        }
    }


@Composable
fun PortionDialog(
    currentPortion: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var portion by remember { mutableStateOf(currentPortion.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Chỉnh sửa phần ăn",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = portion,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                            portion = newValue
                        }
                    },
                    label = { Text("Số phần ăn") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val portionValue = portion.toIntOrNull() ?: 1
                            if (portionValue in 1..10) {
                                onConfirm(portionValue)
                            }
                        }
                    ) {
                        Text("Lưu")
                    }
                    }
                }
            }
        }
    }

fun generateSampleWeeklyData(): List<DailyMenuDay> {
    val date = Date()
    return listOf(
        DailyMenuDay(
            date = date,
            breakfast = listOf(
                DailyMenuItem(
                    mealId = "1",
                    userId = "user1",
                    date = date,
                    mealName = "Bánh mì trứng",
                    calories = 300.0,
                    mealType = MealCategory.BREAKFAST,
                    portionSize = 1.0
                )
            ),
            lunch = listOf(
                DailyMenuItem(
                    mealId = "2",
                    userId = "user1",
                    date = date,
                    mealName = "Cơm gà xối mỡ",
                    calories = 450.0,
                    mealType = MealCategory.LUNCH,
                    portionSize = 1.0
                ),
                DailyMenuItem(
                    mealId = "3",
                    userId = "user1",
                    date = date,
                    mealName = "Canh rau",
                    calories = 80.0,
                    mealType = MealCategory.LUNCH,
                    portionSize = 1.0
                )
            ),
            dinner = listOf(
                DailyMenuItem(
                    mealId = "4",
                    userId = "user1",
                    date = date,
                    mealName = "Salad cá hồi",
                    calories = 350.0,
                    mealType = MealCategory.DINNER,
                    portionSize = 1.0
                )
            )
        ),
        DailyMenuDay(
            date = Date(date.time + 24 * 60 * 60 * 1000), // Ngày tiếp theo
            breakfast = listOf(
                DailyMenuItem(
                    mealId = "5",
                    userId = "user1",
                    date = date,
                    mealName = "Cháo yến mạch",
                    calories = 250.0,
                    mealType = MealCategory.BREAKFAST,
                    portionSize = 1.0
                )
            ),
            lunch = listOf(
                DailyMenuItem(
                    mealId = "6",
                    userId = "user1",
                    date = date,
                    mealName = "Bún chả",
                    calories = 400.0,
                    mealType = MealCategory.LUNCH,
                    portionSize = 1.0
                )
            ),
            dinner = listOf(
                DailyMenuItem(
                    mealId = "7",
                    userId = "user1",
                    date = date,
                    mealName = "Súp gà",
                    calories = 280.0,
                    mealType = MealCategory.DINNER,
                    portionSize = 1.0
                )
            )
        )
        // Thêm các ngày khác...
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DailyMenuScreenPreview() {
    val sampleData = generateSampleWeeklyData()
    // Note: Preview không thể dùng hiltViewModel, nên tạo mock hoặc bỏ qua
    // DailyMenuContent requires userId and viewModel, so we skip preview for now
    // or create a preview-specific version without these dependencies
}