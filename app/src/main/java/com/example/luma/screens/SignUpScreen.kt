package com.example.luma.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AppBackground
import com.example.luma.components.AuthContainer
import com.example.luma.components.CustomButton
import com.example.luma.components.CustomInput
import com.example.luma.components.ScreenTitle
import com.example.luma.model.User
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun SignUpScreen(navController: NavController){
    // Variables de estado para almacenar datos del usuario
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Variables de estado para controlar errores
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Contexto actual para mostrar mensajes
    val context = LocalContext.current
    // Autenticación de Firebase
    val auth = Firebase.auth
    // Base de datos Firestore
    val db = Firebase.firestore

    AppBackground(contentAlignment = Alignment.Center) {
        // Contenedor del formulario de registro
        AuthContainer {
            Spacer(modifier = Modifier.height(20.dp))

            ScreenTitle(stringResource(id = R.string.register_title))

            Spacer(modifier = Modifier.height(20.dp))

            // Contenedor de campos de entrada
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Campos de entrada para el nombre, correo y contraseña
                CustomInput(
                    stringResource(R.string.name_label),
                    name,
                    stringResource(R.string.name_placeholder),
                    {
                        name = it
                        // Limpia el error al escribir nuevamente en el campo de nombre
                        if (nameError != null) nameError = null
                    },
                    false,
                    errorMessage = nameError
                )

                CustomInput(
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

                CustomInput(
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

                // Campo de entrada para confirmar la contraseña
                CustomInput(
                    stringResource(R.string.confirm_password_label),
                    confirmPassword,
                    stringResource(R.string.password_placeholder),
                    {
                        confirmPassword = it
                        if (confirmPasswordError != null) confirmPasswordError = null
                    },
                    true,
                    errorMessage = confirmPasswordError
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Contenedor de botones
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                // Botón de registro
                CustomButton(
                    stringResource(R.string.register_title),
                    colorResource(id = R.color.btn_primary_background),
                    colorResource(id = R.color.btn_primary_text),
                    enabled = !isLoading,
                    isLoading = isLoading
                ) { 
                    // Validaciones básicas
                    nameError = if (name.isEmpty()) "El nombre es obligatorio" else null
                    emailError = if (email.isEmpty()) "El correo es obligatorio" else null
                    passwordError = if (password.isEmpty()) "La contraseña es obligatoria" else null
                    confirmPasswordError = if (confirmPassword.isEmpty()) "Repite la contraseña" else null

                    // Si no hay errores, crea el usuario
                    if (nameError == null && emailError == null && passwordError == null && confirmPasswordError == null) {
                        // Verifica si las contraseñas coinciden
                        if (password == confirmPassword) {
                            // Verifica la longitud de la contraseña
                            if (password.length < 6) {
                                passwordError = "La contraseña debe tener al menos 6 caracteres"
                                return@CustomButton
                            }

                            // Activa el estado de carga
                            isLoading = true
                            // Registra el usuario con Firebase
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Obtiene el UID del usuario registrado
                                        val uid = auth.currentUser?.uid ?: ""
                                        // Guarda los datos del usuario en un User
                                        val user = User(uid, name, email)

                                        // Guarda los datos del usuario en Firestore
                                        db.collection("users").document(uid).set(user)
                                            .addOnSuccessListener {
                                                // Desactiva el estado de carga y navega a la pantalla principal
                                                isLoading = false
                                                navController.navigate("home") {
                                                    // Elimina la pantalla de inicio de sesión de la pila de navegación
                                                    popUpTo("login") { inclusive = true }
                                                }
                                            }
                                            .addOnFailureListener { e ->
                                                // Desactiva el estado de carga y muestra un mensaje de error
                                                isLoading = false
                                                Toast.makeText(context, "Error al guardar perfil: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    } else {
                                        // Desactiva el estado de carga y muestra un mensaje de error
                                        isLoading = false
                                        val exception = task.exception
                                        // Manejo de errores
                                        when (exception) {
                                            is FirebaseAuthUserCollisionException -> {
                                                emailError = "Este correo ya está registrado"
                                            }
                                            is FirebaseAuthWeakPasswordException -> {
                                                passwordError = "La contraseña es muy débil"
                                            }
                                            else -> {
                                                Toast.makeText(context, "Error: ${exception?.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                }
                        } else {
                            // Las contraseñas no coinciden, muestra un mensaje de error
                            confirmPasswordError = "Las contraseñas no coinciden"
                        }
                    }
                }

                // Botón para navegar a la pantalla de inicio de sesión
                CustomButton(
                    stringResource(R.string.already_have_account),
                    colorResource(id = R.color.btn_secondary_background),
                    colorResource(id = R.color.btn_secondary_text),
                    enabled = !isLoading
                ) {navController.navigate("login")}
            }
        }
    }
}