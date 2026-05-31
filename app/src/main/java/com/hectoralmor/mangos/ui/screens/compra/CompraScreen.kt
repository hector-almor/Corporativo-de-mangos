package com.hectoralmor.mangos.ui.screens.compra

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CompraScreen(
    onCancelar: () -> Unit,
    onGuardar: () -> Unit
) {
    val proveedores = listOf("Proveedor A", "Proveedor B", "Proveedor C") /*De prueba, manda a llamar los proveedores de la bdd y pon una excepcion por si no hay ninguno*/
    var proveedorSeleccionado by remember { mutableStateOf("Selecciona un proveedor") }
    var dropdownExpandido by remember { mutableStateOf(false) }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    val fecha = remember { /* Fecha automática, toma el momento cuando abres la pantalla*/
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    }
    val total = remember(precio, cantidad) { /*Aqui sacas el total con los datos de la bdd*/
        val p = precio.toDoubleOrNull() ?: 0.0 /*Si detecta algo raro, se pone 0.0 en automatico*/
        val c = cantidad.toIntOrNull() ?: 0
        p * c
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
                Text(
                    text = "Agregar Compra",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                VerticalDivider(
                    modifier = Modifier
                        .padding(horizontal = 18.dp)
                        .height(28.dp),
                    thickness = 1.dp
                )
                Text(
                    text = "Mangos USA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.weight(0.9f))

            Text(
                text = "Favor de rellenar los campos necesarios para poder agregar una compra a su lista",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.alpha(0.5f)
            )

            Spacer(modifier = Modifier.weight(2f))

            Row(verticalAlignment = Alignment.CenterVertically /*CAmpo del proveedor*/) {
                Text("Proveedor: ", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, modifier = Modifier.width(110.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { dropdownExpandido = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(proveedorSeleccionado, modifier = Modifier.weight(1f).alpha(0.5f), fontSize = 16.sp) /*Que muestre el proveedor seleccionado*/
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
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            /*Campo del precio*/
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Precio: ", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, modifier = Modifier.width(110.dp))
                OutlinedTextField(value = precio, onValueChange = { input ->
                    val filtroCaracteres = input.filter { it.isDigit() || it == '.' } /*Solo permite dígitos y un punto decimal*/
                    val puntoDecFiltro = filtroCaracteres.count { it == '.' } /*Solo un punto decimal*/
                    if (puntoDecFiltro > 1) return@OutlinedTextField /*No deja poner otro decimal*/

                    val decimalesFiltro = filtroCaracteres.substringAfter('.', "")
                    if (decimalesFiltro.length > 2) return@OutlinedTextField /*MAximo 3 decimales*/

                    val cantEnterosFiltro = filtroCaracteres.substringBefore('.')
                    if (cantEnterosFiltro.length > 6) return@OutlinedTextField /*Maximo 6 dígitos enteros */
                    precio = filtroCaracteres
                },
                    modifier = Modifier.width(120.dp),
                    prefix = { Text("$") }, /*Para que este dentro del texto y no se pueda mover*/
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), /*Para que te abra el tipo de teclado de numeritos*/
                    singleLine = true, /*En una sola lina, nada de enter*/
                    shape = RoundedCornerShape(4.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            /*Campo de la cantidad*/
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Cantidad:  ", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, modifier = Modifier.width(110.dp))
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { input ->
                        val filtro = input.filter { it.isDigit() } /*Solo dígitos, nada de letras ni símbolos*/
                        if (filtro.length > 6) return@OutlinedTextField /*Maximo 10 dígitos*/
                        cantidad = filtro
                    },
                    modifier = Modifier.width(110.dp)
                        .height(50.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(4.dp),
                )
            }

            Spacer(modifier = Modifier.weight(3f))

            HorizontalDivider(thickness = 1.dp)

            Spacer(modifier = Modifier.weight(1f))

            /*Campo del Total, este se saca automatico, agarra el valor de total que definimos arriba*/
            Text(
                text = "Total: $${"%.2f".format(total)}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF1404)
            )

            Spacer(modifier = Modifier.height(12.dp))

            /*Campo de la fecha*/
            Text(
                text = "Fecha: $fecha",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onCancelar,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.height(52.dp)
                ) {
                    Text("Cancelar", fontSize = 16.sp, color = Color.Red)
                }

                Spacer(modifier = Modifier.width(12.dp))

                /*Para activar el boton de guardar cuando los campos tengan un valor valido*/
                val botonValidoFiltro = remember(proveedorSeleccionado, precio, cantidad) {
                    proveedorSeleccionado != "Selecciona un proveedor" &&
                            precio.isNotBlank() && (precio.toDoubleOrNull() ?: 0.0) > 0.0 && /*Que no sea puro 0 precio*/
                            cantidad.isNotBlank() && (cantidad.toIntOrNull() ?: 0) > 0 /*Que no sea puro 0 cantidad*/
                }

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

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}