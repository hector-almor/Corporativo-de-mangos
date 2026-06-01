package com.hectoralmor.mangos.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hectoralmor.mangos.data.CompraConProveedor
import com.hectoralmor.mangos.data.entity.CompraEntity
import com.hectoralmor.mangos.data.entity.ProveedorEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface CompraDao {
    @Transaction
    @Query("SELECT * FROM compras ORDER BY id ASC")
    fun obtenerTodas(): Flow<List<CompraConProveedor>>

    @Transaction
    @Query("SELECT * FROM compras WHERE fecha = :fecha")
    fun obtenerPorFecha(fecha: String): Flow<List<CompraConProveedor>>

    @Transaction
    @Query("SELECT * FROM compras WHERE id = :id")
    suspend fun obtenerCompraId(id: Long): CompraConProveedor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(compra: CompraEntity)

    @Delete
    suspend fun eliminar(compra: CompraEntity)

    @Update
    suspend fun actualizar(compra: CompraEntity)
}