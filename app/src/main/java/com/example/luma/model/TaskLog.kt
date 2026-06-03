package com.example.luma.model

data class TaskLog(
    val id: String = "", // ID único del registro
    val userId: String = "", // ID del usuario al que pertenece el registro
    val taskId: String = "", // ID de la tarea a la que pertenece el registro
    val groupName: String = "", // Nombre del grupo al que pertenece la tarea
    val content: String = "", // Contenido de la tarea
    val action: String = "", // creado, actualizado, completado, eliminado
    val completed: Boolean = false, // Variable para revisar si está completada
    val priority: String = "", // Puede ser "Alta", "Media" o "Baja"
    val dueDate: String = "", // Fecha límite
    val timestamp: Long = System.currentTimeMillis() // Fecha de creación del registro
)