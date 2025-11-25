package com.team.eatcleanapp.ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.team.eatcleanapp.domain.model.enums.Goal
import com.team.eatcleanapp.domain.model.user.User
import com.team.eatcleanapp.ui.navigation.Screen
import com.team.eatcleanapp.ui.screens.profile.UserViewModel
import com.team.eatcleanapp.util.Result

@Composable
fun GoalSelectionScreen(
    navController: NavController,
    userId: String? = null,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var selectedGoal by remember { mutableStateOf<Goal?>(null) }
    val userProfileState by userViewModel.userProfile.collectAsState()
    val updateProfileState by userViewModel.updateProfileState.collectAsState()
    
    // Get current user to pre-select goal
    val currentUser = (userProfileState as? Result.Success<User>)?.data
    
    LaunchedEffect(currentUser) {
        if (currentUser != null && selectedGoal == null) {
            selectedGoal = currentUser.goal
        }
    }
    
    // Load user profile if userId is provided
    LaunchedEffect(userId) {
        if (userId != null && userId.isNotBlank() && userId != "default_user_id") {
            userViewModel.getUserProfile(userId)
        }
    }
    
    // Handle update result
    LaunchedEffect(updateProfileState) {
        val state = updateProfileState // Assign to local variable for smart cast
        when (state) {
            is Result.Success<*> -> {
                Toast.makeText(context, "Cập nhật mục tiêu thành công", Toast.LENGTH_SHORT).show()
                // Refresh user profile to ensure latest data
                if (userId != null && userId.isNotBlank() && userId != "default_user_id") {
                    userViewModel.getUserProfile(userId)
                }
                userViewModel.resetUpdateState()
                kotlinx.coroutines.delay(300) // Wait a bit for profile to refresh
                navController.popBackStack()
            }
            is Result.Error -> {
                Toast.makeText(
                    context,
                    "Lỗi khi cập nhật mục tiêu: ${state.message}",
                    Toast.LENGTH_SHORT
                ).show()
                userViewModel.resetUpdateState()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Column {
            Text(
                text = "Mục Tiêu",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF057432),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Mục tiêu của bạn là gì?",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        // Goal Options
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GoalOption(
                goal = Goal.LOSE_WEIGHT,
                isSelected = selectedGoal == Goal.LOSE_WEIGHT,
                onSelected = { selectedGoal = Goal.LOSE_WEIGHT }
            )

            GoalOption(
                goal = Goal.MAINTAIN_WEIGHT,
                isSelected = selectedGoal == Goal.MAINTAIN_WEIGHT,
                onSelected = { selectedGoal = Goal.MAINTAIN_WEIGHT }
            )

            GoalOption(
                goal = Goal.GAIN_WEIGHT,
                isSelected = selectedGoal == Goal.GAIN_WEIGHT,
                onSelected = { selectedGoal = Goal.GAIN_WEIGHT }
            )
        }

        // Continue Button
        Button(
            onClick = {
                if (selectedGoal != null) {
                    val targetUserId = userId ?: currentUser?.id
                    if (targetUserId != null && currentUser != null) {
                        // Update user goal
                        val updatedUser = currentUser.copy(goal = selectedGoal!!)
                        userViewModel.updateUserProfile(updatedUser)
                    } else {
                        // If no user, just navigate (for onboarding flow)
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route + "/goal") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = selectedGoal != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF057432),
                disabledContainerColor = Color(0xFFA5D6A7)
            )
        ) {
            Text(
                text = "Hoàn thành",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun GoalOption(
    goal: Goal,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFF057432) else Color(0xFFE0E0E0)
    val backgroundColor = if (isSelected) Color(0xFFE8F5E8) else Color.White

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onSelected,
                role = Role.RadioButton
            ),
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor,
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        ),
        shadowElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Goal Title
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Radio button indicator
                RadioButton(
                    selected = isSelected,
                    onClick = null, // null because we handle click in parent
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF057432)
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = getGoalDisplayName(goal),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF057432)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Additional info for specific goals
            if (goal == Goal.LOSE_WEIGHT || goal == Goal.GAIN_WEIGHT) {
                Text(
                    text = getGoalAdditionalInfo(goal),
                    fontSize = 12.sp,
                    color = Color(0xFF757575),
                    modifier = Modifier.padding(start = 40.dp)
                )
            } else {
                Text(
                    text = "Tối ưu cho sức khỏe của bạn",
                    fontSize = 12.sp,
                    color = Color(0xFF757575),
                    modifier = Modifier.padding(start = 40.dp)
                )
            }
        }
    }
}

// Helper functions
private fun getGoalDisplayName(goal: Goal): String {
    return when (goal) {
        Goal.LOSE_WEIGHT -> "Giảm cân"
        Goal.MAINTAIN_WEIGHT -> "Duy trì cân nặng"
        Goal.GAIN_WEIGHT -> "Tăng cân"
    }
}

private fun getGoalAdditionalInfo(goal: Goal): String {
    return when (goal) {
        Goal.LOSE_WEIGHT -> "Mức giảm cân hợp lý: 0.5 - 1kg/tuần"
        Goal.GAIN_WEIGHT -> "Mức tăng cân hợp lý: 0.25 - 0.5kg/tuần"
        Goal.MAINTAIN_WEIGHT -> "Tối ưu cho sức khỏe của bạn"
    }
}

@Preview(showBackground = true)
@Composable
fun GoalSelectionScreenPreview() {
    val navController = rememberNavController()

    MaterialTheme {
        Surface {
            GoalSelectionScreen(navController = navController)
        }
    }
}