package com.example.luma.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AppBackground
import com.example.luma.components.AuthContainer
import com.example.luma.components.CustomButton
import com.example.luma.components.CustomImput
import com.example.luma.components.ScreenTitle

@Composable
fun SignUpScreen(navController: NavController){
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    AppBackground(contentAlignment = Alignment.Center) {
        // Contenedor
        AuthContainer {
            Spacer(modifier = Modifier.height(20.dp))

            ScreenTitle(stringResource(id = R.string.register_title))

            Spacer(modifier = Modifier.height(20.dp))

            // Inputs
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomImput(
                    stringResource(R.string.name_label),
                    name,
                    stringResource(R.string.name_placeholder),
                    {name = it},
                    false
                )

                // Correo
                CustomImput(
                    stringResource(R.string.email_label),
                    email,
                    stringResource(R.string.email_placeholder),
                    {email = it}, // Guarda el valor hacia el estado
                    false
                )

                // Contraseña
                CustomImput(
                    stringResource(R.string.password_label),
                    password,
                    stringResource(R.string.password_placeholder),
                    {password = it},
                    true
                )

                // Confirmar contraseña
                CustomImput(
                    stringResource(R.string.confirm_password_label),
                    confirmPassword,
                    stringResource(R.string.password_placeholder),
                    {confirmPassword = it},
                    true
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botones
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                CustomButton(
                    stringResource(R.string.register_title),
                    colorResource(id = R.color.btn_primary_background),
                    colorResource(id = R.color.btn_primary_text)
                ) { navController.navigate("home") }

                CustomButton(
                    stringResource(R.string.already_have_account),
                    colorResource(id = R.color.btn_secondary_background),
                    colorResource(id = R.color.btn_secondary_text)
                ) {navController.navigate("login")}
            }
        }

    }

}