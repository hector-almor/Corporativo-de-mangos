package com.hectoralmor.mangos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hectoralmor.mangos.R
import com.hectoralmor.mangos.data.Producto

private val postItColores = listOf( /* La lista de los colores*/
    Color(0xFFFFF176),
    Color(0xFF80DEEA),
    Color(0xFFA5D6A7),
    Color(0xFFFFCC80),
    Color(0xFFF48FB1),
    Color(0xFFCE93D8),
)

@Composable
fun PrincipalScreen(
    listaProductos: List<Producto>,
    onAgregarProductoClick: () -> Unit,
    onLimpiarClick: () -> Unit,
    onAgregarProveedorScreen: () -> Unit,
    onAgregarCompraScreen: () -> Unit,
    onEditarProveedorScreen: () -> Unit,
    onEditarCompraScreen: () -> Unit /*Nuevo parametro para editar compra*/
) {
    var menuExpandido by remember { mutableStateOf(false) } /*Para el menuExpandido del floatingActionButton*/

    Scaffold { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Lista de Compras", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    VerticalDivider(
                        modifier = Modifier.padding(horizontal = 18.dp).height(28.dp),
                        thickness = 1.dp
                    )
                    Text(text = "Mangos USA", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listaProductos) { producto ->
                        PostItCard(
                            producto = producto,
                            onEditarClick = { onEditarCompraScreen() } /*Aqui ira la logica de editar el post it*/
                        ) /* Aqui llama mi componente de post it*/
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
                    onClick = { onEditarProveedorScreen() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.editarproveedor),
                        contentDescription = "Icono de editar proveedor",
                        modifier = Modifier.size(24.dp)
                    )
                }

                /*Boton de agregar, a la derecha*/
                Column(horizontalAlignment = Alignment.End) {
                    if (menuExpandido) {
                        FloatingActionButton(onClick = {
                            menuExpandido = false
                            onAgregarProveedorScreen()
                            //onLimpiarClick()
                        }, modifier = Modifier.width(150.dp)) {
                            Text("Agregar Proveedor", modifier = Modifier.padding(horizontal = 8.dp))
                        } /*Si el menu esta expandidio se muestra el boton de agregar proveedor*/

                        Spacer(modifier = Modifier.height(8.dp))

                        FloatingActionButton(onClick = {
                            menuExpandido = false
                            onAgregarCompraScreen()
                            onAgregarProductoClick()
                        }, modifier = Modifier.width(150.dp)) {
                            Text("Agregar Compra", modifier = Modifier.padding(horizontal = 8.dp))
                        } /*Si el menu esta expandidio se muestra el boton de agregar compra*/

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    FloatingActionButton(
                        onClick = { menuExpandido = !menuExpandido }
                    ) {
                        Text(if (menuExpandido) "x" else "+", fontSize = 20.sp)
                    }
                    /*Si el menu no esta expandido se muestra el boton de +, de lo contrario se muestra la x*/
                }
            }
        }
    }
}

@Composable
        /*Componente para visualizacion de post it*/
fun PostItCard(producto: Producto, onEditarClick: () -> Unit) {
    /* Primero convierte el nombre del producto (sera en un futuro del proveedor) en un número único que es
       fijo y calcula el residuo con floorMod, este lo usamos en lugar de usa rporcentaje  para asegurar un índice positivo
       válido entre 0 y 5 por el listof que hice arriba de colores*/
    val colorIndex = java.lang.Math.floorMod(producto.nombre.hashCode(), postItColores.size)
    /*Aqui asignamos el color al producto dependiendo de su nombre*/
    val cardColor = postItColores[colorIndex]

    Box(modifier = Modifier
        .aspectRatio(1f)
        .shadow(elevation = 6.dp, shape = RoundedCornerShape(4.dp))
        .background(color = cardColor, shape = RoundedCornerShape(4.dp))
        .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = producto.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF212121)
            )

            Text(
                text = "Proveedor A", /*Proveedor*/
                fontSize = 11.sp,
                maxLines = 1,
                color = Color(0xFF424242)
            )

            Text(
                text = producto.descripcion, /*Cantidad*/
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF424242)
            )

            Text(
                text = "$${producto.precio}", /*Precio*/
                fontSize = 12.sp,
                maxLines = 1,
                color = Color(0xFF424242)
            )

            Text(
                text = "12/05/2025", /*Aqui ira producto.fecha cuando este en la bdd*/
                fontSize = 11.sp,
                maxLines = 1,
                color = Color(0xFF424242)
            )

            Spacer(modifier = Modifier.weight(1f))

            /*Fila inferior con icono de editar a la izquierda y precio a la derecha*/
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                /*Icono de lapiz para editar, luego se cambia a un Icon real*/
                TextButton(
                    onClick = onEditarClick,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(color = Color.Black, shape = RoundedCornerShape(1.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.editarcompra),
                            contentDescription = "Icono de editar compra",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = "$${producto.precio}", /*Total (O directamente lo sacas y mandas a imprimir una funcion aqui)*/
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color(0xFF1A237E)
                )
            }

            /* CAMBIAR A ESTO CON LAS MODIFICACIONES DEBIDAS DE LA BASE DE DATOS
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = producto.proveedor, /*Proveedor*/
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF212121)
            )

            Text(
                text = producto.cantidad, /*Cantidad*/
                fontSize = 12.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF424242),
                modifier = Modifier.weight(1f, fill = false)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$${producto.precio}", /*Precio*/
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color(0xFF1A237E),
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$${producto.fecha}", /*Fecha*/
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color(0xFF1A237E),
                modifier = Modifier.align(Alignment.End))

                Text(text = "$${producto.total}", /*Total (O directamente lo sacas y mandas a imprimir una funcion aqui)*/
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color(0xFF1A237E),
                modifier = Modifier.align(Alignment.End))
                }
             */
        }
    }
}