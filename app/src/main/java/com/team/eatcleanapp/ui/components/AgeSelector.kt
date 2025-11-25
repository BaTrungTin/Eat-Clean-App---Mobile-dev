package com.team.eatcleanapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.eatcleanapp.ui.theme.Black
import com.team.eatcleanapp.ui.theme.EatCleanAppMobiledevTheme
import com.team.eatcleanapp.ui.theme.JungleGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AgeSelector(
    modifier: Modifier = Modifier,
    currentAge: Int,
    onAgeSelected: (Int) -> Unit
) {
    val ages = listOf(null, null) + (1..150).toList() + listOf(null, null)
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(currentAge)
    {
        val targetIndex = currentAge + 1
        listState.scrollToItem(targetIndex - 2)
    }

    LaunchedEffect(listState)
    {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                val centerIndex = index + 2
                val ageValue = ages.getOrNull(centerIndex)

                if (ageValue != null && ageValue != currentAge)
                    onAgeSelected(ageValue)
            }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            itemsIndexed(ages) { index, age ->
                val centerIndex = listState.firstVisibleItemIndex + 2
                val isSelected = index == centerIndex

                Text(
                    text = age?.toString() ?: "",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = if (isSelected) JungleGreen else Black.copy(alpha = 0.5f),
                        fontSize = if (isSelected) 18.sp else 16.sp,
                    ),
                    modifier = Modifier
                        .width(35.dp)
                        .scale(if (isSelected) 1.3f else 1.2f)
                        .clickable {
                            if (age != null)
                                coroutineScope.launch {
                                    listState.animateScrollToItem((index - 2)
                                        .coerceAtLeast(0))
                                }
                        }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgeSelectorPreview()
{
    var selectedAge by remember { mutableStateOf(20) }

    EatCleanAppMobiledevTheme {
        AgeSelector(
            modifier = Modifier,
            currentAge = selectedAge,
            onAgeSelected = { selectedAge = it }
        )
    }
}