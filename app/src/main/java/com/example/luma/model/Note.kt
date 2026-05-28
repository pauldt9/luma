package com.example.luma.model

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val date: String // Para poner en la esquina la fecha de creacion de la nota
)

