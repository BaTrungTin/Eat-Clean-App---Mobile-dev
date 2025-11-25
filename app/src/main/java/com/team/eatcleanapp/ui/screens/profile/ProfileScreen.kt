package com.team.eatcleanapp.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.team.eatcleanapp.R
import com.team.eatcleanapp.domain.model.enums.Gender
import com.team.eatcleanapp.domain.model.enums.Goal
import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.ui.screens.auth.AuthViewModel
import com.team.eatcleanapp.ui.theme.JungleGreen
import com.team.eatcleanapp.util.Result
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: String,
    onUpdateHealthClick: () -> Unit,
    onGoalClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    userViewModel: UserViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userProfileState by userViewModel.userProfile.collectAsState()
    val updateState by userViewModel.updateProfileState.collectAsState()
    val logoutState by authViewModel.logoutState.collectAsState()

    LaunchedEffect(userId) {
        android.util.Log.d("ProfileScreen", "Loading profile for userId: $userId")
        if (userId.isNotBlank() && userId != "default_user_id") {
            // First try to get user profile
            userViewModel.getUserProfile(userId)
            
            // If user doesn't exist, try to create from Firebase Auth
            // This will be handled by checking the error state and creating user if needed
        } else {
            android.util.Log.e("ProfileScreen", "Invalid userId: $userId")
        }
    }
    
    // Auto-create user from Firebase Auth if user doesn't exist in database
    LaunchedEffect(userProfileState) {
        val state = userProfileState
        if (state is Result.Error && state.message?.contains("Không tìm thấy user") == true) {
            android.util.Log.d("ProfileScreen", "User not found, trying to get from Firebase Auth...")
            // Try to get current user from Firebase and create if needed
            userViewModel.getCurrentUser()
        }
    }
    
    // Handle current user state (when auto-creating user)
    val currentUserState by userViewModel.currentUser.collectAsState()
    LaunchedEffect(currentUserState) {
        when (val state = currentUserState) {
            is Result.Success -> {
                val user = state.data
                if (user != null && user.id == userId) {
                    android.util.Log.d("ProfileScreen", "User created/loaded from Firebase, refreshing profile...")
                    // User was created/loaded, now get the full profile
                    userViewModel.getUserProfile(userId)
                }
            }
            else -> Unit
        }
    }

    LaunchedEffect(updateState) {
        when (updateState) {
            is Result.Success<*> -> {
                // Refresh profile after update
                userViewModel.getUserProfile(userId)
                userViewModel.resetUpdateState()
            }
            else -> Unit
        }
    }

    LaunchedEffect(logoutState) {
        val state = logoutState // Assign to local variable for smart cast
        when (state) {
            is Result.Success<*> -> {
                authViewModel.resetLogoutState()
                onLogoutClick()
            }
            is Result.Error -> {
                Toast.makeText(
                    context,
                    "Lỗi khi đăng xuất: ${state.message}",
                    Toast.LENGTH_SHORT
                ).show()
                authViewModel.resetLogoutState()
            }
            else -> Unit
        }
    }

    val user = (userProfileState as? Result.Success<User>)?.data
    val isLoading = userProfileState is Result.Loading || userProfileState is Result.Idle
    val hasError = userProfileState is Result.Error
    
    // Log state changes
    LaunchedEffect(userProfileState) {
        when (val state = userProfileState) {
            is Result.Success -> {
                android.util.Log.d("ProfileScreen", "User loaded successfully: ${state.data.name}")
            }
            is Result.Error -> {
                android.util.Log.e("ProfileScreen", "Error loading user: ${state.message}")
            }
            is Result.Loading -> {
                android.util.Log.d("ProfileScreen", "Loading user profile...")
            }
            else -> Unit
        }
    }

    val backgroundColor = Color(0xFF225416).copy(alpha = 0.2f)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hồ sơ") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(backgroundColor)
                .fillMaxSize()
        ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (hasError) {
            val errorState = userProfileState as? Result.Error
            val errorMessage = errorState?.message ?: "Không thể tải thông tin người dùng"
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Không thể tải thông tin người dùng",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        android.util.Log.d("ProfileScreen", "Retrying to load profile for userId: $userId")
                        userViewModel.getUserProfile(userId)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = JungleGreen
                    )
                ) {
                    Text("Thử lại")
                }
            }
        } else if (user != null) {
            SimpleProfileContent(
                user = user,
                userId = userId,
                onUpdateHealthClick = onUpdateHealthClick,
                onGoalClick = onGoalClick,
                onLogoutClick = onLogoutClick,
                authViewModel = authViewModel
            )
        }
        }
    }
}

