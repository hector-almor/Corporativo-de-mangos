package com.hectoralmor.mangos.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hectoralmor.mangos.data.entity.ProveedorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProveedorDao {
    @Query("SELECT * FROM proveedores ORDER BY nombre ASC")
    fun obtenerTodos(): Flow<List<ProveedorEntity>>

    @Query("SELECT * FROM proveedores WHERE id = :id")
    suspend fun obtenerProveedorId(id: Int): ProveedorEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(proveedor: ProveedorEntity)

    @Delete
    suspend fun eliminar(proveedor: ProveedorEntity)

    @Update
    suspend fun actualizar(proveedor: ProveedorEntity)
}