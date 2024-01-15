package com.example.businesshub.data.data_source.DTO

data class CompanyDTO(
    val result: Result
) {
    data class Result(
        val objectId: String,
        val name: String,
        val desc: String,
        val addr: String,
        val ogrp: String,
        val inn: String,
        val kpp: String,
    )
}
