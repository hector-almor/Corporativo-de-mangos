package com.hectoralmor.mangos.ui.screens.principal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hectoralmor.mangos.R
import com.hectoralmor.mangos.domain.model.Compra
import com.hectoralmor.mangos.ui.theme.MangoColores

private val postItColores = MangoColores.PostItColores

@Composable
fun PrincipalScreen(
    onAgregarProveedorScreen: () -> Unit,
    onAgregarCompraScreen: () -> Unit,
    onEditarProveedorScreen: () -> Unit,
    onEditarCompraScreen: (Long) -> Unit,
    viewModel: PrincipalViewModel = viewModel(factory = PrincipalViewModel.Factory)
) {
    var menuExpandido by remember { mutableStateOf(false) } /*Para el menuExpandido del floatingActionButton*/

    //LLAMA AL VIEWMODEL PARA OBTENER LOS DATOS, CADA SCREEN TIENE SU PROPIO VIEWMODEL CON EL QUE INTERACTÚA PARA
    //CUALQUER OPERACIÓN QUE TENGA QUE VER CON LA PERSISTENCIA DE DATOS
    val compras by viewModel.compras.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MangoColores.Fondo
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
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
                            text = "Lista de Compras",
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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        end = 12.dp,
                        top = 18.dp,
                        bottom = padding.calculateBottomPadding() + 90.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(compras) { index, compra ->
                        PostItCard(
                            numeroCompra = index + 1,
                            compra = compra,
                            onEditarClick = { onEditarCompraScreen(compra.id) }
                        )
                    }
                }
            }

            /*Fila inferior con ambos botones flotantes alineados al mismo nivel*/
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                /*Boton de editar, a la izquierda, va directo a la pantalla de editar proveedor*/
                FloatingActionButton(
                    onClick = { onEditarProveedorScreen() },
                    containerColor = MangoColores.BotonGuardar
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.editarproveedor),
                        contentDescription = "Icono de editar proveedor",
                        modifier = Modifier.size(24.dp),
                        tint = MangoColores.TextoPrincipal
                    )
                }

                /*Boton de agregar, a la derecha*/
                Column(horizontalAlignment = Alignment.End) {

                    if (menuExpandido) {

                        FloatingActionButton(
                            onClick = {
                                menuExpandido = false
                                onAgregarProveedorScreen()
                            },
                            modifier = Modifier.width(170.dp),
                            containerColor = MangoColores.BotonPrincipal
                        ) {
                            Text(
                                "Agregar Proveedor",
                                modifier = Modifier.padding(horizontal = 8.dp),
                                color = MangoColores.TextoPrincipal
                            )
                        } /*Si el menu esta expandido se muestra el boton de agregar proveedor*/

                        Spacer(modifier = Modifier.height(8.dp))

                        FloatingActionButton(
                            onClick = {
                                menuExpandido = false
                                onAgregarCompraScreen()
                            },
                            modifier = Modifier.width(170.dp),
                            containerColor = MangoColores.BotonEliminar
                        ) {
                            Text(
                                "Agregar Compra",
                                modifier = Modifier.padding(horizontal = 8.dp),
                                color = MangoColores.Blanco
                            )
                        } /*Si el menu esta expandido se muestra el boton de agregar compra*/

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    FloatingActionButton(
                        onClick = { menuExpandido = !menuExpandido },
                        containerColor = MangoColores.BotonOscuro
                    ) {
                        Text(
                            if (menuExpandido) "x" else "+",
                            fontSize = 20.sp,
                            color = MangoColores.Crema
                        )
                    }
                    /*Si el menu no esta expandido se muestra el boton de +, de lo contrario se muestra la x*/
                }
            }
        }
    }
}

/*Componente para visualizacion de post it*/
@Composable
fun PostItCard(numeroCompra: Int, compra: Compra, onEditarClick: () -> Unit) {
    /* Convierte el nombre del proveedor en un número único y calcula el residuo con floorMod
       para asegurar un índice positivo válido entre 0 y 5 */
    val colorIndex = Math.floorMod(compra.nombre.hashCode(), postItColores.size)

    /*Aqui asignamos el color a la compra dependiendo del nombre del proveedor*/
    val cardColor = postItColores[colorIndex]

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .background(
                color = cardColor.copy(alpha = 0.92f),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Compra #$numeroCompra",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MangoColores.TextoSecundario,
                modifier = Modifier.alpha(0.85f)
            )
            Text(
                text = compra.nombre, /*Proveedor*/
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MangoColores.TextoPrincipal
            )
            Text(
                text = "${compra.cantidad} Kg",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MangoColores.TextoCampo
            )
            Text(
                text = "$${compra.precio} / Kg", /*Precio*/
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MangoColores.TextoCampo,
            )
            Text(
                text = compra.fecha, /*Fecha*/
                fontSize = 13.sp,
                maxLines = 1,
                color = MangoColores.TextoSecundario
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "$${"%,.2f".format(compra.total)}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = MangoColores.Guava
                    )
                }
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = MangoColores.BotonTropical,
                            shape = RoundedCornerShape(14.dp)
                        )
                ) {
                    TextButton(
                        onClick = onEditarClick
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.editarcompra),
                            contentDescription = "Editar Compra",
                            tint = MangoColores.Blanco,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}