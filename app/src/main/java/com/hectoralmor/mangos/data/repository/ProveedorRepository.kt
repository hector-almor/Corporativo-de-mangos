package com.hectoralmor.mangos.data.repository

import com.hectoralmor.mangos.data.dao.ProveedorDao
import com.hectoralmor.mangos.data.entity.ProveedorEntity
import kotlinx.coroutines.flow.Flow


class ProveedorRepository(private val dao: ProveedorDao) {

    fun obtenerTodos(): Flow<List<ProveedorEntity>> = dao.obtenerTodos()

    suspend fun obtenerProveedorId(id: Int): ProveedorEntity = dao.obtenerProveedorId(id)

    suspend fun guardar(proveedor: ProveedorEntity) = dao.insertar(proveedor)

    suspend fun actualizar(proveedor: ProveedorEntity) = dao.actualizar(proveedor)

    suspend fun eliminar(proveedor: ProveedorEntity) = dao.eliminar(proveedor)
}