package com.team.eatcleanapp.ui.screens.splash

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.eatcleanapp.R

private val BgLight   = Color(0xFFE7F5EC)
private val GreenDark = Color(0xFF3C9E6B)


@Composable
fun Corner(
    color: Color,
    diameter: Dp = 180.dp,
    corner: Alignment,
    pushOut: Float = 0.35f,
    modifier: Modifier = Modifier
) {
    val px = with(LocalDensity.current) { diameter.toPx() }
    Canvas(modifier) {
        val r  = px / 2f
        val dx = when (corner) {
            Alignment.TopEnd, Alignment.CenterEnd, Alignment.BottomEnd -> size.width + r * pushOut
            else -> -r * pushOut
        }
        val dy = when (corner) {
            Alignment.BottomStart, Alignment.BottomCenter, Alignment.BottomEnd -> size.height + r * pushOut
            else -> -r * pushOut
        }
        drawCircle(color = color, radius = r, center = Offset(dx, dy))
    }
}

@Composable
fun SplashScreen(onStart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        //Ép kiểu bo góc trên dưới
        Corner(
            corner = Alignment.TopEnd,
            color = Color(0xFFB9F2C6).copy(alpha = 0.80f),
            diameter = 220.dp,
            pushOut = 0.08f,
            modifier = Modifier.matchParentSize()
        )
        Corner(
            corner = Alignment.BottomStart,
            color = Color(0xFF6AC28A).copy(alpha = 0.55f),
            diameter = 280.dp,
            pushOut = 0.05f,
            modifier = Modifier.matchParentSize()
        )

        // --- Nội dung ---
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(R.drawable.splash_illustration),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(333f / 320f),
                    contentScale = ContentScale.Fit
                )
            }

            // Text + Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "bắt đầu hành trình của\nbạn ngay hôm nay !",
                    color = GreenDark,
                    fontSize = 30.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(60.dp))

                Button(
                    onClick = onStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA3C2A4),
                        contentColor = Color.White
                    )
                ) {
                    Text("Bắt đầu", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(70.dp))
            }
        }
    }
}


