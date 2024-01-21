package com.example.businesshub.data.data_source.DTO

data class CurrentUserDTO(
    val objectId: String,
    val username: String,
    val createdAt: String,
    val updatedAt: String,
    val sessionToken: String,
    val company: Company? = null,
    val person: Person? = null,
){

    data class Company(
        val objectId: String,
        val __type: String,
        val className: String,
    )
    data class Person(
        val objectId: String,
        val __type: String,
        val className: String,
    )


}
