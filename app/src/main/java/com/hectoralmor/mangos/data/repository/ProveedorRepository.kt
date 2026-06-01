package com.hectoralmor.mangos.data.repository

import com.hectoralmor.mangos.data.dao.ProveedorDao
import com.hectoralmor.mangos.data.entity.ProveedorEntity
import com.hectoralmor.mangos.domain.model.Proveedor
import com.hectoralmor.mangos.domain.model.repository.ProveedorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProveedorRepository(private val dao: ProveedorDao) : ProveedorRepository {

    override fun obtenerTodos(): Flow<List<Proveedor>> =
        dao.obtenerTodos().map { lista -> lista.map { it.toDomain() } }

    override suspend fun obtenerProveedorId(id: Int): Proveedor =
        dao.obtenerProveedorId(id).toDomain()

    override suspend fun guardar(proveedor: Proveedor) =
        dao.insertar(proveedor.toEntity())

    override suspend fun actualizar(proveedor: Proveedor) =
        dao.actualizar(proveedor.toEntity())

    override suspend fun eliminar(proveedor: Proveedor) =
        dao.eliminarProveedorConCompras(proveedor.id)
}

private fun ProveedorEntity.toDomain() = Proveedor(
    id = id,
    nombre = nombre,
    direccion = direccion
)

private fun Proveedor.toEntity() = ProveedorEntity(
    id = id,
    nombre = nombre,
    direccion = direccion
)