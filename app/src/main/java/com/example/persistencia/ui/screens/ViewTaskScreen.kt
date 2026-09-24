package com.example.persistencia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.persistencia.data.Task
import com.example.persistencia.ui.theme.AppColors
import com.example.persistencia.ui.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTaskScreen(
    task: Task,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = AppColors.Paper,
        topBar = {
            TopAppBar(
                title = { Text("Consultar Tarea", color = AppColors.Ink, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = AppColors.Ink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.Paper)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Text(
                text = DateUtils.formatDateTime(task.fechaCreacion),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.AccentStrong
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = task.titulo,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.Ink
            )
            Spacer(modifier = Modifier.height(14.dp))

            val badgeBg = if (task.estadoCompletado) AppColors.DangerSoft else AppColors.AccentSoft
            val badgeColor = if (task.estadoCompletado) AppColors.Danger else AppColors.AccentStrong
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(badgeBg)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (task.estadoCompletado) "Completada" else "Pendiente",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = badgeColor
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "DESCRIPCIÓN",
                style = MaterialTheme.typography.labelSmall,
                color = AppColors.InkFaint
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (task.descripcion.isNotBlank()) task.descripcion else "Sin descripción.",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.InkSoft,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.15
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}