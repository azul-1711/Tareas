package com.example.persistencia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.persistencia.data.Task
import com.example.persistencia.ui.components.EmptyTasksView
import com.example.persistencia.ui.components.TaskCalendar
import com.example.persistencia.ui.components.TaskCard
import com.example.persistencia.ui.theme.AppColors
import com.example.persistencia.ui.utils.DateUtils
import com.example.persistencia.ui.utils.TaskDateFilter

@Composable
fun TaskSearchScreen(
    tasks: List<Task>,
    year: Int,
    month: Int,
    selectedDay: Int,
    onYearMonthChange: (Int, Int) -> Unit,
    onDaySelected: (Int) -> Unit,
    onToggleTask: (Task) -> Unit,
    onViewTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit
) {
    val daysWithTasks = remember(tasks, year, month) {
        TaskDateFilter.daysWithTasksInMonth(tasks, year, month)
    }
    val dayTasks = remember(tasks, year, month, selectedDay) {
        TaskDateFilter.tasksForDay(tasks, year, month, selectedDay)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Buscar por fecha",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            color = AppColors.Ink,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
        )

        TaskCalendar(
            year = year,
            month = month,
            selectedDay = selectedDay,
            daysWithTasks = daysWithTasks,
            onDaySelected = onDaySelected,
            onPrevMonth = {
                if (month == 0) onYearMonthChange(year - 1, 11) else onYearMonthChange(year, month - 1)
            },
            onNextMonth = {
                if (month == 11) onYearMonthChange(year + 1, 0) else onYearMonthChange(year, month + 1)
            },
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        HorizontalDivider(color = AppColors.Line, modifier = Modifier.padding(top = 16.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
            Text("SELECCIONADO", style = MaterialTheme.typography.labelSmall, color = AppColors.InkSoft)
            Text(
                text = selectedDay.toString(),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Medium,
                color = AppColors.Ink
            )
            Text(
                text = "${DateUtils.weekdayName(year, month, selectedDay)} · ${dayTasks.size} tareas",
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.InkSoft
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            if (dayTasks.isEmpty()) {
                EmptyTasksView("Sin tareas ese día.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(dayTasks, key = { it.id }) { task ->
                        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)) {
                            TaskCard(
                                task = task,
                                onToggle = { onToggleTask(task) },
                                onView = { onViewTask(task) },
                                onEdit = { onEditTask(task) },
                                onDelete = { onDeleteTask(task) }
                            )
                        }
                    }
                }
            }
        }
    }
}