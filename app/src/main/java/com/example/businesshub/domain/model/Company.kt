package com.example.businesshub.domain.model

data class Company(
    val name: String,
    val desc: String,
    val addr: String,
    val ogrn: Long,
    val inn:  Int,
    val kpp:  Int,
    val date: Long,
)
