package com.hectoralmor.mangos.data

import androidx.room.Embedded
import androidx.room.Relation
import com.hectoralmor.mangos.data.entity.CompraEntity
import com.hectoralmor.mangos.data.entity.ProveedorEntity

data class CompraConProveedor(
    @Embedded val compra: CompraEntity,
    @Relation(
        parentColumn = "proveedorId",
        entityColumn = "id"
    )
    val proveedor: ProveedorEntity
)