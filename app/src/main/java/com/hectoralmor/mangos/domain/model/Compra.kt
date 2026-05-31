package com.hectoralmor.mangos.domain.model

data class Compra(
    val id: Long = 0,
    val proveedorId: Int,
    val nombre: String,
    val cantidad: Double,
    val precio: Double,
    val fecha: String,
    val total: Double = precio * cantidad
)

