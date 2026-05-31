package com.hectoralmor.mangos.data.repository

import com.hectoralmor.mangos.data.CompraConProveedor
import com.hectoralmor.mangos.data.dao.CompraDao
import com.hectoralmor.mangos.data.entity.CompraEntity
import kotlinx.coroutines.flow.Flow

class CompraRepository(private val dao: CompraDao) {
    fun obtenerTodas(): Flow<List<CompraConProveedor>> = dao.obtenerTodas()
    fun obtenerPorFecha(fecha: String): Flow<List<CompraConProveedor>> = dao.obtenerPorFecha(fecha)

    suspend fun guardar(compra: CompraEntity) = dao.insertar(compra)
    suspend fun obtenerCompraId(id: Long) = dao.obtenerCompraId(id)
    suspend fun actualizar(compra: CompraEntity) = dao.actualizar(compra)
    suspend fun eliminar(compra: CompraEntity) = dao.eliminar(compra)
}