package com.hectoralmor.mangos.ui.screens.proveedor

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hectoralmor.mangos.R

@Composable
fun EditarProveedorScreen(
    onCancelar: () -> Unit,
    onGuardar: () -> Unit,
    onEliminar: () -> Unit,
    // AL IGUAL QUE EN PRINCIPALVIEWMODEL, SE DECLARA AQUI
    viewModel: ProveedorViewModel = viewModel(factory = ProveedorViewModel.Factory)
) {
    /*De prueba, agregar de la bdd*/
    val proveedores = listOf("Proveedor A", "Proveedor B", "Proveedor C")

    /*Datos de prueba, despues reemplaza por los de la bdd*/
    val datosPorProveedor = mapOf(
        "Proveedor A" to Pair("Proveedor A", "Calle nose 123"),
        "Proveedor B" to Pair("Proveedor B", "Avenida mi casa 456"),
        "Proveedor C" to Pair("Proveedor C", "La Chingada 789")
    )

    var proveedorSeleccionado by remember { mutableStateOf("Selecciona un proveedor") }
    var dropdownExpandido by remember { mutableStateOf(false) }
    var nombreProveedor by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    val context = LocalContext.current /*Para poder lanzar el intent de google maps*/

    /*Solo se puede editar si hay un proveedor seleccionado*/
    val ProveedorSeleccionadoFiltro = proveedorSeleccionado != "Selecciona un proveedor"

    /*Para activar el boton de guardar cuando los campos tengan un valor valido*/
    val botonValidoFiltro = remember(nombreProveedor, ProveedorSeleccionadoFiltro) {
        ProveedorSeleccionadoFiltro && nombreProveedor.isNotBlank() /*Que haya proveedor seleccionado y nombre no vacio*/
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Editar Proveedor", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.weight(1f))
                VerticalDivider(
                    modifier = Modifier.padding(horizontal = 18.dp).height(28.dp),
                    thickness = 1.dp
                )
                Text(text = "Mangos USA", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.weight(0.9f))

            Text(
                text = "Selecciona un proveedor para poder editar sus datos",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.alpha(0.5f)
            )

            Spacer(modifier = Modifier.weight(2f))

            /*Dropdown para seleccionar el proveedor a editar*/
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Proveedor: ", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, modifier = Modifier.width(110.dp))
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { dropdownExpandido = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(proveedorSeleccionado, modifier = Modifier.weight(1f).alpha(if (ProveedorSeleccionadoFiltro) 1f else 0.5f), fontSize = 16.sp)
                        Text("+", fontSize = 16.sp)
                    }
                    DropdownMenu(
                        expanded = dropdownExpandido,
                        onDismissRequest = { dropdownExpandido = false }
                    ) {
                        proveedores.forEach { proveedor ->
                            DropdownMenuItem(
                                text = { Text(proveedor) }, /*Aqui se llama la lista de proveedores que falta modificar conforme la bdd*/
                                onClick = {
                                    proveedorSeleccionado = proveedor
                                    dropdownExpandido = false
                                    /*Carga los datos del proveedor seleccionado en los campos*/
                                    val datos = datosPorProveedor[proveedor]
                                    nombreProveedor = datos?.first ?: ""
                                    direccion = datos?.second ?: ""
                                }
                            )
                        }
                    }
                }
                /*Boton de deseleccionar, este solo visible si hay un proveedor seleccionado*/
                if (ProveedorSeleccionadoFiltro) {
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {
                        proveedorSeleccionado = "Selecciona un proveedor"
                        nombreProveedor = ""
                        direccion = ""
                    }) {
                        Text("x", fontSize = 18.sp, color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            /*Campo del nombre*/
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Nombre: ", fontWeight = FontWeight.SemiBold, fontSize = 18.sp,
                    modifier = Modifier.width(110.dp).alpha(if (ProveedorSeleccionadoFiltro) 1f else 0.4f))
                OutlinedTextField(
                    value = nombreProveedor,
                    onValueChange = { input ->
                        if (!ProveedorSeleccionadoFiltro) return@OutlinedTextField /*Bloquea si no hay proveedor seleccionado*/
                        val filtro = input.filter { it.isLetterOrDigit() || it == ' ' } /*Solo letras, numeros y espacios*/
                        if (filtro.length > 50) return@OutlinedTextField /*Maximo 50 caracteres*/
                        nombreProveedor = filtro
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = ProveedorSeleccionadoFiltro, /*Desactivado hasta que se seleccione un proveedor*/
                    singleLine = true, /*En una sola linea, nada de enter*/
                    shape = RoundedCornerShape(4.dp),
                    placeholder = { Text("Nombre del proveedor", modifier = Modifier.alpha(0.5f)) }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            /*Campo de la direccion*/
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Dirección: ", fontWeight = FontWeight.SemiBold, fontSize = 18.sp,
                    modifier = Modifier.width(110.dp).alpha(if (ProveedorSeleccionadoFiltro) 1f else 0.4f))
                OutlinedTextField(
                    value = direccion,
                    onValueChange = { input ->
                        if (!ProveedorSeleccionadoFiltro) return@OutlinedTextField /*Bloquea si no hay proveedor seleccionado*/
                        if (input.length > 100) return@OutlinedTextField /*Maximo 100 caracteres*/
                        direccion = input
                    },
                    modifier = Modifier.weight(1f),
                    enabled = ProveedorSeleccionadoFiltro, /*Desactivado hasta que se seleccione un proveedor*/
                    singleLine = true,
                    shape = RoundedCornerShape(4.dp),
                    placeholder = { Text("Dirección del proveedor", modifier = Modifier.alpha(0.5f)) },
                    trailingIcon = {
                        /*Boton dentro del textfield para abrir maps con la direccion escrita*/
                        IconButton(
                            onClick = {
                                if (direccion.isNotBlank()) {
                                    val uri = Uri.parse("geo:0,0?q=${Uri.encode(direccion)}")
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    context.startActivity(intent)
                                }
                            },
                            enabled = direccion.isNotBlank() && ProveedorSeleccionadoFiltro /*Solo si hay algo escrito y proveedor seleccionado*/
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.mapslogo),
                                contentDescription = "Icono de Google Maps",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.weight(6f))

            HorizontalDivider(thickness = 1.dp)

            Spacer(modifier = Modifier.weight(1f))

            /*ESto para la fila de botones*/
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                /*Eliminar a la izq*/
                Button(
                    onClick = onEliminar,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.4f)
                    ),
                    enabled = ProveedorSeleccionadoFiltro
                ) {
                    Text("Eliminar", fontSize = 16.sp, color = Color.White)
                }

                /*Cancelar y guardar a la derecha*/
                Row {
                    OutlinedButton(
                        onClick = onCancelar,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.height(52.dp)
                    ) {
                        Text("Cancelar", fontSize = 16.sp, color = Color.Red)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = onGuardar,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF368A39),
                            disabledContainerColor = Color.Gray.copy(alpha = 0.4f)
                        ),
                        enabled = botonValidoFiltro
                    ) {
                        Text("Guardar", fontSize = 16.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}