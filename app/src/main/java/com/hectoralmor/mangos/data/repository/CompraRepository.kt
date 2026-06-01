package com.hectoralmor.mangos.data.repository

import com.hectoralmor.mangos.data.dao.CompraDao
import com.hectoralmor.mangos.data.entity.CompraEntity
import com.hectoralmor.mangos.domain.model.Compra
import com.hectoralmor.mangos.domain.model.repository.CompraRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CompraRepository(private val dao: CompraDao) : CompraRepository {

    override fun obtenerTodas(): Flow<List<Compra>> =
        dao.obtenerTodas().map { lista -> lista.map { it.toDomain() } }

    override fun obtenerPorFecha(fecha: String): Flow<List<Compra>> =
        dao.obtenerPorFecha(fecha).map { lista -> lista.map { it.toDomain() } }

    override suspend fun obtenerCompraId(id: Long): Compra =
        dao.obtenerCompraId(id).toDomain()

    override suspend fun guardar(compra: Compra) =
        dao.insertar(compra.toEntity())

    override suspend fun actualizar(compra: Compra) =
        dao.actualizar(compra.toEntity())

    override suspend fun eliminar(compra: Compra) =
        dao.eliminar(compra.toEntity())
}

private fun com.hectoralmor.mangos.data.CompraConProveedor.toDomain() = Compra(
    id = compra.id,
    proveedorId = compra.proveedorId,
    nombre = proveedor.nombre,
    cantidad = compra.cantidad,
    precio = compra.precio,
    fecha = compra.fecha,
    total = compra.total
)

private fun Compra.toEntity() = CompraEntity(
    id = id,
    proveedorId = proveedorId,
    cantidad = cantidad,
    precio = precio,
    fecha = fecha,
    total = total
)