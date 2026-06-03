package com.example.luma.services

import com.example.luma.model.Task
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object GeminiService {

    // Inicializamos Gemini
    private val model =
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-3.1-flash-lite")

    // Función para interactuar con Gemini
    suspend fun chatWithTasks(userQuestion: String, tasks: List<Task>): String {

        // Obtenemos la fecha actual
        val today = LocalDate.now()

        // Filtramos tareas pendientes
        val pendingTasks = tasks.filter { !it.completed }

        // Construimos el texto de las tareas
        val taskText = if (pendingTasks.isEmpty()) { // Si no hay tareas pendientes, agregamos eso al mensaje
            "No hay tareas pendientes."
        } else {
            pendingTasks.joinToString("\n") { task ->
                val isOverdue = try {
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val dueDate = LocalDate.parse(task.dueDate, formatter)
                    dueDate.isBefore(today)
                } catch (e: Exception) {
                    false
                }

                // Verificamos si la tarea está atrasada
                val overdueText = if (isOverdue) "Está atrasada" else "No está atrasada"

                // Construimos el texto de la tarea
                "- ${task.content} (Prioridad: ${task.priority}, Vence: ${task.dueDate}, Está atrasada: $overdueText, Grupo al que pertenece: ${task.groupName})"
            }
        }

        // Construimos el prompt para la API
        val prompt = """
            Eres Luma, un asistente de productividad inteligente.

            Tareas del usuario:
            $taskText
            
            Instrucción importante:Si una tarea dice "Sí, está atrasada", debes mencionarlo cuando el usuario pregunte por tareas atrasadas, pendientes urgentes o qué debería hacer primero.

            Pregunta del usuario:
            $userQuestion

            Responde breve, amable y en español.
            No uses asteriscos.
        """.trimIndent()

        // Llamamos a la API de Gemini
        return try {

            // Generamos una respuesta
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