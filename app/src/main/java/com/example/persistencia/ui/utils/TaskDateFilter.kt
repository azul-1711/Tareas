package com.example.persistencia.ui.utils

import com.example.persistencia.data.Task
import java.util.Calendar

object TaskDateFilter {
    fun tasksForDay(tasks: List<Task>, year: Int, month: Int, day: Int): List<Task> {
        val cal = Calendar.getInstance()
        return tasks.filter { task ->
            cal.timeInMillis = task.fechaCreacion
            cal.get(Calendar.YEAR) == year &&
                cal.get(Calendar.MONTH) == month &&
                cal.get(Calendar.DAY_OF_MONTH) == day
        }.sortedByDescending { it.fechaCreacion }
    }

    fun daysWithTasksInMonth(tasks: List<Task>, year: Int, month: Int): Set<Int> {
        val cal = Calendar.getInstance()
        val result = mutableSetOf<Int>()
        for (task in tasks) {
            cal.timeInMillis = task.fechaCreacion
            if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month) {
                result.add(cal.get(Calendar.DAY_OF_MONTH))
            }
        }
        return result
    }
}
