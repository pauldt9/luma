package com.example.luma.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luma.model.AiChat
import com.example.luma.model.ChatMessage
import com.example.luma.repository.AiChatRepository
import com.example.luma.repository.ChatMessageRepository
import com.example.luma.repository.TaskRepository
import com.example.luma.services.GeminiService
import kotlinx.coroutines.launch

class AiViewModel : ViewModel() {

    // Repositorios
    private val taskRepository = TaskRepository()
    private val aiChatRepository = AiChatRepository()
    private val messageRepository = ChatMessageRepository()

    // Mensajes y chats
    val messages = mutableStateListOf<ChatMessage>()
    val chats = mutableStateListOf<AiChat>()

    // Chat actual
    var currentChatId by mutableStateOf<String?>(null)
        private set

    // Cargar chats
    fun loadChats(userId: String) {
        viewModelScope.launch {
            chats.clear()
            chats.addAll(aiChatRepository.getUserChats(userId))
        }
    }

    // Seleccionar chat
    fun selectChat(chatId: String) {
        currentChatId = chatId // Actualizamos el chat actual
        messages.clear() // Limpiamos los mensajes

        // Cargamos los mensajes del chat seleccionado
        viewModelScope.launch {
            val savedMessages = messageRepository.getMessages(chatId)

            messages.clear() // Limpiamos los mensajes
            messages.addAll(savedMessages) // Agregamos los mensajes guardados
        }
    }

    // Crear nuevo chat
    fun newChat() {
        currentChatId = null
        messages.clear()
    }

    // Enviar mensaje al chat
    fun sendMessage(userId: String, question: String) {
        viewModelScope.launch {

            // Creamos un título para el chat a partir de la pregunta
            val title = question
                .split(" ")
                .take(4)
                .joinToString(" ")

            // Creamos el chat si no existe
            val chatId = currentChatId ?: aiChatRepository.createChat(
                userId = userId,
                title = title
            )

            currentChatId = chatId

            // Guardamos el mensaje del usuario
            val userMessage = ChatMessage(
                chatId = chatId,
                text = question,
                user = true
            )

            messages.add(userMessage)
            messageRepository.saveMessage(userId, chatId, question, true)

            // Obtenemos las tareas del usuario
            val tasks = taskRepository.getUserTasks(userId)
            // Llamamos a Gemini para obtener una respuesta
            val response = GeminiService.chatWithTasks(question, tasks)

            // Guardamos el mensaje de Gemini
            val aiMessage = ChatMessage(
                chatId = chatId,
                text = response,
                user = false
            )

            messages.add(aiMessage)
            messageRepository.saveMessage(userId, chatId, response, false)

            // Actualizamos los chats
            loadChats(userId)
        }
    }
}