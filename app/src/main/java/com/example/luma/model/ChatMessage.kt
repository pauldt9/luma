package com.example.luma.model

data class ChatMessage(
    val text: String, // contenido del mensaje
    val isUser: Boolean // determinar si es el usuario o la IA
)