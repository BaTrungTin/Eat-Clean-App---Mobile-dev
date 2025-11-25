package com.team.eatcleanapp.ui.screens.meal

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.rotate
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.team.eatcleanapp.R
import com.team.eatcleanapp.domain.model.meal.Meal
import com.team.eatcleanapp.ui.theme.JungleGreen
import kotlin.math.roundToInt
import com.team.eatcleanapp.util.Result
import com.team.eatcleanapp.ui.screens.meal.MealViewModel
import com.team.eatcleanapp.ui.screens.favorite.FavoriteViewModel
import com.team.eatcleanapp.ui.screens.dailymenu.MenuViewModel
import com.team.eatcleanapp.domain.model.enums.MealCategory
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.ui.text.style.TextAlign
import com.team.eatcleanapp.data.mapper.IngredientCaloriesLookup

@Composable
fun DetailScreen(
    mealId: String,
    userId: String,
    onBackClick: () -> Unit,
    viewModel: MealViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    menuViewModel: MenuViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val mealDetailState by viewModel.mealDetail.collectAsState()
    val isFavoriteState by favoriteViewModel.isFavoriteState.collectAsState()
    
    // State for date picker in AddMealDialog
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateForAdd by remember { mutableStateOf(Date()) }

    LaunchedEffect(mealId) {
        android.util.Log.d("DetailScreen", "Loading meal detail for ID: $mealId")
        viewModel.getMealDetail(mealId)
        favoriteViewModel.checkIsFavorite(userId, mealId)
    }
    
    // Log state changes
    LaunchedEffect(mealDetailState) {
        when (val state = mealDetailState) {
            is Result.Success<Meal?> -> {
                android.util.Log.d("DetailScreen", "Meal loaded: ${state.data?.name ?: "null"}")
            }
            is Result.Error -> {
                android.util.Log.e("DetailScreen", "Error loading meal: ${state.message}")
            }
            else -> {}
        }
    }

    // Observe toggle favorite state and refresh favorite status
    val toggleFavoriteState by favoriteViewModel.toggleFavoriteState.collectAsState()
    
    LaunchedEffect(toggleFavoriteState) {
        when (val state = toggleFavoriteState) {
            is Result.Success<Boolean> -> {
                val isNowFavorite = state.data
                // Toggle successful, refresh favorite status
                favoriteViewModel.checkIsFavorite(userId, mealId)
                favoriteViewModel.resetToggleState()
                // Show toast message
                android.widget.Toast.makeText(
                    context,
                    if (isNowFavorite) "Đã thêm vào yêu thích" else "Đã xóa khỏi yêu thích",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
            is Result.Error -> {
                android.util.Log.e("DetailScreen", "Error toggling favorite: ${state.message}")
                favoriteViewModel.resetToggleState()
                android.widget.Toast.makeText(
                    context,
                    "Lỗi: ${state.message}",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        when (val state = mealDetailState) {
            is Result.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = JungleGreen
                )
            }
            is Result.Success<Meal?> -> {
                val meal = state.data
                val isFavorite = when (val favState = isFavoriteState) {
                    is Result.Success<Boolean> -> favState.data
                    else -> false
                }

                meal?.let { currentMeal ->
                    android.util.Log.d("DetailScreen", "Meal data: name=${currentMeal.name}, ingredients=${currentMeal.ingredients.size}, instructions=${currentMeal.instructions.size}")
                    android.util.Log.d("DetailScreen", "Ingredients: ${currentMeal.ingredients.map { "${it.name} (${it.quantity} ${it.unit})" }}")
                    android.util.Log.d("DetailScreen", "Instructions: ${currentMeal.instructions.take(3).joinToString(" | ")}")
                    MealContent(
                        meal = currentMeal,
                        isFavorite = isFavorite,
                        onBackClick = onBackClick,
                        onFavoriteClick = {
                            favoriteViewModel.toggleFavorite(userId, mealId)
                        },
                        onAddToMenuClick = {
                            // Will be handled by AddMealDialog
                        },
                        mealId = mealId,
                        userId = userId,
                        menuViewModel = menuViewModel,
                        showDatePicker = showDatePicker,
                        selectedDateForAdd = selectedDateForAdd,
                        onShowDatePickerChange = { showDatePicker = it },
                        onSelectedDateForAddChange = { selectedDateForAdd = it }
                    )
                } ?: run {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Không tìm thấy món ăn",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "Meal ID: $mealId",
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            is Result.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Có lỗi xảy ra khi tải chi tiết món ăn",
                        color = Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = (state as Result.Error).message ?: "Unknown error",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
            else -> {
                // Idle state - có thể chưa load
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = JungleGreen
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealContent(
    meal: Meal,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onAddToMenuClick: () -> Unit,
    mealId: String,
    userId: String,
    menuViewModel: MenuViewModel,
    showDatePicker: Boolean,
    selectedDateForAdd: Date,
    onShowDatePickerChange: (Boolean) -> Unit,
    onSelectedDateForAddChange: (Date) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    
    // Reset selected date when opening dialog
    LaunchedEffect(showAddDialog) {
        if (showAddDialog) {
            onSelectedDateForAddChange(Date())
        }
    }

    val addMealState by menuViewModel.addMealState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(addMealState) {
        when (val state = addMealState) {
            is Result.Success<*> -> {
                android.widget.Toast.makeText(
                    context,
                    "Đã thêm món vào thực đơn!",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                menuViewModel.resetAddMealState()
                showAddDialog = false
            }

            is Result.Error -> {
                android.widget.Toast.makeText(
                    context,
                    state.message ?: "Lỗi khi thêm món vào thực đơn",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                menuViewModel.resetAddMealState()
            }

            else -> {}
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            item {
                HeaderSection(
                    name = meal.name,
                    onBackClick = onBackClick,
                    onAddClick = { showAddDialog = true }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    imageVector = Icons.Default.AutoFixHigh, // Icon cây bút phép thuật (Magic Wand)
                    contentDescription = "Edit",
                    tint = JungleGreen,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Image
            item {
                Spacer(modifier = Modifier.height(12.dp))
                MealImage(meal.image)
            }

            // Calories & Favorite
            item {
                Spacer(modifier = Modifier.height(16.dp))
                NutritionSection(meal, isFavorite, onFavoriteClick)
            }

            // Ingredients
            item {
                Spacer(modifier = Modifier.height(24.dp))
                IngredientsSection(meal)
            }

            // Instructions
            item {
                Spacer(modifier = Modifier.height(24.dp))
                InstructionsSection(meal.instructions)
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        // Add Meal Dialog (outside LazyColumn)
        // DatePickerDialog needs to be shown separately from AlertDialog
        AddMealDialog(
            mealName = meal.name,
            onDismiss = { showAddDialog = false },
            onConfirm = { date: Date, mealType: MealCategory, portionSize: Double ->
                menuViewModel.addMealToDailyMenu(userId, date, mealId, mealType, portionSize)
            },
            isLoading = addMealState is Result.Loading,
            showDialog = showAddDialog,
            selectedDate = selectedDateForAdd,
            onDateChange = { date -> onSelectedDateForAddChange(date) },
            onShowDatePicker = { onShowDatePickerChange(true) }
        )
        
        // DatePickerDialog at DetailScreen level to avoid being blocked by AlertDialog
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDateForAdd.time
            )
            val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
            
            DatePickerDialog(
                onDismissRequest = { onShowDatePickerChange(false) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                onSelectedDateForAddChange(Date(millis))
                                android.util.Log.d("DetailScreen", "Date selected: ${dateFormat.format(selectedDateForAdd)}")
                            }
                            onShowDatePickerChange(false)
                        }
                    ) {
                        Text("Chọn")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onShowDatePickerChange(false) }) {
                        Text("Hủy")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
fun HeaderSection(
    name: String,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .background(JungleGreen, shape = RoundedCornerShape(8.dp))
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Text(
            text = name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = JungleGreen,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        IconButton(
            onClick = onAddClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = JungleGreen,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun MealImage(imageUrl: String?) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, JungleGreen), // Viền xanh
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp) // Tăng chiều cao lên chút
            .padding(4.dp)
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .listener(
                        onError = { _, result ->
                            Log.e("MealImage", "Lỗi load ảnh: ${result.throwable.message}")
                        }
                    )
                    .build(),
                contentDescription = "Meal Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                error = painterResource(R.drawable.ic_launcher_background)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "Default Meal Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun NutritionSection(
    meal: Meal,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🔥~ ", fontSize = 18.sp, color = Color(0xFFFF9800))
            Text(
                text = "${meal.totalCalories.roundToInt()} kcal",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }

        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = "Favorite",
            tint = if (isFavorite) JungleGreen else Color.Gray,
            modifier = Modifier
                .size(28.dp)
                .clickable { onFavoriteClick() }
        )
    }
}

@Composable
fun IngredientsSection(meal: Meal) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Nguyên liệu",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.width(16.dp))
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (meal.ingredients.isEmpty()) {
            Text(
                text = "Không có thông tin nguyên liệu",
                fontSize = 14.sp,
                color = Color.Gray,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        } else {
            android.util.Log.d(
                "DetailScreen",
                "Displaying ${meal.ingredients.size} ingredients"
            )
            meal.ingredients.forEach { ingredient ->
                // Tính calories cho ingredient này
                val quantityInGrams = IngredientCaloriesLookup.convertToGrams(
                    ingredient.quantity,
                    ingredient.unit
                )
                val ingredientCalories = (ingredient.caloriesPer100 / 100.0) * quantityInGrams

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = ingredient.name,
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                        if (ingredientCalories > 0) {
                            Text(
                                text = "${ingredientCalories.roundToInt()} kcal",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    Text(
                        text = "${ingredient.quantity.roundToInt()} ${ingredient.unit}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = JungleGreen
                    )
                }
            }
        }
    }
}

@Composable
fun InstructionsSection(instructions: List<String>) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Hướng dẫn",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.width(16.dp))
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (instructions.isEmpty()) {
            Text(
                text = "Không có thông tin hướng dẫn",
                fontSize = 14.sp,
                color = Color.Gray,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        } else {
            android.util.Log.d("DetailScreen", "Displaying ${instructions.size} instructions")
            instructions.forEach { step ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = step,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealDialog(
    mealName: String,
    onDismiss: () -> Unit,
    onConfirm: (Date, MealCategory, Double) -> Unit,
    isLoading: Boolean,
    showDialog: Boolean,
    selectedDate: Date,
    onDateChange: (Date) -> Unit,
    onShowDatePicker: () -> Unit
) {
    var selectedMealType by remember { mutableStateOf(MealCategory.LUNCH) }
    var portionSize by remember { mutableStateOf("1.0") }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { if (!isLoading) onDismiss() },
            title = {
            Text(
                text = "Thêm vào thực đơn",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Meal name
                Text(
                    text = mealName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = JungleGreen,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Date picker
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isLoading) { 
                            android.util.Log.d("AddMealDialog", "Date field clicked")
                            onShowDatePicker() 
                        }
                ) {
                    OutlinedTextField(
                        value = dateFormat.format(selectedDate),
                        onValueChange = {},
                        label = { Text("Ngày") },
                        readOnly = true,
                        enabled = false, // Disable to prevent keyboard
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Select date",
                                modifier = Modifier.rotate(180f)
                            )
                        }
                    )
                }

                // Meal type selector
                Text(
                    text = "Bữa ăn",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MealCategory.values().forEach { category ->
                        FilterChip(
                            selected = selectedMealType == category,
                            onClick = { if (!isLoading) selectedMealType = category },
                            label = { Text(category.displayName) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = JungleGreen,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Portion size
                OutlinedTextField(
                    value = portionSize,
                    onValueChange = { if (!isLoading) portionSize = it },
                    label = { Text("Khẩu phần") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    suffix = { Text("phần", color = Color.Gray) }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val portion = portionSize.toDoubleOrNull() ?: 1.0
                    onConfirm(selectedDate, selectedMealType, portion)
                },
                enabled = !isLoading && portionSize.toDoubleOrNull() != null && portionSize.toDoubleOrNull()!! > 0,
                colors = ButtonDefaults.buttonColors(containerColor = JungleGreen)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Thêm")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Hủy")
            }
        }
        )
    }
}
