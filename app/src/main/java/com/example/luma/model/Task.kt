package com.example.luma.model

data class Task (
    val id: Int,
    val content: String,
    val priority: TaskPriority,
    val isCompleted: Boolean
)

// Valores fijos
enum class TaskPriority {
    HIGH,
    MEDIUM,
    LOW
}