package com.team.eatcleanapp.ui.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationBar(
    selected: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    NavigationBar(
        containerColor = Color(0x59009253),
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .clip(RoundedCornerShape(15.dp))
            .padding(horizontal = 2.dp)
    ) {
        BottomNavItem.values().forEach { item ->
            val isSelected = item == selected

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item) },
                icon = {
                    BottomNavItemContent(
                        item = item,
                        isSelected = isSelected
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Transparent,
                    unselectedIconColor = Color.Transparent,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
private fun BottomNavItemContent(
    item: BottomNavItem,
    isSelected: Boolean
) {
    // Animation cho kích thước background
    val backgroundSize by animateDpAsState(
        targetValue = if (isSelected) 52.dp else 0.dp,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 300),
        label = "backgroundSize"
    )

    // Animation cho độ trong suốt của icon được chọn
    val selectedIconAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 300),
        label = "selectedIconAlpha"
    )

    // Animation cho độ trong suốt của icon không được chọn
    val unselectedIconAlpha by animateFloatAsState(
        targetValue = if (isSelected) 0f else 1f,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 300),
        label = "unselectedIconAlpha"
    )

    // Animation cho kích thước icon
    val iconSize by animateDpAsState(
        targetValue = if (isSelected) 28.dp else 24.dp,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 300),
        label = "iconSize"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxHeight()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(52.dp) // Kích thước cố định để giữ vị trí
        ) {
            // Background tròn với animation kích thước
            if (backgroundSize > 0.dp) {
                Box(
                    modifier = Modifier
                        .size(backgroundSize)
                        .clip(CircleShape)
                        .background(Color(0xFF057432))
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape,
                            clip = true,
                            ambientColor = Color.Black.copy(alpha = 0.3f),
                            spotColor = Color.Black.copy(alpha = 0.3f)
                        )
                )
            }

            // Icon được chọn (màu sáng) - hiện dần khi được chọn
            Icon(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.contentDescription,
                tint = Color(0xFFABDFAA),
                modifier = Modifier
                    .size(iconSize)
                    .graphicsLayer {
                        alpha = selectedIconAlpha
                    }
            )

            // Icon không được chọn (màu tối) - mờ dần khi được chọn
            Icon(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.contentDescription,
                tint = Color(0x80046E1F),
                modifier = Modifier
                    .size(iconSize)
                    .graphicsLayer {
                        alpha = unselectedIconAlpha
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    var selectedItem by remember { mutableStateOf(BottomNavItem.HOME) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomNavigationBar(
            selected = selectedItem,
            onItemSelected = { selectedItem = it }
        )
    }
}