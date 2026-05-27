package com.example.luma.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AppCard
import com.example.luma.components.MainScaffold
import com.example.luma.components.ScreenSubtitle
import com.example.luma.components.ScreenTitle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.luma.components.CustomInput
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.mutableStateListOf
import com.example.luma.model.ChatMessage

@Composable
fun AiScreen(navController: NavController){
    // Estado del prompt del usuario
    var userPrompt by remember { mutableStateOf("") }

    // Almacena los mensajes en una lista
//    val messages = remember {
//        mutableStateListOf<ChatMessage>()
//    }

    // Mensajes de prueba
    val messages = listOf(
        ChatMessage("Hola", true),
        ChatMessage("Hola, ¿en qué puedo ayudarte?", false)
    )

    MainScaffold(
        selectedItem = "ai",
        onBottomItemClick = { route ->
            navController.navigate(route)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header
            ScreenTitle(stringResource(id = R.string.ai_title))
            ScreenSubtitle(stringResource(id = R.string.ai_subtitle))

            Spacer(modifier = Modifier.height(65.dp))

            // Body
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(id = R.string.ai_prompt_label),
                    color = colorResource(id = R.color.text_color),
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Aqui va a estar la conversacion con la IA
                AppCard (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(370.dp),
                    containerColor = colorResource(id = R.color.textfield_bg_col),
                    containerBorder = colorResource(id = R.color.textfield_border_col)
                ){
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Recorre la lista y dibuja el contenedor de cada mensaje
                        items(messages){ message ->
                            MessageBubble(message = message)
                        }
                    }
                }

                // Aqui el usuario ingresa el prompt
                CustomInput(
                    label = "",
                    value = userPrompt,
                    placeholder = stringResource(id = R.string.prompt_placeholder),
                    onValueChange = { userPrompt = it },
                    inputHeight = 60.dp,
                    trailingContent = {
                        IconButton(
                            onClick = {
                                // TODO: Enviar prompt
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Enviar",
                                tint = colorResource(id = R.color.text_color)
                            )
                        }
                    }
                )
            }
        }
    }
}

// Crea la burbuja de los mensajes
@Composable
private fun MessageBubble(message: ChatMessage){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser){
            Arrangement.End // El mensaje del usuario se posiciona en la derecha
        } else {
            Arrangement.Start // El mensaje de la IA se posiciona en la izquierda
        }
    ) {
        // Contenedor del mensaje
        AppCard(
            modifier = Modifier.fillMaxWidth(0.78f),
            contentPadding = PaddingValues(12.dp),
            containerColor = if (message.isUser) {
                colorResource(id = R.color.user_msg_col)
            } else {
                colorResource(id = R.color.ai_msg_col)
            },
            containerBorder = colorResource(id = R.color.container_border)
        ) {
            // Mensaje
            Text(
                text = message.text,
                color = colorResource(id = R.color.text_color),
                fontSize = 14.sp
            )
        }
    }
}