package com.team.eatcleanapp.ui.screens.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.team.eatcleanapp.R
import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.ui.navigation.Screen
import com.team.eatcleanapp.ui.theme.FernGreen
import com.team.eatcleanapp.ui.theme.JungleGreen
import com.team.eatcleanapp.ui.theme.LightGreen
import kotlin.math.roundToInt

// 1. Màn hình chính (Có ViewModel & NavController) - Dùng khi chạy App
@Composable
fun MenuScreen(
    navController: NavController,
    viewModel: MenuViewModel = hiltViewModel(),
    onMealClick: (String) -> Unit // Thêm callback để điều hướng ra ngoài NavHost con
) {
    val meals by viewModel.meals.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    MenuScreenContent(
        meals = meals,
        isLoading = isLoading,
        onSearch = { query -> viewModel.searchMeals(query) },
        onMealClick = onMealClick
    )
}

// 2. Nội dung giao diện (Không có ViewModel) - Dùng để Preview
@Composable
fun MenuScreenContent(
    meals: List<Meal>,
    isLoading: Boolean,
    onSearch: (String) -> Unit,
    onMealClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Menu Icon (Placeholder)
            Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Menu",
                tint = JungleGreen,
                modifier = Modifier.size(32.dp).clickable { /* Open Drawer */ }
            )
            
            Spacer(modifier = Modifier.weight(1f))

            // Avatar (Placeholder)
            Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.LightGray)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

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
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = JungleGreen)
            }
        } else {
            if (meals.isEmpty()) {
                 Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Không tìm thấy món ăn nào", color = Color.Gray)
                }
            } else {
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
    // Trạng thái yêu thích giả định (vì màn hình Menu chưa có ViewModel xử lý favorite riêng)
    // Thực tế cần ViewModel để quản lý state này cho từng item, hoặc chỉ để UI tĩnh như design
    var isFavorite by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.Start, // Căn lề trái
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(bottom = 8.dp)
            .width(160.dp) // Giới hạn chiều rộng item
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, JungleGreen), // Viền xanh
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

// 3. Preview - Sử dụng dữ liệu giả VÀ thêm Mock Navigation Bar
@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    val sampleMeals = listOf(
        Meal(id = "1", name = "Salad Ức Gà", image = null),
        Meal(id = "2", name = "Sinh Tố Cải Xoăn", image = null),
        Meal(id = "3", name = "Bún Bò Huế", image = null),
        Meal(id = "4", name = "Cơm Gạo Lứt", image = null)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = LightGreen,
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                // Item 1: Home
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { 
                        Icon(
                            painter = painterResource(R.drawable.home), 
                            contentDescription = null, 
                            modifier = Modifier.size(30.dp)
                        ) 
                    },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = FernGreen)
                )
                // Item 2: Menu (Selected - Mô phỏng giống Figma)
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(JungleGreen)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.menu), 
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )
                // Item 3: Favorite
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { 
                        Icon(
                            painter = painterResource(R.drawable.heart), 
                            contentDescription = null, 
                            modifier = Modifier.size(30.dp)
                        ) 
                    },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = FernGreen)
                )
                // Item 4: Profile
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { 
                        Icon(
                            painter = painterResource(R.drawable.boy), 
                            contentDescription = null, 
                            modifier = Modifier.size(30.dp)
                        ) 
                    },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = FernGreen)
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            MenuScreenContent(
                meals = sampleMeals,
                isLoading = false,
                onSearch = {},
                onMealClick = {}
            )
        }
    }
}
