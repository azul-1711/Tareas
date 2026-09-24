package com.example.persistencia.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao,
    private val remote: FakeRemoteDataSource = FakeRemoteDataSource()
) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun getTaskById(id: Int): Task? {
        return taskDao.getTaskById(id)
    }

    suspend fun insert(task: Task): Long {
        return taskDao.insertTask(task)
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun getUnsyncedTasks(): List<Task> {
        return taskDao.getUnsyncedTasks()
    }

    suspend fun syncPending(): Int {
        val pendientes = taskDao.getUnsyncedTasks()
        if (pendientes.isEmpty()) return 0

        remote.subirTareas(pendientes)

        var sincronizadas = 0
        pendientes.forEach { enviada ->
            val actual = taskDao.getTaskById(enviada.id)
            if (actual == enviada) {
                taskDao.updateTask(actual.copy(isSynced = true))
                sincronizadas++
            }
        }
        return sincronizadas
    }
}