@Composable
private fun ProfileTopBar(
    userName: String,
    userEmail: String,
    avatarUrl: String?
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (nameRef, avatarRef, emailRef, bmiTitleRef) = createRefs()

        Text(
            text = userName,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            ),
            color = Color(0xFF000000),
            modifier = Modifier.constrainAs(nameRef) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 20.dp)
            }
        )

        // Avatar
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(JungleGreen)
                .constrainAs(avatarRef) {
                    top.linkTo(parent.top, margin = 12.dp)
                    start.linkTo(nameRef.end, margin = 16.dp)
                },
            contentAlignment = Alignment.Center
        ) {
            if (avatarUrl != null && avatarUrl.isNotEmpty()) {
                // TODO: Load image from URL using Coil or Glide
                Image(
                    painter = painterResource(R.drawable.avatar),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = userName.take(1).uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = userEmail,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF666666),
            modifier = Modifier.constrainAs(emailRef) {
                top.linkTo(nameRef.bottom, margin = 4.dp)
                start.linkTo(nameRef.start)
            }
        )

        Text(
            text = "Chỉ số cơ thể (BMI)",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color(0xFF000000),
            modifier = Modifier.constrainAs(bmiTitleRef) {
                top.linkTo(nameRef.bottom, margin = 20.dp)
                start.linkTo(nameRef.start)
            }
        )
    }
}

@Composable
private fun ProfileMetricsSection(user: User) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // BMI
        if (user.healthMetrics != null) {
            HealthMetricCard(
                name = "BMI",
                value = user.healthMetrics.bmi.toDouble(),
                unit = null,
                dateTime = formatDate(user.healthMetrics.lastUpdated)
            )
        }

        // BMR
        if (user.healthMetrics != null) {
            HealthMetricCard(
                name = "BMR",
                value = user.healthMetrics.bmr.toDouble(),
                unit = "kcal/ngày",
                dateTime = formatDate(user.healthMetrics.lastUpdated)
            )
        }

        // TDEE
        if (user.healthMetrics != null) {
            HealthMetricCard(
                name = "TDEE",
                value = user.healthMetrics.tdee.toDouble(),
                unit = "kcal/ngày",
                dateTime = formatDate(user.healthMetrics.lastUpdated)
            )
        }

        // Chiều cao
        if (user.height > 0) {
            HealthMetricCard(
                name = "Chiều cao",
                value = user.height,
                unit = "cm",
                dateTime = formatDate(user.updatedAt)
            )
        }

        // Cân nặng
        if (user.weight > 0) {
            HealthMetricCard(
                name = "Cân nặng",
                value = user.weight,
                unit = "kg",
                dateTime = formatDate(user.updatedAt)
            )
        }

        // Tuổi
        if (user.age > 0) {
            HealthMetricCard(
                name = "Tuổi",
                value = user.age.toDouble(),
                unit = "tuổi",
                dateTime = formatDate(user.updatedAt)
            )
        }

        // Giới tính
        HealthMetricCard(
            name = "Giới tính",
            value = if (user.gender == Gender.MALE) 1.0 else 2.0,
            unit = null,
            displayValue = if (user.gender == Gender.MALE) "Nam" else "Nữ",
            dateTime = formatDate(user.updatedAt)
        )

        // Mục tiêu
        HealthMetricCard(
            name = "Mục tiêu",
            value = 0.0,
            unit = null,
            displayValue = when (user.goal) {
                Goal.LOSE_WEIGHT -> "Giảm cân"
                Goal.MAINTAIN_WEIGHT -> "Duy trì cân nặng"
                Goal.GAIN_WEIGHT -> "Tăng cân"
            },
            dateTime = formatDate(user.updatedAt)
        )
    }
}

