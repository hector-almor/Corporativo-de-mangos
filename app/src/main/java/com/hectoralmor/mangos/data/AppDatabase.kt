package com.hectoralmor.mangos.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hectoralmor.mangos.data.dao.CompraDao
import com.hectoralmor.mangos.data.dao.ProveedorDao
import com.hectoralmor.mangos.data.entity.CompraEntity
import com.hectoralmor.mangos.data.entity.ProveedorEntity

@Database(entities = [ProveedorEntity::class, CompraEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun compraDao(): CompraDao
    abstract fun proveedorDao(): ProveedorDao
}