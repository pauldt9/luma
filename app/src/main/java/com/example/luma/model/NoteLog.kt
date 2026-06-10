package com.example.luma.model

data class NoteLog(
    val id: String = "", // Identificador único del registro
    val userId: String = "", // ID del usuario al que pertenece el registro
    val noteId: String = "", // ID de la nota a la que pertenece el registro
    val oldTitle: String = "", // Título anterior de la nota
    val oldContent: String = "", // Contenido anterior de la nota
    val oldGroupName: String = "", // Grupo anterior de la nota
    val newTitle: String = "", // Nuevo título de la nota
    val newContent: String = "", // Nuevo contenido de la nota
    val newGroupName: String = "", // Nuevo grupo de la nota
    val action: String = "", // creada, actualizada, eliminada
    val timestamp: Long = System.currentTimeMillis() // Fecha y hora del registro
)