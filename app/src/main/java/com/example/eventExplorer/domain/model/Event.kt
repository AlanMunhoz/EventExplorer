package com.example.eventExplorer.domain.model

data class Event (
    val people: List<People>,
    val data: Long,
    val description: String,
    val image: String,
    val longitude: Float,
    val latitude: Float,
    val price: Float,
    val title: String,
    val id: String,
    val cupons: List<Cupons>
)