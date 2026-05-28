package com.example.luma.repository

import com.example.luma.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// Repositorio para interactuar con Firestore
class TaskRepository {

    private val db = FirebaseFirestore.getInstance() // Instancia de Firestore

    // Obtiene las tareas del usuario
    suspend fun getUserTasks(userId: String): List<Task> {

        return try {

            // Realiza una consulta a Firestore para obtener las tareas del usuario
            val snapshot = db.collection("tasks")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            // Convierte los documentos de Firestore en objetos Task y los devuelve
            snapshot.documents.mapNotNull { document ->
                document.toObject(Task::class.java)
            }

        } catch (e: Exception) {

            emptyList()
        }
    }
}