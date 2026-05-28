package com.example.luma.services

import com.example.luma.model.Task
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content

object GeminiService {

    // Inicializamos Gemini
    private val model =
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-3.1-flash-lite")

    suspend fun chatWithTasks(userQuestion: String, tasks: List<Task>): String {

        // Filtramos tareas pendientes
        val pendingTasks = tasks.filter { !it.completed }

        val taskText = if (pendingTasks.isEmpty()) {
            "No hay tareas pendientes."
        } else {
            pendingTasks.joinToString("\n") { task ->
                "- ${task.content} (Prioridad: ${task.priority}, Vence: ${task.dueDate})"
            }
        }

        val prompt = """
            Eres Luma, un asistente de productividad inteligente.

            Contexto del usuario:
            $taskText

            Pregunta del usuario:
            $userQuestion

            Responde breve, amable y en español.
            No uses asteriscos.
        """.trimIndent()

        // Llamamos a la API de Gemini
        return try {

            val response = model.generateContent(
                content {
                    text(prompt)
                }
            )

            response.text ?: "No pude generar una respuesta."

        } catch (e: Exception) {
            "Error con Gemini: ${e.message}"
        }
    }
}