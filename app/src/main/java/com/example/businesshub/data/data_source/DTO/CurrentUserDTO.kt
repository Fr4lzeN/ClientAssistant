package com.example.businesshub.data.data_source.DTO

data class CurrentUserDTO(
    val objectId: String,
    val username: String,
    val createdAt: String,
    val updatedAt: String,
    val sessionToken: String,
)
