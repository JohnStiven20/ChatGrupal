package org.example.project.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun HomeScreen(onNavigateToDetails: () -> Unit, onNavigateToSettings: () -> Unit) {

    var nickName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Introduce un Nick")

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier,
            onValueChange = { nickName = it },
            value = nickName,
            maxLines = 1,
            singleLine = true,

        )

        Button(onClick = onNavigateToDetails) {
            Text("Go to Details")
        }
        Button(onClick = onNavigateToSettings) {
            Text("Go to Settings")
        }

    }
}