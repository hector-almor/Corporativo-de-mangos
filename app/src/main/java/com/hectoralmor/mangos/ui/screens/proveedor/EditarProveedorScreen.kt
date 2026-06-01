package com.hectoralmor.mangos.ui.screens.proveedor

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hectoralmor.mangos.R
import com.hectoralmor.mangos.ui.theme.MangoColores

@Composable
fun EditarProveedorScreen(
    onCancelar: () -> Unit,
    onGuardar: () -> Unit,
    onEliminar: () -> Unit,
    // AL IGUAL QUE EN PRINCIPALVIEWMODEL, SE DECLARA AQUI
    viewModel: ProveedorViewModel = viewModel(factory = ProveedorViewModel.Factory)
) {
    val proveedores by viewModel.proveedores.collectAsStateWithLifecycle()
    val proveedorSeleccionado by viewModel.proveedorSeleccionado.collectAsStateWithLifecycle()
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    var dropdownExpandido by remember { mutableStateOf(false) }
    var nombreProveedor by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var guardando by remember { mutableStateOf(false) }
    val context = LocalContext.current /*Para poder lanzar el intent de google maps*/

    LaunchedEffect(proveedorSeleccionado) {
        proveedorSeleccionado?.let {
            nombreProveedor = it.nombre
            direccion = it.direccion
        } ?: run {
            nombreProveedor = ""
            direccion = ""
        }
    }

    /*Solo se puede editar si hay un proveedor seleccionado*/
    val ProveedorSeleccionadoFiltro = proveedorSeleccionado != null

    /*Para activar el boton de guardar cuando los campos tengan un valor valido*/
    val botonValidoFiltro = remember(nombreProveedor, ProveedorSeleccionadoFiltro) {
        ProveedorSeleccionadoFiltro && nombreProveedor.isNotBlank() /*Que haya proveedor seleccionado y nombre no vacio*/
    }

    Scaffold(
        containerColor = MangoColores.Fondo
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 14.dp,
                        shape = RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp),
                        clip = false
                    )
                    .background(
                        color = MangoColores.Header,
                        shape = RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 18.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Editar Proveedor",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 29.sp,
                        color = MangoColores.TextoPrincipal
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    VerticalDivider(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .height(28.dp),
                        thickness = 1.dp,
                        color = MangoColores.TextoSecundario
                    )

                    Image(
                        painter = painterResource(id = R.drawable.usamangos),
                        contentDescription = "Logo Mangos USA",
                        modifier = Modifier.size(58.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.weight(0.9f))

                Text(
                    text = "Selecciona un proveedor para poder editar sus datos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MangoColores.TextoSecundario,
                    modifier = Modifier.alpha(0.8f)
                )

                Spacer(modifier = Modifier.weight(2f))

                /*Dropdown para seleccionar el proveedor a editar*/
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Proveedor: ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = MangoColores.TextoPrincipal,
                        modifier = Modifier.width(110.dp)
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(
                            onClick = { dropdownExpandido = true },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                proveedorSeleccionado?.nombre ?: "Selecciona un proveedor",
                                modifier = Modifier.weight(1f).alpha(if (ProveedorSeleccionadoFiltro) 1f else 0.5f),
                                fontSize = 16.sp,
                                color = MangoColores.TextoPrincipal
                            )
                            Text("+", fontSize = 16.sp, color = MangoColores.TextoPrincipal)
                        }
                        DropdownMenu(
                            expanded = dropdownExpandido,
                            onDismissRequest = { dropdownExpandido = false }
                        ) {
                            if (proveedores.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No hay proveedores registrados", modifier = Modifier.alpha(0.5f)) },
                                    onClick = { dropdownExpandido = false }
                                )
                            } else {
                                proveedores.forEach { proveedor ->
                                    DropdownMenuItem(
                                        text = { Text(proveedor.nombre) }, /*Aqui se llama la lista de proveedores que falta modificar conforme la bdd*/
                                        onClick = {
                                            viewModel.seleccionarProveedor(proveedor)
                                            dropdownExpandido = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    /*Boton de deseleccionar, este solo visible si hay un proveedor seleccionado*/
                    if (ProveedorSeleccionadoFiltro) {
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {
                            viewModel.limpiarSeleccion()
                        }) {
                            Text("x", fontSize = 18.sp, color = MangoColores.BotonEliminar, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                /*Campo del nombre*/
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Nombre: ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = MangoColores.TextoPrincipal,
                        modifier = Modifier.width(110.dp).alpha(if (ProveedorSeleccionadoFiltro) 1f else 0.4f)
                    )
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
                    Text(
                        "Dirección: ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = MangoColores.TextoPrincipal,
                        modifier = Modifier.width(110.dp).alpha(if (ProveedorSeleccionadoFiltro) 1f else 0.4f)
                    )
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
                                    modifier = Modifier.size(24.dp),
                                    tint = MangoColores.TextoPrincipal
                                )
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(6f))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MangoColores.TextoSecundario.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.weight(1f))

                /*ESto para la fila de botones*/
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    /*Eliminar a la izq*/
                    Button(
                        onClick = {
                            mostrarDialogoEliminar = true
                        },
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MangoColores.BotonEliminar,
                            disabledContainerColor = MangoColores.TextoSecundario.copy(alpha = 0.4f)
                        ),
                        enabled = ProveedorSeleccionadoFiltro && !guardando
                    ) {
                        Text("Eliminar", fontSize = 16.sp, color = MangoColores.Blanco)
                    }

                    /*Cancelar y guardar a la derecha*/
                    Row {
                        OutlinedButton(
                            onClick = onCancelar,
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.height(52.dp)
                        ) {
                            Text("Cancelar", fontSize = 16.sp, color = MangoColores.BotonEliminar)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = {
                                if (guardando) return@Button
                                guardando = true
                                proveedorSeleccionado?.let {
                                    viewModel.actualizarProveedor(
                                        it.copy(
                                            nombre = nombreProveedor,
                                            direccion = direccion
                                        )
                                    )
                                    onGuardar()
                                }
                            },
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MangoColores.BotonGuardar,
                                disabledContainerColor = MangoColores.TextoSecundario.copy(alpha = 0.4f)
                            ),
                            enabled = botonValidoFiltro && !guardando
                        ) {
                            Text("Guardar", fontSize = 16.sp, color = MangoColores.TextoPrincipal)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = {
                if (!guardando) mostrarDialogoEliminar = false
            },
            title = {
                Text("Eliminar proveedor")
            },
            text = {
                Text("¿Seguro que quieres eliminar el proveedor?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (guardando) return@Button

                        guardando = true

                        proveedorSeleccionado?.let { viewModel.eliminarProveedor(it) }
                        onEliminar()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MangoColores.BotonEliminar
                    )
                ) {
                    Text("Eliminar", color = MangoColores.Blanco)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                    },
                    enabled = !guardando
                ) {
                    Text("Cancelar", color = MangoColores.BotonEliminar)
                }
            },
            containerColor = MangoColores.Fondo,
            titleContentColor = MangoColores.TextoPrincipal,
            textContentColor = MangoColores.TextoPrincipal
        )
    }
}