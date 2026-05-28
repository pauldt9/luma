package com.example.luma.repository

import com.example.luma.model.AiChat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AiChatRepository {

    // Instancia de Firestore
    private val db = FirebaseFirestore.getInstance()

    // Crea un nuevo chat con el título y el ID del usuario
    suspend fun createChat(userId: String, title: String): String {
        // Crea un nuevo documento en la colección "ai_chats"
        val chatRef = db.collection("ai_chats").document()

        // Crea el objeto AiChat con los datos
        val chat = AiChat(
            id = chatRef.id,
            userId = userId,
            title = title
        )

        // Guarda el objeto AiChat en Firestore
        chatRef.set(chat).await()
        // Devuelve el ID del chat
        return chatRef.id
    }

    // Obtiene el último chat del usuario
    suspend fun getLastChat(userId: String): AiChat? {
        // Realiza una consulta a la colección "ai_chats"
        val snapshot = db.collection("ai_chats")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        // Devuelve el último chat encontrado
        return snapshot.documents
            .mapNotNull { it.toObject(AiChat::class.java) }
            .maxByOrNull { it.updatedAt }
    }

    // Obtiene todos los chats del usuario
    suspend fun getUserChats(userId: String): List<AiChat> {
        // Realiza una consulta a la colección "ai_chats"
        val snapshot = db.collection("ai_chats")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        // Devuelve la lista de chats
        return snapshot.documents
            .mapNotNull { it.toObject(AiChat::class.java) }
            .sortedByDescending { it.updatedAt }
    }

    // Actualiza la fecha de actualización del chat
    suspend fun updateChatDate(chatId: String) {
        db.collection("ai_chats")
            .document(chatId)
            .update("updatedAt", System.currentTimeMillis())
            .await()
    }
}