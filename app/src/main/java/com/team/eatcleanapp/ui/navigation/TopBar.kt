package com.team.eatcleanapp.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.eatcleanapp.R
import com.team.eatcleanapp.util.DateUtils
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBarWithDatePicker(
    selectedDate: Date,
    onDateSelected: (Date) -> Unit,
    onMenuClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.time
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        val newDate = Date(millis)
                        onDateSelected(newDate)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // UI Top Bar giống thiết kế
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ICON MENU
        Icon(
            painter = painterResource(id = R.drawable.sidebar),
            contentDescription = "Menu",
            tint = Color(0xFF0E8A49),
            modifier = Modifier
                .size(28.dp)
                .clickable { onMenuClick() }
        )

        // CLICKABLE DATE PICKER
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { showDatePicker = true }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = "Calendar",
                tint = Color(0xFF7ABA7A),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))

            // 👉 Format ngày bằng DateUtils
            Text(
                text = DateUtils.formatDateDDTHGMM(selectedDate),   // "10 thg 11"
                color = Color(0xFF7ABA7A),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // AVATAR
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFF0E8A49), CircleShape)
                .clickable { onAvatarClick() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.heart),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun SimpleTopBar(
    title: String,
    onMenuClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ICON MENU
        Icon(
            painter = painterResource(id = R.drawable.sidebar),
            contentDescription = "Menu",
            tint = Color(0xFF0E8A49),
            modifier = Modifier
                .size(28.dp)
                .clickable { onMenuClick() }
        )

        // TIÊU ĐỀ TRUNG TÂM
        Text(
            text = title,
            color = Color(0xFF0E8A49),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        // AVATAR
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFF0E8A49), CircleShape)
                .clickable { onAvatarClick() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.heart),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
        }
    }
}

