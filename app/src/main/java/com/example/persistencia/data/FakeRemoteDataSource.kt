package com.example.persistencia.data

import kotlinx.coroutines.delay

class FakeRemoteDataSource {

    private val servidor = mutableMapOf<Int, Task>()

    suspend fun subirTareas(tareas: List<Task>) {
        delay(1500) // latencia de red simulada
        tareas.forEach { servidor[it.id] = it.copy(isSynced = true) }
    }
}