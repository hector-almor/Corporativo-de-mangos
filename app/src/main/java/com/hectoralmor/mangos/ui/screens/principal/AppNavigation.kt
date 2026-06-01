package com.hectoralmor.mangos.ui.screens.principal

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController /*Ocupé una dependencia a nivel modulo: app*/
import androidx.navigation.navArgument
import com.hectoralmor.mangos.ui.screens.compra.CompraScreen
import com.hectoralmor.mangos.ui.screens.compra.EditarCompraScreen
import com.hectoralmor.mangos.ui.screens.proveedor.EditarProveedorScreen
import com.hectoralmor.mangos.ui.screens.proveedor.ProveedorScreen
import com.hectoralmor.mangos.ui.theme.MangoColores


/*Esta clase no toca la bdd, solo la navegacion de las pantallas*/
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MangoColores.Fondo)
    ) {
        NavHost(
            navController = navController,
            startDestination = "principal"
        ) {
            composable(route = "principal") {
                PrincipalScreen(
                    onAgregarProveedorScreen = { navController.navigate("proveedor") },
                    onAgregarCompraScreen = { navController.navigate("compra") },
                    onEditarProveedorScreen = { navController.navigate("editarproveedor") },
                    onEditarCompraScreen = { compraId -> navController.navigate("editarcompra/$compraId") }
                )
            }
            composable(route = "compra") {
                CompraScreen(
                    onCancelar = { navController.popBackStack() },
                    onGuardar = { navController.popBackStack() }
                )
            }
            composable(route = "proveedor") {
                ProveedorScreen(
                    onCancelar = { navController.popBackStack() },
                    onGuardar = { navController.popBackStack() }
                )
            }
            composable(route = "editarproveedor") {
                EditarProveedorScreen(
                    onCancelar = { navController.popBackStack() },
                    onGuardar = { navController.popBackStack() },
                    onEliminar = { navController.popBackStack() }
                )
            }
            composable(
                route = "editarcompra/{compraId}",
                arguments = listOf(navArgument("compraId") { type = NavType.LongType })
            ) { backStackEntry ->
                val compraId = backStackEntry.arguments?.getLong("compraId") ?: return@composable
                EditarCompraScreen(
                    compraId = compraId,
                    onCancelar = { navController.popBackStack() },
                    onGuardar = { navController.popBackStack() },
                    onEliminar = { navController.popBackStack() }
                )
            }
        }
    }
}