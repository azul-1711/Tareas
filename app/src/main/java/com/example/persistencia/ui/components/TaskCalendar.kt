package com.example.persistencia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.persistencia.ui.theme.AppColors
import com.example.persistencia.ui.utils.DateUtils

@Composable
fun TaskCalendar(
    year: Int,
    month: Int,
    selectedDay: Int,
    daysWithTasks: Set<Int>,
    onDaySelected: (Int) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val offset = remember(year, month) { DateUtils.firstDayOffset(year, month) }
    val daysInMonth = remember(year, month) { DateUtils.daysInMonth(year, month) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundNavButton(icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft, onClick = onPrevMonth)
            Text(
                text = "${DateUtils.monthsEs[month]} $year",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.Ink
            )
            RoundNavButton(icon = Icons.AutoMirrored.Filled.KeyboardArrowRight, onClick = onNextMonth)
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            DateUtils.weekdaysShortEs.forEach { label ->
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = AppColors.InkFaint
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        val totalCells = offset + daysInMonth
        val rows = (totalCells + 6) / 7

        for (row in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val cellIndex = row * 7 + col
                    val day = cellIndex - offset + 1
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (day in 1..daysInMonth) {
                            CalendarDay(
                                day = day,
                                selected = day == selectedDay,
                                hasTask = daysWithTasks.contains(day),
                                onClick = { onDaySelected(day) }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun RoundNavButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(AppColors.AccentSoft)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = AppColors.AccentStrong)
    }
}

@Composable
private fun CalendarDay(
    day: Int,
    selected: Boolean,
    hasTask: Boolean,
    onClick: () -> Unit
) {
    val background = if (selected) AppColors.AccentStrong else AppColors.AccentSoft
    val textColor = if (selected) Color.White else AppColors.Ink
    val size = if (selected) 38.dp else 34.dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(background)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.toString(),
                color = textColor,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 3.dp)
                .size(4.dp)
                .clip(CircleShape)
                .background(if (hasTask && !selected) AppColors.Accent else Color.Transparent)
        )
    }
}