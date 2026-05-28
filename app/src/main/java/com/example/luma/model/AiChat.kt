package com.example.luma.model

data class AiChat(
    val id: String = "", // Identificador único del chat
    val userId: String = "", // ID del usuario al que pertenece el chat
    val title: String = "Nuevo chat", // Título del chat
    val createdAt: Long = System.currentTimeMillis(), // Fecha de creación del chat
    val updatedAt: Long = System.currentTimeMillis() // Fecha de actualización del chat
)