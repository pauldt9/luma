package com.example.luma.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(navController: NavController){
    // Variables de estado para almacenar correo y contraseña
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Variables de estado para controlar errores
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    // Variable de estado para controlar el estado de carga
    var isLoading by remember { mutableStateOf(false) }

    // Contexto actual para mostrar mensajes
    val context = LocalContext.current
    // Autenticación de Firebase
    val auth = Firebase.auth

    AppBackground (contentAlignment = Alignment.Center){
        AuthContainer {
            Spacer(modifier = Modifier.height(20.dp))

            ScreenTitle(stringResource(id = R.string.login_title))

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomImput(
                    stringResource(R.string.email_label),
                    email,
                    stringResource(R.string.email_placeholder),
                    {
                        email = it
                        // Limpia el error al escribir nuevamente en el campo de correo
                        if (emailError != null) emailError = null
                    },
                    false,
                    errorMessage = emailError
                )

                CustomImput(
                    stringResource(R.string.password_label),
                    password,
                    stringResource(R.string.password_placeholder),
                    {
                        password = it
                        // Limpia el error al escribir nuevamente en el campo de contraseña
                        if (passwordError != null) passwordError = null
                    },
                    true,
                    errorMessage = passwordError
                )

                Text(
                    text = stringResource(R.string.forgot_password),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable(enabled = !isLoading) {
                            if (email.isNotEmpty()) {
                                // Envía un correo de recuperación de contraseña mediante Firebase
                                auth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Éxito, muestra un mensaje
                                            Toast.makeText(context, "Correo de recuperación enviado", Toast.LENGTH_SHORT).show()
                                        } else {
                                            // En caso de error, muestra un mensaje de error
                                            Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                // Si el campo de correo está vacío, muestra un mensaje de error
                                emailError = "Ingresa tu correo para recuperar la contraseña"
                            }
                        },
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.btn_forgot_pass_text)
                )

            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                // Botón de inicio de sesión
                CustomButton(
                    stringResource(R.string.login_button),
                    colorResource(id = R.color.btn_primary_background),
                    colorResource(id = R.color.btn_primary_text),
                    enabled = !isLoading,
                    isLoading = isLoading
                ) {
                    // Validaciones básicas
                    emailError = if (email.isEmpty()) "El correo es obligatorio" else null
                    passwordError = if (password.isEmpty()) "La contraseña es obligatoria" else null

                    // Si no hay errores, inicia sesión
                    if (emailError == null && passwordError == null) {
                        // Activa el estado de carga
                        isLoading = true
                        // Inicia sesión con Firebase
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                // Desactiva el estado de carga
                                isLoading = false
                                if (task.isSuccessful) {
                                    // Navega a la pantalla principal
                                    navController.navigate("home") {
                                        // Elimina la pantalla de inicio de sesión de la pila de navegación
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    // Obtiene la excepción y muestra un mensaje de error
                                    val exception = task.exception
                                    // Manejo de errores
                                    when (exception) {
                                        // Usuario inexistente
                                        is FirebaseAuthInvalidUserException -> {
                                            emailError = "Usuario no encontrado"
                                        }
                                        // Contraseña o correo incorrectos
                                        is FirebaseAuthInvalidCredentialsException -> {
                                            passwordError = "Contraseña o correo incorrectos"
                                        }
                                        // Otros errores
                                        else -> {
                                            Toast.makeText(context, "Error al iniciar sesión: ${exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                    }
                }

                // Botón para navegar a la pantalla de registro
                CustomButton(
                    stringResource(R.string.register_button),
                    colorResource(id = R.color.btn_secondary_background),
                    colorResource(id = R.color.btn_secondary_text),
                    enabled = !isLoading
                ) {navController.navigate("sign_up")}
            }
        }
    }
}