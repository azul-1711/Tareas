package com.example.persistencia.ui.theme

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.persistencia.data.Task
import com.example.persistencia.data.TaskDatabase
import com.example.persistencia.data.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository

    val tasks: StateFlow<List<Task>>

    private val connectivityManager =
        application.getSystemService(ConnectivityManager::class.java)

    private val isOnline = MutableStateFlow(false)

    // Evita dos sincronizaciones simultáneas
    private val syncMutex = Mutex()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            isOnline.value = true
            syncTasks() // volvió la red: sincronizar lo pendiente
        }

        override fun onLost(network: Network) {
            isOnline.value = false
        }
    }

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        tasks = repository.allTasks
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

        isOnline.value = hasInternet()
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        syncTasks() // sincroniza pendientes de sesiones anteriores
    }

    private fun hasInternet(): Boolean {
        val caps = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    fun addTask(titulo: String, descripcion: String) {
        if (titulo.isBlank()) return
        viewModelScope.launch {
            repository.insert(Task(titulo = titulo, descripcion = descripcion, isSynced = false))
            syncTasks()
        }
    }

    fun toggleTaskState(task: Task) {
        viewModelScope.launch {
            repository.update(task.copy(estadoCompletado = !task.estadoCompletado, isSynced = false))
            syncTasks()
        }
    }

    fun updateTask(task: Task, nuevoTitulo: String, nuevaDescripcion: String) {
        if (nuevoTitulo.isBlank()) return
        viewModelScope.launch {
            repository.update(task.copy(titulo = nuevoTitulo, descripcion = nuevaDescripcion, isSynced = false))
            syncTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }
    fun syncTasks() {
        if (!isOnline.value) return
        viewModelScope.launch {
            if (!syncMutex.tryLock()) return@launch
            try {
                repository.syncPending()
            } finally {
                syncMutex.unlock()
            }
        }
    }

    override fun onCleared() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
        super.onCleared()
    }
}