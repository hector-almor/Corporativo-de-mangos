package com.hectoralmor.mangos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val precio: Double,
    val descripcion: String
)

/*Cambialo a
* @Entity(tableName = "Compra")
data class Compra(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val proveedor: String,
    val cantidad: int //Cantidad
    val precio: Double, //Precio
    val fecha: String, //fecha
    val total: Double, //total
)
*/