package com.example.luma.model

data class Habit (
    val id: String = "", // Identificador único del habito
    val userId: String = "", // ID del usuario al que pertenece el habito
    val groupName: String = "", // Nombre del grupo al que pertenece el habito
    val name: String = "", // Nombre del habito
    val description: String = "", // Descripcion del habito
    val frequency: String = "", // Frecuencia del habito
    val currentStreak: Int = 0, // Racha actual
    val bestStreak: Int = 0, // Racha mas larga que tuvo el usuario
    val completedToday: Boolean = false, // Si ya completó el habito hoy
    val lastCompletedDate: String = "", // Última fecha en la que completo el habito
    val createdAt: Long = System.currentTimeMillis() // Fecha de creación del habito
)