package com.team.eatcleanapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.team.eatcleanapp.ui.theme.PearlAqua
import com.team.eatcleanapp.ui.theme.White

@Composable
fun Login(
    onNextClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPdVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGrayGreen)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_circle),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-60).dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            Text(
                "Chào mừng trở lại!",
                style = MaterialTheme.typography.titleLarge,
                color = JungleGreen
            )

            Spacer(modifier = Modifier.height(46.dp))

            Image(
                painter = painterResource(R.drawable.img),
                contentDescription = "Image",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(180.dp)
            )

            Spacer(modifier = Modifier.height(46.dp))

            //email
            OutlinedTextField(
                value = email,
                onValueChange = { newEmail -> email = newEmail },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 10.dp, 10.dp, 15.dp),

                placeholder = {
                    Text(
                        "Email",
                        color = Black.copy(alpha = 0.5f)
                    )
                },

                singleLine = true,

                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = ForestGreen,
                    fontWeight = FontWeight.SemiBold
                ),

                shape = RoundedCornerShape(16.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = FernGreen.copy(alpha = 0.2f),
                    unfocusedContainerColor = White,
                    focusedBorderColor = JungleGreen,
                    unfocusedBorderColor = White
                )
            )

            //password
            OutlinedTextField(
                value = password,
                onValueChange = { newPassword -> password = newPassword },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 10.dp, 10.dp, 15.dp),

                placeholder = {
                    Text(
                        "Mật khẩu ",
                        color = Black.copy(alpha = 0.5f)
                    )
                },

                trailingIcon = {
                    val icon =
                        if (isPdVisible)
                            painterResource(R.drawable.visible)
                        else
                            painterResource(R.drawable.visible_off)

                    IconButton(
                        onClick = { isPdVisible = !isPdVisible }
                    ) {
                        Icon(
                            painter = icon,
                            contentDescription =
                                if (isPdVisible)
                                    "Hide password"
                                else
                                    "Show password",
                            tint = ForestGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },

                visualTransformation =
                    if (isPdVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),

                singleLine = true,

                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = ForestGreen,
                    fontWeight = FontWeight.SemiBold
                ),

                shape = RoundedCornerShape(16.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = FernGreen.copy(alpha = 0.2f),
                    unfocusedContainerColor = White,
                    focusedBorderColor = JungleGreen,
                    unfocusedBorderColor = White
                )
            )

            Text(
                text = "Quên mật khẩu",
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 20.dp, top = 10.dp)
                    .clickable { onForgotPasswordClick() },
                color = ForestGreen.copy(alpha = 0.5f),
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
            )

            Spacer(modifier = Modifier.height(66.dp))

            Button(
                onClick = { onNextClick(email, password) },
                modifier = Modifier
                    .size(310.dp, 75.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PearlAqua),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Đăng Nhập",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 30.sp),
                    color = White
                )
            }

            Spacer(modifier = Modifier.height(33.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Chưa có tài khoản?",
                    color = Black.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    "Đăng Ký",
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .clickable { onRegisterClick() },
                    color = ForestGreen.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 24.sp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview()
{
    EatCleanAppMobiledevTheme {
        Login(
            onNextClick = { _, _ -> },
            onRegisterClick = {},
            onForgotPasswordClick = {}
        )
    }
}