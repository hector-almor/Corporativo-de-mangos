package com.hectoralmor.mangos.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao // Limpiamos el prefijo redundante ya que tenemos el import
interface ProductoDao {

    // Cambiado de upsertProducto a upsert
    @Upsert
    suspend fun upsert(producto: Producto)

    // Cambiado de deleteProducto a delete
    @Delete
    suspend fun delete(producto: Producto)

    // Cambiado de getProductos a getAll (este es el que le faltaba a tu ViewModel)
    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun getAll(): Flow<List<Producto>>

    // Cambiado de getProductoById a getById
    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun getById(id: Int): Producto?
}