package org.example.project.ui.nickname

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun NickNameScreen(onNavigateToSettings: () -> Unit, nickNameViewModel: NickNameViewModel) {

    val nickName = nickNameViewModel.nickname.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().background(color =  Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Introduce un Nickname")

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = nickName,
            onValueChange = { nickNameViewModel.onChange(nickName = it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp).border(1.5.dp,
                    MaterialTheme.colorScheme.onBackground,
                    RoundedCornerShape(percent = 25)
                ).clip(RoundedCornerShape(percent = 25)).background(color = Color.White),
            maxLines = 1,
            singleLine = true,
            label = { Text("Nickname") },
            shape = RoundedCornerShape(percent = 25)
        )

        Spacer(modifier = Modifier.height(10.dp))


        Button(onClick = onNavigateToSettings, shape = CircleShape) {
            Text("Connectarse")
        }

    }
}