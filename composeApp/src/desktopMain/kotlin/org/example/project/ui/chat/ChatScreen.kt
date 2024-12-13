package org.example.project.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import org.example.project.ui.nickname.NickNameViewModel


@Composable
fun ChatScreen(nickNameViewModel: NickNameViewModel, onNavigateBack: () -> Unit) {

    val nickName = nickNameViewModel.nickname.collectAsState().value

    val colorGreyClaro = Color(0xFF3A3C42)
    val colorGreyOscuro = Color(0xFF2E3035)
    val DarkGrayBlue = Color(0xFF383A40)


    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = colorGreyOscuro,
                title = { Text("Nickname: $nickName ", fontStyle = FontStyle.Italic) },
            )
        },
        content = {

            Row(
                modifier = Modifier.fillMaxSize().background(color = colorGreyClaro),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {


                Body(nickName = nickName, onNavigateBack = onNavigateBack)


            }
        }, bottomBar = {

            TextoAbajo(
                nickName = nickName,
                onChage = { nickNameViewModel.onChange(it) },
                color = colorGreyClaro
            )

        }
    )
}


@Composable
fun TextoAbajo(nickName: String, onChage: (String) -> Unit, color: Color) {

    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        backgroundColor = Color(0xFF2E3035),
        content = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = nickName,
                    onValueChange = onChage,
                    modifier = Modifier
                        .weight(1f) // Distribuye espacio proporcionalmente
                        .padding(start = 16.dp, end = 8.dp)
                        .border(
                            width = 0.2.dp,
                            color = Color(0xFF2E3035), // Color del borde
                            shape = RoundedCornerShape(percent = 25)
                        )
                        .background(color = color, shape = RoundedCornerShape(percent = 25)),
                    maxLines = 1,
                    singleLine = true,
                    shape = RoundedCornerShape(percent = 25),
                    colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                    ),
                    textStyle = TextStyle(Color(0xFFEEEEEE))
                )

                Spacer(modifier = Modifier.width(8.dp)) // Añade un espacio entre el TextField y el IconButton

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(percent = 30))
                        .background(color = Color(0xFFE0E0E0))
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }
    )
}

@Composable
fun UserActionColumn(nickName: String, onNavigateBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxHeight()) {
        Text("Hola $nickName", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = onNavigateBack,
            shape = CircleShape,
            modifier = Modifier.size(120.dp)
        ) {
            Text("Volver", style = MaterialTheme.typography.button)
        }
    }
}


@Composable
fun Body(nickName: String, onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserActionColumn(nickName, onNavigateBack)
        Spacer(modifier = Modifier.width(16.dp))
        TablaUsuarios(mutableListOf("", "", "", ""))
    }
}


@Composable
fun TablaUsuarios(lista: MutableList<String>) {

    LazyColumn() {
        items(lista) {
           TarjetaUsuario()
        }
    }
}

@Composable
fun TarjetaUsuario() {

    Card(
        contentColor = contentColorFor(backgroundColor = MaterialTheme.colors.background),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .width(380.dp)
            .height(180.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "gregre",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                    )
                    androidx.compose.material3.Text(
                        text = "ef",
                        maxLines = 2,
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    androidx.compose.material3.Text(
                        text = "htreh€",
                        style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.primary
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    TextButton(
                        onClick = {

                        },
                        enabled = true
                    ) {
                        androidx.compose.material3.Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {

                    }) {
                        androidx.compose.material3.Text("-")
                    }
                    androidx.compose.material3.Text(
                        text = "cantidad.toString()",
                        style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    TextButton(onClick = {
                    }) {
                        androidx.compose.material3.Text("+")
                    }
                }

            }
        }


    }

}


