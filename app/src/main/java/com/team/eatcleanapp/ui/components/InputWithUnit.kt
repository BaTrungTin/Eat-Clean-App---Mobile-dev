package com.team.eatcleanapp.ui.components

import android.graphics.drawable.Icon
import android.widget.Space
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.eatcleanapp.R
import com.team.eatcleanapp.ui.theme.Black
import com.team.eatcleanapp.ui.theme.EatCleanAppMobiledevTheme
import com.team.eatcleanapp.ui.theme.FernGreen
import com.team.eatcleanapp.ui.theme.ForestGreen
import com.team.eatcleanapp.ui.theme.JungleGreen
import com.team.eatcleanapp.ui.theme.LightGrayGreen
import com.team.eatcleanapp.ui.theme.White

@Composable
fun InputWithUnit(
    unit1: String,
    unit2: String,
    isFirstSelected: Boolean,
    onUnitChange: (Boolean) -> Unit,
    value: String,
    onValueChange: (String) -> Unit
) {
    val offsetX by animateDpAsState( if (isFirstSelected) 0.dp else 65.dp)

    Box(
        modifier = Modifier
            .width(150.dp)
            .height(120.dp)
            .background(White, RoundedCornerShape(25.dp))
            .padding(5.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(40.dp)
                    .background(LightGrayGreen, RoundedCornerShape(60.dp))
                    .padding(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .offset(offsetX)
                        .fillMaxHeight()
                        .width(65.dp)
                        .background(White, RoundedCornerShape(60.dp))
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(modifier = Modifier.width(65.dp), contentAlignment = Alignment.Center) {
                        Text(
                            unit1,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                            color = if (isFirstSelected)
                                Black.copy(alpha = 0.5f)
                            else
                                Black.copy(alpha = 0.4f),
                            modifier = Modifier.clickable { onUnitChange(true) }
                        )
                    }

                    Box(modifier = Modifier.width(65.dp), contentAlignment = Alignment.Center) {
                        Text(
                            unit2,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                            color = if (!isFirstSelected)
                                Black.copy(alpha = 0.5f)
                            else
                                Black.copy(alpha = 0.4f),
                            modifier = Modifier.clickable { onUnitChange(false) }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    val filtered = newValue.filter { it.isDigit() || it == '.' || it == ',' }
                        .replace(',', '.')
                    onValueChange(filtered)
                },
                singleLine = true,
                maxLines = 1,
                textStyle = MaterialTheme.typography.titleLarge.copy(color = JungleGreen),
                placeholder = {
                    Text(
                        "?",
                        modifier = Modifier.fillMaxSize(),
                        color = Black.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.SemiBold),
                        textAlign = TextAlign.Center
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = White,
                    focusedBorderColor = White
                ),
                shape = RoundedCornerShape(25.dp)
            )
        }
    }
}

@Composable
fun InputWithUnit_2(
    unit: String,
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .width(155.dp)
            .height(120.dp)
            .background(White, RoundedCornerShape(25.dp))
            .padding(5.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(LightGrayGreen, RoundedCornerShape(60.dp))
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    unit,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    color = Black.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    val filtered = newValue.filter { it.isDigit() || it == '.' || it == ',' }
                        .replace(',', '.')
                    onValueChange(filtered)
                },
                singleLine = true,
                maxLines = 1,
                textStyle = MaterialTheme.typography.titleLarge.copy(color = JungleGreen),
                placeholder = {
                    Text(
                        label,
                        modifier = Modifier.fillMaxSize(),
                        color = Black.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold),
                        textAlign = TextAlign.Center
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = White,
                    focusedBorderColor = White
                ),
                shape = RoundedCornerShape(25.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InputWithUnitPreview()
{
    var isMetric by remember { mutableStateOf(true) }
    var value by remember { mutableStateOf("") }

    EatCleanAppMobiledevTheme {
        InputWithUnit_2(
            "phút",
            "phút/ngày",
            value = value,
            onValueChange = { value = it }
        )
    }
}