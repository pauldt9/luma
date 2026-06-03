package com.example.luma.model

data class NoteLog(
    val id: String = "", // Identificador único del registro
    val userId: String = "", // ID del usuario al que pertenece el registro
    val noteId: String = "", // ID de la nota a la que pertenece el registro
    val title: String = "", // Título de la nota
    val content: String = "", // Contenido de la nota
    val action: String = "", // creada, actualizada, eliminada
    val timestamp: Long = System.currentTimeMillis() // Fecha y hora del registro
)