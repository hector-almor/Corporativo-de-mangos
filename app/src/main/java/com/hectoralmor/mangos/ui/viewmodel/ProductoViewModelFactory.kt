package com.hectoralmor.mangos.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hectoralmor.mangos.data.AppDatabase

class ProductoViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = AppDatabase.getDatabase(application).dao
        @Suppress("UNCHECKED_CAST")
        return ProductoViewModel(dao) as T
    }
}