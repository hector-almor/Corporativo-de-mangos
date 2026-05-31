package com.hectoralmor.mangos.ui.screens.principal

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController /*Ocupé una dependencia a nivel modulo: app*/
import com.hectoralmor.mangos.ui.screens.compra.CompraScreen
import com.hectoralmor.mangos.ui.screens.compra.EditarCompraScreen
import com.hectoralmor.mangos.ui.screens.proveedor.EditarProveedorScreen
import com.hectoralmor.mangos.ui.screens.proveedor.ProveedorScreen

// ESTA CLASE EN TEORÍA NO DEBE TOCAR LA BD, SI NO QUE, SOLO DELEGA A LAS SCREEN
@Composable
fun AppNavigation() {
    // Obtenemos el ViewModel usando nuestro Factory
    // val viewModel:  ProductoViewModel = viewModel(factory = ProductoViewModelFactory)
    val context = LocalContext.current
    val app = context.applicationContext as Application

    //val productoViewModel: ProductoViewModel = viewModel(factory = ProductoViewModelFactory(app))

    // Observamos el estado de los productos (se actualiza solo)
    // val listaProductos by productoViewModel.productos.collectAsState()

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "principal" /*Empieza en el principal, que seria la principalscreen*/
    ) {
        composable(route = "principal") {
            PrincipalScreen(
                // listaProductos = listaProductos,
                /*
                onAgregarProductoClick = {
                    productoViewModel.agregarProducto(
                        "Compra ${listaProductos.size + 1}",
                        10.5,
                        "Cantidad: 10"
                    )
                },*/
                // onLimpiarClick = { productoViewModel.limpiarProductos() },
                onAgregarProveedorScreen = { navController.navigate("proveedor") },
                onAgregarCompraScreen = { navController.navigate("compra") },
                onEditarProveedorScreen = { navController.navigate("editarproveedor") },
                onEditarCompraScreen = { navController.navigate("editarcompra") } /*Navega a editar compra*/
            )
        }
        composable(route = "compra") {
            CompraScreen(
                onCancelar = { navController.popBackStack() },
                onGuardar = { navController.popBackStack() } // aquí después agregas la lógica de guardar
            )
        }
        composable(route = "proveedor") {
            ProveedorScreen(
                onCancelar = { navController.popBackStack() },
                onGuardar = { navController.popBackStack() } /*Aqui despues agregas la logica de guardar*/
            )
        }
        composable(route = "editarproveedor") {
            EditarProveedorScreen(
                onCancelar = { navController.popBackStack() },
                onGuardar = { navController.popBackStack() }, /*Aqui despues agregas la logica de guardar*/
                onEliminar = { navController.popBackStack() } /*Aqui despues agregas la logica de eliminar*/
            )
        }
        composable(route = "editarcompra") {
            EditarCompraScreen(
                onCancelar = { navController.popBackStack() },
                onGuardar = { navController.popBackStack() }, /*Aqui despues agregas la logica de guardar*/
                onEliminar = { navController.popBackStack() } /*Aqui despues agregas la logica de eliminar*/
            )
        }
    }
}