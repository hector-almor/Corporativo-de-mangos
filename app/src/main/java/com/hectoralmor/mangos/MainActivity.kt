package com.hectoralmor.mangos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hectoralmor.mangos.ui.screens.principal.AppNavigation
import com.hectoralmor.mangos.ui.theme.CorporativoDeMangosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CorporativoDeMangosTheme {
                AppNavigation()
            }
        }
    }
}

/*
package com.hectoralmor.mangos

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.material3.HorizontalDivider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Obtenemos el ViewModel usando nuestro Factory
            //val viewModel:  ProductoViewModel = viewModel(factory = ProductoViewModelFactory)
            val app = application as Application
            val viewModel: ProductoViewModel = viewModel(factory = ProductoViewModelFactory(app))


            // Observamos el estado de los productos (se actualiza solo)
            val listaProductos by viewModel.productos.collectAsState()

            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        viewModel.agregarProducto("Producto ${listaProductos.size + 1}", 10.5, "Descripción de prueba")
                    }) {
                        Text("+")
                    }
                }
            ) { padding ->
                Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                    Text("Lista de Productos", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))

                    LazyColumn {
                        items(listaProductos) { producto ->
                            ListItem(
                                headlineContent = { Text(producto.nombre) },
                                supportingContent = { Text(producto.descripcion) },
                                trailingContent = { Text("$${producto.precio}") },
                                modifier = Modifier.padding(8.dp)
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
*/

