package com.team.eatcleanapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import com.team.eatcleanapp.R


private object ProfileTokens {
    val BgLight     = Color(0xFF225416).copy(0.2f)// nền
    val CardGreen   = Color(0xFF046E1F).copy(0.5f) // thẻ
    val TitleGreen  = Color(0xFF1F6E43)// tiêu đề

    val TitleBlack   = Color(color = 0xFF000000) // màu đen cho tên
    val ValueRed    = Color(0xFFC11F1F) // màu value


    val Radius      = 16.dp
    val HPad        = 16.dp
    val VPad        = 20.dp
    val InnerPad    = 16.dp
    val Gap         = 12.dp
    val CardHeight  = 152.dp
}

// top bar
@Composable
private fun ProfileTopBar(
    userName: String,
    modifier: Modifier = Modifier
) {
    val t = ProfileTokens
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // text avatar
        Text(
            text = userName,
            color = t.TitleBlack,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        // Avatar
        Image(
            painterResource(R.drawable.avatar), contentDescription = null
            , contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(40.dp)
        )

    }
}


@Composable
private fun MetricCard(
    title: String,
    valueText: String,
    dateText: String,
    modifier: Modifier = Modifier
) {
    val t = ProfileTokens
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(t.Radius),
        color = t.CardGreen,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(t.InnerPad),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = t.TitleBlack,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = t.TitleBlack
                    )
                    Spacer(Modifier.size(6.dp))
                    Text(
                        text = dateText,
                        color = t.TitleBlack,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = valueText,
                color = t.ValueRed,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun BodyMetricsScreen(
    userName: String = "Nguyễn Hồng Đông",
    dateText: String = "14 tháng 8 · 11:00",
) {
    val t = ProfileTokens

    Surface(
        color = t.BgLight,
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(t.BgLight)
                .padding(
                    top = 75.dp,
                    bottom = 82.dp,
                    start = t.HPad,
                    end = t.HPad
                ),
        ) {
            // 1. Top Bar (đã chỉnh sửa)
            ProfileTopBar(
                userName = userName,
            )

           Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Chỉ số cơ thể (BMI)",
                color = t.TitleBlack.copy(alpha = 0.85f),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(start = 5.dp, bottom = 8.dp)
            )


            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(t.Gap, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MetricCard(
                    title = "BMI", valueText = "18.5", dateText = dateText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(t.CardHeight)
                )
                MetricCard(
                    title = "Chiều cao", valueText = "180cm", dateText = dateText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(t.CardHeight)
                )
                MetricCard(
                    title = "Cân nặng", valueText = "60kg", dateText = dateText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(t.CardHeight)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    MaterialTheme {
        BodyMetricsScreen()
    }
}