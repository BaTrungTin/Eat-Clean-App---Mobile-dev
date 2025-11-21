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
import androidx.compose.ui.draw.clip
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
import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.ui.navigation.Screen
import com.team.eatcleanapp.ui.theme.JungleGreen
import kotlin.math.roundToInt

@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val favoriteMeals by viewModel.favoriteMeals.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFavoriteMeals()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
             // Menu Icon (Placeholder)
             Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground), // Replace with your menu icon resource
                contentDescription = "Menu",
                tint = JungleGreen,
                modifier = Modifier.size(32.dp).clickable { /* Open Drawer */ }
            )

            Spacer(modifier = Modifier.weight(1f))
            
            // Avatar (Placeholder)
             Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground), // Replace with your avatar resource
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.LightGray)
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = JungleGreen)
            }
        } else {
            if (favoriteMeals.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Bạn chưa có món ăn yêu thích nào", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(favoriteMeals) { meal ->
                        FavoriteMealItem(
                            meal = meal, 
                            onClick = {
                                navController.navigate(Screen.MealDetail.createRoute(meal.id))
                            },
                            onFavoriteClick = {
                                viewModel.removeFavorite(meal.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteMealItem(
    meal: Meal, 
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
                        .data(meal.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = meal.name,
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
                    text = meal.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = JungleGreen,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                 Text(
                    text = "${meal.totalCalories.roundToInt()} kcal",
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
