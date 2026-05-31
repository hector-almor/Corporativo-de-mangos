package com.hectoralmor.mangos.domain.model.repository

import com.hectoralmor.mangos.domain.model.Proveedor
import kotlinx.coroutines.flow.Flow


interface ProveedorRepository {
    fun obtenerTodos(): Flow<List<Proveedor>>
    suspend fun guardar(proveedor: Proveedor)
    suspend fun eliminar(proveedor: Proveedor)
    suspend fun actualizar(proveedor: Proveedor)
}