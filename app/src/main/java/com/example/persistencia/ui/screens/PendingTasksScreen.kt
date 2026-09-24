package com.example.persistencia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.persistencia.data.Task
import com.example.persistencia.ui.components.EmptyTasksView
import com.example.persistencia.ui.components.TaskCard
import com.example.persistencia.ui.components.TaskSearchBar
import com.example.persistencia.ui.theme.AppColors

@Composable
fun PendingTasksScreen(
    tasks: List<Task>,
    onToggleTask: (Task) -> Unit,
    onViewTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    val pendingTasks = remember(tasks, query) {
        tasks.filter { !it.estadoCompletado }
            .filter { query.isBlank() || it.titulo.contains(query, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Pendientes",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            color = AppColors.Ink,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
        )

        TaskSearchBar(query = query, onQueryChange = { query = it })

        Box(modifier = Modifier.weight(1f)) {
            if (pendingTasks.isEmpty()) {
                EmptyTasksView(if (query.isBlank()) "No tienes tareas pendientes." else "Sin resultados para \"$query\".")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(pendingTasks, key = { it.id }) { task ->
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