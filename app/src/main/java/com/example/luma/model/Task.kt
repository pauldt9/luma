package com.example.luma.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties // Esta anotación indica a Firestore que esta clase no contiene datos adicionales
data class Task(
    val id: String = "", // ID único de la tarea
    val userId: String = "", // ID del usuario al que pertenece la tarea
    val groupName: String = "", // Nombre del grupo al que pertenece la tarea
    val content: String = "", // Contenido de la tarea
    val priority: String = "Baja", // Puede ser "Alta", "Media" o "Baja"
    val dueDate: String = "", // Fecha límite
    val completed: Boolean = false, // Variable para revisar si está completada
    val outdated: Boolean = false, // Variable para revisar si está fuera de tiempo
    val timestamp: Long = System.currentTimeMillis() // Fecha de creación de la tarea
)
