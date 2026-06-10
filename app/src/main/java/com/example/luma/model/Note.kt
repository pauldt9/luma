package com.example.luma.model

data class Note(
    val id: String = "", // Identificador de la nota
    val userId: String = "", // ID del usuario al que pertenece la nota
    val groupName: String = "", // Nombre del grupo al que pertenece la nota
    val title: String = "", // Título
    val content: String = "", // Contenido
    val date: String = "", // Fecha de creación
    val timestamp: Long = 0L
)