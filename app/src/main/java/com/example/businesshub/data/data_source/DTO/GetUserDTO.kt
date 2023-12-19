package com.example.businesshub.data.data_source.DTO

data class GetUserDTO(
    val username: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String,
    val objectId: String,


) {
    override fun toString(): String {
        return "GetUserDTO(username='$username', email='$email', createdAt='$createdAt', updatedAt='$updatedAt', objectId='$objectId')"
    }
}
