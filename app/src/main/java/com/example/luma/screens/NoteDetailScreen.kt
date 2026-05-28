package com.example.luma.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AppBackground
import com.example.luma.components.BackButton

@Composable
fun NoteDetailScreen(navController: NavController){
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 50.dp),
            horizontalAlignment = Alignment.Start
        ) {
            BackButton(
                onClick = {navController.popBackStack()}
            )

            // Inputs
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp, vertical = 10.dp)
            ) {
                // Titulo de la nota
                NoteTextField(
                    value = title,
                    placeholder = stringResource(id = R.string.title_placeholder),
                    onValueChange = { title = it }
                )

                Spacer(modifier = Modifier.height(15.dp))

                // Contenido de la nota
                NoteTextField(
                    value = content,
                    placeholder = stringResource(id = R.string.content_placeholder),
                    fontSize = 16.sp,
                    onValueChange = { content = it },
                    singleLine = false
                )
            }
        }
    }
}

// Textfields
@Composable
private fun NoteTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    fontSize: TextUnit = 32.sp,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true
){
    Box(
        modifier = modifier
    ){
        // Placeholder del titulo
        if (value.isEmpty()){
            Text(
                text = placeholder,
                fontSize = fontSize,
                color = colorResource(id = R.color.placeholder_col)
            )
        }

        // Titulo
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = fontSize,
                color = colorResource(id = R.color.text_color)
            ),
            singleLine = singleLine
        )
    }
}