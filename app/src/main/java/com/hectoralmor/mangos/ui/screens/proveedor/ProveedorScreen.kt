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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hectoralmor.mangos.R
import com.hectoralmor.mangos.ui.theme.MangoColores

@Composable
fun ProveedorScreen(
    onCancelar: () -> Unit,
    onGuardar: () -> Unit,
    viewModel: ProveedorViewModel = viewModel(factory = ProveedorViewModel.Factory)
) {
    var nombreProveedor by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var guardando by remember { mutableStateOf(false) }
    val context = LocalContext.current /*Para poder lanzar el intent de google maps*/

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
                        text = "Agregar Proveedor",
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
                    text = "Favor de rellenar los campos necesarios para poder agregar un proveedor a su lista",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MangoColores.TextoSecundario,
                    modifier = Modifier.alpha(0.8f)
                )

                Spacer(modifier = Modifier.weight(2f))

                /*Campo del nombre del proveedor*/
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Nombre: ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = MangoColores.TextoPrincipal,
                        modifier = Modifier.width(110.dp)
                    )
                    OutlinedTextField(
                        value = nombreProveedor,
                        onValueChange = { input ->
                            val filtro = input.filter { it.isLetterOrDigit() || it == ' ' } /*Solo letras, numeros y espacios*/
                            if (filtro.length > 50) return@OutlinedTextField /*Maximo 50 caracteres*/
                            nombreProveedor = filtro
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true, /*En una sola linea, nada de enter*/
                        shape = RoundedCornerShape(4.dp),
                        placeholder = { Text("Nombre del proveedor", modifier = Modifier.alpha(0.5f)) }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                /*Campo de la direccion, abre maps con la direccion escrita*/
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Dirección: ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = MangoColores.TextoPrincipal,
                        modifier = Modifier.width(110.dp)
                    )
                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { input ->
                            if (input.length > 100) return@OutlinedTextField /*Maximo 100 caracteres*/
                            direccion = input
                        },
                        modifier = Modifier.weight(1f),
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
                                enabled = direccion.isNotBlank() /*Solo si hay algo escrito, si no no esta activado*/
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
                    val botonValidoFiltro = remember(nombreProveedor) {
                        nombreProveedor.isNotBlank() /*Que no este vacio el nombre*/
                    }

                    Button(
                        onClick = {
                            if (guardando) return@Button

                            guardando = true

                            viewModel.guardarProveedor(
                                nombre = nombreProveedor,
                                direccion = direccion
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