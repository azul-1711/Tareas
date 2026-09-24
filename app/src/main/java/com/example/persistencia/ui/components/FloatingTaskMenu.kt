package com.example.persistencia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.persistencia.ui.theme.AppColors
import com.example.persistencia.ui.theme.AppShapes

@Composable
fun FloatingTaskMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onViewTask: () -> Unit,
    onEditTask: () -> Unit,
    onDeleteTask: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        shape = AppShapes.MenuShape,
        modifier = Modifier.padding(4.dp),
        // focusable = false: es un menú de solo botones, no un campo de
        // texto. Sin esto, cada apertura pide foco y arma el manejo de
        // back-press del sistema, que es lo que se sentía como lentitud.
        properties = PopupProperties(focusable = false)
    ) {
        MenuAction("Consultar", Icons.Filled.Visibility, AppColors.AccentSoft, AppColors.AccentStrong, onClick = onViewTask)
        MenuAction("Actualizar", Icons.Filled.Edit, AppColors.AccentSoft, AppColors.AccentStrong, onClick = onEditTask)
        MenuAction("Eliminar", Icons.Filled.Delete, AppColors.DangerSoft, AppColors.Danger, textColor = AppColors.Danger, onClick = onDeleteTask)
    }
}

@Composable
private fun MenuAction(
    label: String,
    icon: ImageVector,
    chipBackground: Color,
    iconTint: Color,
    textColor: Color = AppColors.Ink,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = { Text(label, color = textColor) },
        leadingIcon = {
            Box(
                modifier = Modifier.size(30.dp).clip(CircleShape).background(chipBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
            }
        },
        onClick = onClick
    )
}