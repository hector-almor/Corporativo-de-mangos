package com.hectoralmor.mangos.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proveedores")
data class ProveedorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val direccion: String
)