package com.team.eatcleanapp.ui.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.team.eatcleanapp.ui.theme.JungleGreen
import com.team.eatcleanapp.ui.theme.LightGrayGreen
import com.team.eatcleanapp.ui.theme.LightGreen
import com.team.eatcleanapp.ui.theme.White

@Composable
fun TDEECalculator(
    BMR: Double?,
    gender: String?,
    value: String,
    onValueChange: (String) -> Unit,
    value_2: String,
    onValueChange_2: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGreen)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                            .padding(5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "BMT",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                            color = Black.copy(alpha = 0.5f)
                        )
                    }

                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$BMR",
                            style = MaterialTheme.typography.titleLarge.copy(color = JungleGreen)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .padding(5.dp)
                    .background(White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = if (gender == "male")
                                painterResource(R.drawable.mars)
                            else if (gender == "female")
                                painterResource(R.drawable.venus)
                            else painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "gender",
                    tint = Black.copy(alpha = 0.6f),
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            "Mức độ vận động của bạn",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = Black.copy(alpha = 0.6f),
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputWithUnit_2(
                "phút",
                "phút/ngày",
                value = value,
                onValueChange = onValueChange
            )

            Spacer(modifier = Modifier.weight(1f))

            InputWithUnit_2(
                "ngày",
                "ngày/tuần",
                value = value_2,
                onValueChange = onValueChange_2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TDEECalculatorPreview()
{
    EatCleanAppMobiledevTheme {
        TDEECalculator(
            BMR = 2000.0,
            gender = "male",
            value = "30",
            onValueChange = {},
            value_2 = "5",
            onValueChange_2 = {}
        )
    }
}