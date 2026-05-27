package com.hectoralmor.mangos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onLimpiarClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp) /*Separar los botones*/
            ) {
/*                FloatingActionButton(  /*ES PARA BORRAR LA LISTA, ESTO ES MIENTRAS SE TRABAJA SOLAMENTE*/
                    onClick = onLimpiarClick,
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ) {
                    Text("Borrar️")
                }
*/
                // Segundo botón (Agregar)
                FloatingActionButton(onClick = onAgregarProductoClick) {
                    Text("+")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(
                text = "Lista de Productos",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(listaProductos) { producto ->
                    PostItCard(producto = producto) /* Aqui llama mi componente de post it*/
                }
            }
        }
    }
}

@Composable
/*Componente para visualizacion de post it*/
fun PostItCard(producto: Producto) {
    /* Primero convierte el nombre del producto (sera en un futuro del proveedor) en un número único que es
       fijo y calcula el residuo con floorMod, este lo usamos en lugar de '%' para asegurar un índice positivo
       válido entre 0 y 5 por el listof que hice arriba de colores*/
    val colorIndex = java.lang.Math.floorMod(producto.nombre.hashCode(), postItColores.size)
    /*Aqui asignamos el color al producto dependiendo de su nombre*/
    val cardColor = postItColores[colorIndex]

    Box(modifier = Modifier
        .aspectRatio(1f)
        .shadow(
            elevation = 6.dp,
            shape = RoundedCornerShape(4.dp),
            ambientColor = Color.Black.copy(alpha = 0.2f),
            spotColor = Color.Black.copy(alpha = 0.3f)
        )
        .background(color = cardColor, shape = RoundedCornerShape(4.dp))
        .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier /*Espacio para simular que es un post it, asi la parte de arriba que se pega*/
                .fillMaxWidth()
                .height(8.dp)
                .background(
                    color = cardColor.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = producto.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF212121)
            )

            Text(
                text = producto.descripcion,
                fontSize = 12.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF424242),
                modifier = Modifier.weight(1f, fill = false)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$${producto.precio}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color(0xFF1A237E),
                modifier = Modifier.align(Alignment.End)
            )

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