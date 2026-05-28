package com.example.luma.model

data class ChatMessage(
    val id: String = "", // Identificador único del mensaje
    val chatId: String = "", // ID del chat al que pertenece el mensaje
    val userId: String = "", // ID del usuario que envió el mensaje
    val text: String = "", // Contenido del mensaje
    val user: Boolean = false, // Determinar si es el usuario o la IA
    val timestamp: Long = System.currentTimeMillis() // Marca de tiempo para el mensaje
)