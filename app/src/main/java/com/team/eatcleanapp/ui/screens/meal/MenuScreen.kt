package com.team.eatcleanapp.ui.screens.meal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.team.eatcleanapp.R
import com.team.eatcleanapp.domain.model.meal.Meal
import com.team.eatcleanapp.ui.theme.JungleGreen
import kotlin.math.roundToInt
import com.team.eatcleanapp.util.Result
import com.team.eatcleanapp.ui.screens.meal.MealViewModel
import androidx.compose.ui.platform.LocalContext
import com.team.eatcleanapp.ui.components.CommonTopBar

@Composable
fun MenuScreen(
    navController: NavController,
    mealViewModel: MealViewModel = hiltViewModel(),
    onMealClick: (String) -> Unit,
    onMenuClick: () -> Unit = {}
) {
    val mealsState by mealViewModel.meals.collectAsState()
    val syncState by mealViewModel.syncState.collectAsState()
    val searchQuery by mealViewModel.searchQuery.collectAsState()
    val context = LocalContext.current

    // Load meals on first launch
    LaunchedEffect(Unit) {
        mealViewModel.getMeals()
    }

    // Auto sync if database is empty
    LaunchedEffect(mealsState) {
        when (val state = mealsState) {
            is Result.Success<List<Meal>> -> {
                if (state.data.isEmpty() && syncState !is Result.Loading && syncState !is Result.Success<*>) {
                    // Database is empty, try to sync from remote
                    android.util.Log.d("MenuScreen", "Database is empty, auto-syncing from API...")
                    mealViewModel.syncMealsFromRemote()
                }
            }
            else -> Unit
        }
    }

    // Handle sync result
    LaunchedEffect(syncState) {
        when (val state = syncState) {
            is Result.Success<*> -> {
                android.util.Log.d("MenuScreen", "Sync successful, reloading meals...")
                android.widget.Toast.makeText(
                    context,
                    "Đồng bộ dữ liệu thành công!",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                mealViewModel.resetSyncState()
                // Đợi một chút để đảm bảo database đã được cập nhật
                kotlinx.coroutines.delay(500)
                mealViewModel.getMeals() // Reload meals after sync
            }
            is Result.Error -> {
                android.util.Log.e("MenuScreen", "Sync error: ${state.message}")
                android.widget.Toast.makeText(
                    context,
                    state.message ?: "Lỗi khi đồng bộ dữ liệu",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                mealViewModel.resetSyncState()
            }
            else -> Unit
        }
    }


    // Xử lý các trạng thái loading, error, success
    val meals = when (val state = mealsState) {
        is Result.Success<List<Meal>> -> state.data
        else -> emptyList()
    }

    val isLoading = mealsState is Result.Loading || syncState is Result.Loading
    val isError = mealsState is Result.Error
    val errorMessage = when (val state = mealsState) {
        is Result.Error -> state.message ?: "Có lỗi xảy ra khi tải dữ liệu"
        else -> null
    }
    val isSyncing = syncState is Result.Loading

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CommonTopBar(onMenuClick = onMenuClick)
        MenuScreenContent(
            meals = meals,
            isLoading = isLoading,
            isError = isError,
            errorMessage = errorMessage,
            isSyncing = isSyncing,
            onSearch = { query: String -> 
                mealViewModel.setSearchQuery(query)
            },
            onMealClick = onMealClick,
            onRetry = { mealViewModel.getMeals() },
            onSync = { mealViewModel.syncMealsFromRemote() }
        )
    }
}

@Composable
fun MenuScreenContent(
    meals: List<Meal>,
    isLoading: Boolean,
    isError: Boolean,
    errorMessage: String?,
    isSyncing: Boolean,
    onSearch: (String) -> Unit,
    onMealClick: (String) -> Unit,
    onRetry: () -> Unit,
    onSync: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearch(it)
            },
            placeholder = { Text("Nhập tên món bạn cần tìm", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = JungleGreen)
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F8E9), RoundedCornerShape(12.dp)), // Màu nền xanh nhạt
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                focusedContainerColor = Color(0xFFF1F8E9),
                unfocusedContainerColor = Color(0xFFF1F8E9)
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Grid List
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = JungleGreen)
                        if (isSyncing) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Đang đồng bộ dữ liệu từ server...",
                                color = JungleGreen,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
            isError -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = errorMessage ?: "Có lỗi xảy ra",
                            color = Color.Red,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = onRetry,
                                    colors = ButtonDefaults.buttonColors(containerColor = JungleGreen)
                                ) {
                                    Text("Thử lại", color = Color.White)
                                }
                                OutlinedButton(
                                    onClick = onSync,
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = JungleGreen)
                                ) {
                                    Text("Đồng bộ dữ liệu từ server")
                                }
                            }
                        }
                    }
                }
            }
            meals.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Không tìm thấy món ăn nào",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = onSync,
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = JungleGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Đồng bộ dữ liệu từ server")
                            }
                        }
                    }
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(meals) { meal ->
                        MealItem(meal = meal, onClick = { onMealClick(meal.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun MealItem(meal: Meal, onClick: () -> Unit) {
    var isFavorite by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(bottom = 8.dp)
            .width(160.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, JungleGreen),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(meal.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = meal.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp) // Padding bên trong viền
                        .clip(RoundedCornerShape(12.dp)),
                    placeholder = painterResource(R.drawable.ic_launcher_background),
                    error = painterResource(R.drawable.ic_launcher_background)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = meal.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = JungleGreen, // Chữ màu xanh
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
             Text(
                text = "${meal.totalCalories.roundToInt()} kcal",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Favorite",
                tint = JungleGreen,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { isFavorite = !isFavorite }
            )
        }
    }
}

