package com.example.luma.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AppCard
import com.example.luma.components.CustomInput
import com.example.luma.components.MainScaffold
import com.example.luma.components.ScreenSubtitle
import com.example.luma.components.ScreenTitle
import com.example.luma.model.ChatMessage
import com.example.luma.viewModel.AiViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AiScreen(navController: NavController) {
    var userPrompt by remember { mutableStateOf("") }

    // Obtiene el usuario actual
    val currentUser = FirebaseAuth.getInstance().currentUser
    // Crea el estado para el LazyList
    val lazyListState = rememberLazyListState()

    // Instancia del ViewModel
    val aiViewModel: AiViewModel = viewModel()

    // Obtiene los mensajes del ViewModel
    val messages = aiViewModel.messages

    // Obtiene los chats del ViewModel
    val chats = aiViewModel.chats

    // Detecta cuando el LazyList llega al final
    val isNearBottom by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= (messages.size - 2)
        }
    }

    // Carga los chats del usuario actual
    LaunchedEffect(Unit) {
        val userId = currentUser?.uid ?: return@LaunchedEffect
        aiViewModel.loadChats(userId)
    }

    // Selecciona el primer chat si no hay ninguno
    LaunchedEffect(chats.size) {
        if (aiViewModel.currentChatId == null && chats.isNotEmpty()) {
            aiViewModel.selectChat(chats.first().id)
        }
    }

    // Scroll al final del LazyList cuando hay nuevos mensajes
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty() && isNearBottom) {
            lazyListState.animateScrollToItem(messages.size - 1)
        }
    }

    // Pantalla principal
    MainScaffold(selectedItem = "ai", onBottomItemClick = { route ->
        // Navega a la pantalla seleccionada
        navController.navigate(route) {
            launchSingleTop = true
            // Guarda el estado de la pantalla
            restoreState = true

            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
        }
    }) {

        Column(modifier = Modifier.fillMaxSize()) {
            ScreenTitle(stringResource(R.string.ai_title))
            ScreenSubtitle(stringResource(R.string.ai_subtitle))

            Spacer(modifier = Modifier.height(25.dp))

            // Lista de chats
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                item {
                    AppCard(
                        modifier = Modifier.clickable {
                            aiViewModel.newChat()
                        },
                        containerColor = colorResource(R.color.selected_item_circle),
                        containerBorder = colorResource(R.color.container_border),
                        contentPadding = PaddingValues(
                            horizontal = 14.dp,
                            vertical = 8.dp
                        )
                    ) {
                        Text(
                            text = "+ Nuevo",
                            color = colorResource(R.color.text_color),
                            fontSize = 14.sp
                        )
                    }
                }

                // Recorre los chats
                items(chats) { chat ->
                    // Crea un botón para cada chat
                    val isSelected = chat.id == aiViewModel.currentChatId

                    // Botón de chat
                    AppCard(
                        modifier = Modifier.clickable {
                            aiViewModel.selectChat(chat.id)
                        },
                        containerColor = if (isSelected) {
                            colorResource(R.color.selected_item_circle)
                        } else {
                            colorResource(R.color.textfield_bg_col)
                        },
                        containerBorder = colorResource(R.color.container_border),
                        contentPadding = PaddingValues(
                            horizontal = 14.dp,
                            vertical = 8.dp
                        )
                    ) {
                        Text(
                            text = chat.title,
                            color = colorResource(R.color.text_color),
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(45.dp))

            Text(
                stringResource(R.string.ai_prompt_label),
                color = colorResource(R.color.text_color),
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            AppCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                containerColor = colorResource(R.color.textfield_bg_col),
                containerBorder = colorResource(R.color.textfield_border_col)
            ) {
                // Lista de mensajes
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(messages) { MessageBubble(it) }
                }
            }

            CustomInput(
                label = "",
                value = userPrompt,
                placeholder = stringResource(R.string.prompt_placeholder),
                onValueChange = { userPrompt = it },
                inputHeight = 60.dp,
                trailingContent = {
                    // Botón de envío
                    IconButton(onClick = {
                        // Si el campo de entrada está vacío, no hace nada
                        if (userPrompt.isBlank()) return@IconButton

                        // Si el usuario no está autenticado, no hace nada
                        val userId = currentUser?.uid ?: return@IconButton

                        // Pregunta del usuario
                        val question = userPrompt

                        // Limpia el campo de entrada
                        userPrompt = ""

                        // Envia la pregunta al ViewModel
                        aiViewModel.sendMessage(userId, question)
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enviar",
                            tint = colorResource(R.color.text_color)
                        )
                    }
                }
            )
        }
    }
}

// Crea la burbuja de los mensajes
@Composable
private fun MessageBubble(message: ChatMessage){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.user){
            Arrangement.End // El mensaje del usuario se posiciona en la derecha
        } else {
            Arrangement.Start // El mensaje de la IA se posiciona en la izquierda
        }
    ) {
        // Contenedor del mensaje
        AppCard(
            modifier = Modifier.fillMaxWidth(0.78f),
            contentPadding = PaddingValues(12.dp),
            containerColor = if (message.user) {
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