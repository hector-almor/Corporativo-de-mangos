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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CompraScreen(
    onCancelar: () -> Unit,
    onGuardar: () -> Unit,
    viewModel: CompraViewModel = viewModel(factory = CompraViewModel.Factory)
) {
    val proveedores by viewModel.proveedores.collectAsStateWithLifecycle()

    var proveedorSeleccionado by remember { mutableStateOf<Proveedor?>(null) }
    var dropdownExpandido by remember { mutableStateOf(false) }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var guardando by remember { mutableStateOf(false) }
    val fecha = remember { /* Fecha automática, toma el momento cuando abres la pantalla*/
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    }
    val total = remember(precio, cantidad) {
        val p = precio.toDoubleOrNull() ?: 0.0 /*Si detecta algo raro, se pone 0.0 en automatico*/
        val c = cantidad.toIntOrNull() ?: 0
        p * c
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
                        text = "Agregar Compra",
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
                    text = "Favor de rellenar los campos necesarios para poder agregar una compra a su lista",
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
                    Box(modifier = Modifier.fillMaxWidth()) {
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
                        prefix = { Text("$", color = MangoColores.TextoPrincipal) }, /*Para que este dentro del texto y no se pueda mover*/
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), /*Para que te abra el tipo de teclado de numeritos*/
                        singleLine = true, /*En una sola linea, nada de enter*/
                        shape = RoundedCornerShape(4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                /*Campo de la cantidad*/
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Cantidad :  ",
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
                    Text("  Ton", fontSize = 16.sp, color = MangoColores.TextoPrincipal)
                }

                Spacer(modifier = Modifier.weight(3f))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MangoColores.TextoSecundario.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.weight(1f))

                /*Campo del Total, se saca automatico con precio y cantidad*/
                Text(
                    text = "Total: $${"%,.2f".format(total)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MangoColores.Guava
                )

                Spacer(modifier = Modifier.height(12.dp))

                /*Campo de la fecha*/
                Text(
                    text = "Fecha: $fecha",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MangoColores.TextoPrincipal
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
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

                    /*Para activar el boton de guardar cuando los campos tengan un valor valido*/
                    val botonValidoFiltro = remember(proveedorSeleccionado, precio, cantidad) {
                        proveedorSeleccionado != null &&
                                precio.isNotBlank() && (precio.toDoubleOrNull() ?: 0.0) > 0.0 && /*Que no sea puro 0 precio*/
                                cantidad.isNotBlank() && (cantidad.toIntOrNull() ?: 0) > 0 /*Que no sea puro 0 cantidad*/
                    }

                    Button(
                        onClick = {
                            if (guardando) return@Button

                            val prov = proveedorSeleccionado ?: return@Button
                            guardando = true

                            viewModel.guardarCompra(
                                proveedorId = prov.id,
                                nombreProveedor = prov.nombre,
                                cantidad = cantidad.toDouble(),
                                precio = precio.toDouble()
                            )
                            onGuardar()
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

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}