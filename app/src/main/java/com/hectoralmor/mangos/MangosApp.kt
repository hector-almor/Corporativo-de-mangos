package com.hectoralmor.mangos

import android.app.Application
import androidx.room.Room
import com.hectoralmor.mangos.data.AppDatabase

class MangosApp : Application() {
    val database by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "mangos_db"
        ).build()
    }
}