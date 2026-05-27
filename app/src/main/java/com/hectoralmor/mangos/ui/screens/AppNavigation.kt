package com.hectoralmor.mangos.ui.screens

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hectoralmor.mangos.ui.viewmodel.ProductoViewModel
import com.hectoralmor.mangos.ui.viewmodel.ProductoViewModelFactory

@Composable
fun AppNavigation() {
    // Obtenemos el ViewModel usando nuestro Factory
    //val viewModel:  ProductoViewModel = viewModel(factory = ProductoViewModelFactory)
    val context = LocalContext.current
    val app = context.applicationContext as Application

    val productoViewModel: ProductoViewModel = viewModel(factory = ProductoViewModelFactory(app))

    // Observamos el estado de los productos (se actualiza solo)
    val listaProductos by productoViewModel.productos.collectAsState()

    PrincipalScreen(
        listaProductos = listaProductos,
        onAgregarProductoClick = {
            productoViewModel.agregarProducto("Producto ${listaProductos.size + 1}", 10.5, "Descripción de prueba")
        },
        onLimpiarClick = {
            productoViewModel.limpiarProductos()
        }
    )
}