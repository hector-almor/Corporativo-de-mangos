package com.hectoralmor.mangos.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "compras",
    foreignKeys = [
        ForeignKey(
            entity = ProveedorEntity::class,
            parentColumns = ["id"],
            childColumns = ["proveedorId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("proveedorId")]
)
data class CompraEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val proveedorId: Int,
    val cantidad: Double,
    val precio: Double,
    val fecha: String,
    val total: Double
)