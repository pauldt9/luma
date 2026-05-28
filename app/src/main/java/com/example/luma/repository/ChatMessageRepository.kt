package com.example.luma.repository

import com.example.luma.model.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ChatMessageRepository {

    // Instancia de Firestore
    private val db = FirebaseFirestore.getInstance()

    // Guarda un mensaje en Firestore
    suspend fun saveMessage(
        userId: String,
        chatId: String,
        text: String,
        isUser: Boolean
    ) {
        val messageRef = db.collection("chat_messages").document()

        // Crea un objeto ChatMessage con los datos del mensaje
        val message = ChatMessage(
            id = messageRef.id,
            chatId = chatId,
            userId = userId,
            text = text,
            user = isUser // Se usa user para evitar problemas con Firestore
        )

        // Guarda el mensaje en Firestore
        messageRef.set(message).await()
    }

    // Obtiene todos los mensajes de un chat
    suspend fun getMessages(chatId: String): List<ChatMessage> {
        return try {
            // Realiza una consulta a Firestore para obtener los mensajes del chat
            val snapshot = db.collection("chat_messages")
                .whereEqualTo("chatId", chatId)
                .get()
                .await()

            // Convierte los documentos de Firestore en objetos ChatMessage y los ordena por fecha
            snapshot.documents.mapNotNull {
                it.toObject(ChatMessage::class.java)
            }.sortedBy { it.timestamp }

        } catch (e: Exception) {
            emptyList()
        }
    }

    // Borra todos los mensajes de un chat
    suspend fun deleteMessagesByChat(chatId: String) {
        val snapshot = db.collection("chat_messages")
            .whereEqualTo("chatId", chatId)
            .get()
            .await()

        // Itera sobre los documentos y los elimina
        snapshot.documents.forEach { document ->
            document.reference.delete().await()
        }
    }
}