package com.example.luma.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.luma.components.AppBackground
import com.example.luma.components.BackButton
import com.example.luma.components.CustomInput
import com.example.luma.components.ScreenTitle
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.luma.R
import com.example.luma.components.CustomButton

@Composable
fun ProfileScreen(navController: NavController){
    // TODO: Mostrar el nombre del usuario y email. El email NO se edita
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    AppBackground {
        Column {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 65.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(
                    onClick = {navController.popBackStack()}
                )

                ScreenTitle(stringResource(id = R.string.profile_title))
            }

            // Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Icono usuario
                Box(
                    modifier = Modifier
                        .size(85.dp)
                        .background(
                            color = colorResource(id = R.color.btn_action_background),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = colorResource(id = R.color.btn_action_text),
                        modifier = Modifier.size(65.dp)
                    )
                }

                CustomInput(
                    label = stringResource(id = R.string.name_label),
                    value = userName,
                    placeholder = "",
                    onValueChange = {userName = it},
                    inputHeight = 57.dp
                )

                CustomInput(
                    label = stringResource(id = R.string.email_label),
                    value = email,
                    placeholder = "",
                    onValueChange = {},
                    readOnly = true,
                    inputHeight = 57.dp,
                )

                Spacer(modifier = Modifier.height(40.dp))

                CustomButton(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    text = stringResource(id = R.string.save_btn),
                    bgColor = colorResource(id = R.color.btn_action_background),
                    fontColor = colorResource(id = R.color.btn_action_text),
                    onClick = {} // TODO: Guardar el nuevo nombre del usuario
                )

                CustomButton(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    text = stringResource(id = R.string.log_out_btn),
                    bgColor = colorResource(id = R.color.log_out_btn_bg),
                    fontColor = colorResource(id = R.color.btn_action_text),
                    onClick = {} // TODO: Cerrar sesion del usuario
                )
            }
        }
    }
}

