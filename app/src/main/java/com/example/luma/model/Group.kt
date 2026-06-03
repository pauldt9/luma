package com.example.luma.model

data class Group(
    val id: String = "", // Identificador único del grupo
    val userId: String = "", // ID del usuario al que pertenece el grupo
    val categoryId: String = "", // ID de la categoría a la que pertenece el grupo
    val categoryName: String = "", // Nombre de la categoría
    val name: String = "" // Nombre del grupo
)