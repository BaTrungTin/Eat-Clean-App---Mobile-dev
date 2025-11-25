package com.team.eatcleanapp.ui.screens.favorite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.team.eatcleanapp.R
import com.team.eatcleanapp.domain.model.meal.Favorite
import com.team.eatcleanapp.ui.navigation.Screen
import com.team.eatcleanapp.ui.theme.JungleGreen
import kotlin.math.roundToInt
import com.team.eatcleanapp.util.Result
import com.team.eatcleanapp.ui.components.CommonTopBar

@Composable
fun FavoriteScreen(
    navController: NavController,
    userId: String,
    viewModel: FavoriteViewModel = hiltViewModel(),
    onMenuClick: () -> Unit = {}
) {
    val favoritesState by viewModel.favorites.collectAsState()

    LaunchedEffect(userId) {
        viewModel.getFavorites(userId)
    }
    
    LaunchedEffect(viewModel.toggleFavoriteState.collectAsState().value) {
        when (val state = viewModel.toggleFavoriteState.value) {
            is Result.Success -> {
                if (state.data == false) {
                    viewModel.getFavorites(userId)
                }
                viewModel.resetToggleState()
            }
            is Result.Error -> {
                viewModel.resetToggleState()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CommonTopBar(onMenuClick = onMenuClick)
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                "Món ăn yêu thích",
                style = MaterialTheme.typography.titleLarge,
                color = JungleGreen,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when (favoritesState) {
            is Result.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = JungleGreen)
                }
            }
            is Result.Success<*> -> {
                val favorites = when (val state = favoritesState) {
                    is Result.Success<List<Favorite>> -> state.data
                    else -> emptyList()
                }
                if (favorites.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "No favorites",
                                tint = Color.LightGray,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Bạn chưa có món ăn yêu thích nào",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(favorites) { favorite ->
                            FavoriteMealItem(
                                favorite = favorite,
                                onClick = {
                                    navController.navigate(Screen.MealDetail.createRoute(favorite.mealId))
                                },
                                onFavoriteClick = {
                                    viewModel.toggleFavorite(userId, favorite.mealId)
                                }
                            )
                        }
                    }
                }
            }
            is Result.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Có lỗi xảy ra khi tải dữ liệu",
                        color = Color.Red
                    )
                }
            }
            else -> {}
            }
        }
    }
}

@Composable
fun FavoriteMealItem(
    favorite: Favorite,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, JungleGreen),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .height(100.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize().padding(8.dp)
        ) {
            // Hình ảnh món ăn bên trái
            Card(
                 shape = RoundedCornerShape(12.dp),
                 modifier = Modifier.size(84.dp) 
            ) {
                 AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(favorite.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = favorite.mealName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(R.drawable.ic_launcher_background),
                    error = painterResource(R.drawable.ic_launcher_background)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Thông tin món ăn bên phải
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = favorite.mealName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = JungleGreen,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                 Text(
                    text = "${favorite.calories.roundToInt()} kcal",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Icon yêu thích bên phải cùng
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Remove from favorite",
                    tint = JungleGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
             Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
