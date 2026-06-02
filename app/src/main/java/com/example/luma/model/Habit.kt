package com.example.luma.model

data class Habit (
    val id: Int,
    val name: String,
    val category: String,
    val frequency: String,
    val currentStreak: Int, // racha actual
    val bestStreak: Int, // la racha mas larga que tuvo el usuario
    val completedToday: Boolean
)