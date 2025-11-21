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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.team.eatcleanapp.R
import com.team.eatcleanapp.domain.model.Meal
import com.team.eatcleanapp.ui.theme.JungleGreen
import kotlin.math.roundToInt

@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val meal by viewModel.meal.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            meal?.let { currentMeal ->
                MealContent(
                    meal = currentMeal,
                    isFavorite = isFavorite,
                    onBackClick = onBackClick,
                    onFavoriteClick = { viewModel.toggleFavorite() }
                )
            } ?: run {
                Text(
                    text = "Không tìm thấy món ăn",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun MealContent(
    meal: Meal,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        item {
            HeaderSection(meal.name, onBackClick)
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
}

@Composable
fun HeaderSection(name: String, onBackClick: () -> Unit) {
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
            onClick = { /* Add to daily menu logic */ },
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

        meal.ingredients.forEach { ingredient ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = ingredient.name,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = "${ingredient.quantity.roundToInt()} ${ingredient.unit}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                     color = Color.Black
                )
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

        instructions.forEachIndexed { index, step ->
            val stepNumber = index + 1
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                 Text(
                    text = "Bước $stepNumber: $step",
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color.Black
                )
            }
        }
    }
}