@Composable
private fun HealthMetricCard(
    name: String,
    value: Double,
    unit: String?,
    dateTime: String,
    displayValue: String? = null
) {
    val green = Color(0xFF046E1F).copy(alpha = 0.5f)
    val black = Color(0xFF000000)
    val red = Color(0xFFC11F1F)

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = green, shape = RoundedCornerShape(15.dp))
            .height(152.dp)
    ) {
        val (textNameRef, iconRef, textDateTimeRef, textValueRef) = createRefs()

        Text(
            text = name,
            color = black,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier = Modifier.constrainAs(textNameRef) {
                top.linkTo(parent.top, margin = 36.dp)
                start.linkTo(parent.start, margin = 20.dp)
            }
        )

        Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            tint = black,
            modifier = Modifier
                .size(20.dp)
                .constrainAs(iconRef) {
                    top.linkTo(parent.top, margin = 45.dp)
                    start.linkTo(textNameRef.end, margin = 15.dp)
                }
        )

        Text(
            text = dateTime,
            color = black,
            fontSize = 20.sp,
            modifier = Modifier.constrainAs(textDateTimeRef) {
                top.linkTo(iconRef.top)
                start.linkTo(iconRef.end, margin = 5.dp)
            }
        )

        Text(
            text = displayValue ?: "${value.toInt()}${unit ?: ""}",
            color = red,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier = Modifier.constrainAs(textValueRef) {
                top.linkTo(textNameRef.bottom, margin = 15.dp)
                start.linkTo(textNameRef.start)
            }
        )
    }
}

@Composable
private fun ActionButtonsSection(
    onUpdateHealthClick: () -> Unit,
    onLogoutClick: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val logoutState by authViewModel.logoutState.collectAsState()
    val isLoggingOut = logoutState is Result.Loading

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onUpdateHealthClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = JungleGreen
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Cập nhật hồ sơ sức khỏe",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        OutlinedButton(
            onClick = {
                authViewModel.logout()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoggingOut,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFC11F1F)
            ),
            border = BorderStroke(
                width = 1.dp,
                color = Color(0xFFC11F1F)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoggingOut) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Color(0xFFC11F1F)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = if (isLoggingOut) "Đang đăng xuất..." else "Đăng xuất",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SimpleProfileContent(
    user: User,
    userId: String,
    onUpdateHealthClick: () -> Unit,
    onGoalClick: () -> Unit,
    onLogoutClick: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val logoutState by authViewModel.logoutState.collectAsState()
    val isLoggingOut = logoutState is Result.Loading
    val lightGreen = Color(0xFFE8F5E8)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGreen),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        // Avatar
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(JungleGreen)
                .border(
                    width = 3.dp,
                    color = Color(0xFF046E1F),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (user.avatarUrl != null && user.avatarUrl.isNotEmpty()) {
                Image(
                    painter = painterResource(R.drawable.avatar),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = user.name.take(1).uppercase(),
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // User name
        Text(
            text = user.name.ifEmpty { "Người dùng" },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000000)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // User ID
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "id:",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666)
            )
            Text(
                text = userId.take(8),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Action buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hồ sơ sức khỏe button
            OutlinedButton(
                onClick = onUpdateHealthClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = JungleGreen
                ),
                border = BorderStroke(
                    width = 2.dp,
                    color = JungleGreen
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Hồ sơ sức khỏe",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = JungleGreen
                )
            }

            // Mục tiêu button
            OutlinedButton(
                onClick = onGoalClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = JungleGreen
                ),
                border = BorderStroke(
                    width = 2.dp,
                    color = JungleGreen
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Mục tiêu",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = JungleGreen
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Đăng xuất button
            OutlinedButton(
                onClick = {
                    authViewModel.logout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoggingOut,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFC11F1F)
                ),
                border = BorderStroke(
                    width = 2.dp,
                    color = Color(0xFFC11F1F)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoggingOut) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFFC11F1F)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = if (isLoggingOut) "Đang đăng xuất..." else "Đăng xuất",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC11F1F)
                )
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd 'tháng' MM - HH:mm", Locale("vi", "VN"))
    return format.format(date)
}

