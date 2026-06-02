package com.example.luma.model

data class Habit (
    val id: String = "",
    val userId: String = "",
    val groupName: String = "",
    val name: String = "",
    val description: String = "",
    val frequency: String = "",
    val currentStreak: Int = 0, // racha actual
    val bestStreak: Int = 0, // la racha mas larga que tuvo el usuario
    val completedToday: Boolean = false, // si ya completo el habito hoy
    val lastCompletedDate: String = "", // ultima fecha en la que completo el habito
    val createdAt: Long = System.currentTimeMillis()
)