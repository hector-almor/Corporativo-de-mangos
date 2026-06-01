package com.hectoralmor.mangos.ui.screens.compra

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hectoralmor.mangos.R
import com.hectoralmor.mangos.domain.model.Proveedor
import com.hectoralmor.mangos.ui.theme.MangoColores

@Composable
fun EditarCompraScreen(
    compraId: Long,
    onCancelar: () -> Unit,
    onGuardar: () -> Unit,
    onEliminar: () -> Unit,
    viewModel: CompraViewModel = viewModel(factory = CompraViewModel.Factory)
) {
    val proveedores by viewModel.proveedores.collectAsStateWithLifecycle()
    val compraSeleccionada by viewModel.compraSeleccionada.collectAsStateWithLifecycle()

    /*Carga la compra al entrar a la pantalla*/
    LaunchedEffect(compraId) {
        viewModel.obtenerCompraId(compraId)
    }

    var proveedorSeleccionado by remember { mutableStateOf<Proveedor?>(null) }
    var dropdownExpandido by remember { mutableStateOf(false) }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var guardando by remember { mutableStateOf(false) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    /*Inicializa los campos cuando se carga la compra*/
    LaunchedEffect(compraSeleccionada, proveedores) {
        compraSeleccionada?.let { compra ->
            precio = compra.precio.toString()
            cantidad = compra.cantidad.toInt().toString()
            proveedorSeleccionado = proveedores.find { it.id == compra.proveedorId }
        }
    }

    val total = remember(precio, cantidad) {
        val p = precio.toDoubleOrNull() ?: 0.0 /*Si detecta algo raro, se pone 0.0 en automatico*/
        val c = cantidad.toIntOrNull() ?: 0
        p * c
    }

    /*El boton de guardar se actuva cuando los campos tengan un valor valido*/
    val botonValidoFiltro = remember(proveedorSeleccionado, precio, cantidad) {
        proveedorSeleccionado != null &&
                precio.isNotBlank() && (precio.toDoubleOrNull() ?: 0.0) > 0.0 && /*Que no sea puro 0 precio*/
                cantidad.isNotBlank() && (cantidad.toIntOrNull() ?: 0) > 0 /*Que no sea puro 0 cantidad*/
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
                        shape = RoundedCornerShape(
                            bottomStart = 22.dp,
                            bottomEnd = 22.dp
                        ),
                        clip = false
                    )
                    .background(
                        color = MangoColores.Header,
                        shape = RoundedCornerShape(
                            bottomStart = 22.dp,
                            bottomEnd = 22.dp
                        )
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
                        text = "Editar Compra",
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
                    text = "Edita los datos de la compra seleccionada",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MangoColores.TextoSecundario,
                    modifier = Modifier.alpha(0.8f)
                )

                Spacer(modifier = Modifier.weight(2f))

                /*Campo del proveedor*/
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
                                text = proveedorSeleccionado?.nombre ?: "Selecciona un proveedor",
                                modifier = Modifier.weight(1f).alpha(if (proveedorSeleccionado != null) 1f else 0.5f),
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
                                        text = { Text(proveedor.nombre) },
                                        onClick = {
                                            proveedorSeleccionado = proveedor
                                            dropdownExpandido = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                /*Campo del precio*/
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Precio: ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = MangoColores.TextoPrincipal,
                        modifier = Modifier.width(110.dp)
                    )
                    OutlinedTextField(
                        value = precio,
                        onValueChange = { input ->
                            val filtroCaracteres = input.filter { it.isDigit() || it == '.' } /*Solo permite dígitos y un punto decimal*/
                            if (filtroCaracteres.count { it == '.' } > 1) return@OutlinedTextField /*No deja poner otro decimal*/
                            if (filtroCaracteres.substringAfter('.', "").length > 2) return@OutlinedTextField /*Maximo 2 decimales*/
                            if (filtroCaracteres.substringBefore('.').length > 6) return@OutlinedTextField /*Maximo 6 dígitos enteros*/
                            precio = filtroCaracteres
                        },
                        modifier = Modifier.width(120.dp),
                        prefix = { Text("$") }, /*Para que este dentro del texto y no se pueda mover*/
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), /*Para que te abra el tipo de teclado de numeritos*/
                        singleLine = true, /*En una sola linea, nada de enter*/
                        shape = RoundedCornerShape(4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                /*Campo de la cantidad*/
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Cantidad:  ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = MangoColores.TextoPrincipal,
                        modifier = Modifier.width(110.dp)
                    )
                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = { input ->
                            val filtro = input.filter { it.isDigit() } /*Solo dígitos, nada de letras ni símbolos*/
                            if (filtro.length > 6) return@OutlinedTextField /*Maximo 6 dígitos*/
                            cantidad = filtro
                        },
                        modifier = Modifier.width(110.dp).height(50.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(3f))

                HorizontalDivider(thickness = 1.dp, color = MangoColores.TextoSecundario.copy(alpha = 0.5f))

                Spacer(modifier = Modifier.weight(1f))

                /*Campo del Total, se saca automatico con precio y cantidad*/
                Text(
                    text = "Total: $${"%,.2f".format(total)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MangoColores.Guava
                )

                Spacer(modifier = Modifier.height(12.dp))

                /*Campo de la fecha, muestra la fecha original de la compra*/
                compraSeleccionada?.let {
                    Text(
                        text = "Fecha: ${it.fecha}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MangoColores.TextoPrincipal
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            mostrarDialogoEliminar = true
                        },
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MangoColores.BotonEliminar,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.4f)
                        ),
                        enabled = !guardando
                    ) {
                        Text("Eliminar", fontSize = 16.sp, color = MangoColores.Blanco)
                    }

                    Row {
                        OutlinedButton(
                            onClick = {
                                if (!guardando) onCancelar()
                            },
                            enabled = !guardando,
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.height(52.dp)
                        ) {
                            Text("Cancelar", fontSize = 16.sp, color = MangoColores.BotonEliminar)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = {
                                if (guardando) return@Button

                                val prov = proveedorSeleccionado ?: return@Button
                                val compraActual = compraSeleccionada ?: return@Button

                                guardando = true

                                viewModel.editarCompra(
                                    compraActual.copy(
                                        proveedorId = prov.id,
                                        nombre = prov.nombre,
                                        cantidad = cantidad.toDouble(),
                                        precio = precio.toDouble()
                                    )
                                )

                                onGuardar()
                            },
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MangoColores.BotonGuardar,
                                disabledContainerColor = Color.Gray.copy(alpha = 0.4f)
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
                Text("Eliminar compra")
            },
            text = {
                Text("¿Seguro que quieres eliminar la compra?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (guardando) return@Button

                        guardando = true

                        compraSeleccionada?.let {
                            viewModel.eliminarCompra(it)
                        }

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