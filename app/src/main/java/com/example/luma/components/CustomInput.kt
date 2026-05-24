package com.example.luma.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luma.R


@Composable
fun CustomInput(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean,
    errorMessage: String? = null,
    inputHeight: Dp = 40.dp
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        BasicText(
            text = label,
            style = TextStyle(
                color = if (errorMessage != null) Color.Red else colorResource(id = R.color.text_color),
                fontSize = 14.sp
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = colorResource(id = R.color.text_color),
                fontSize = 14.sp
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
            ),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            decorationBox = {innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(inputHeight)
                        .border(
                            width = 1.dp,
                            color = if (errorMessage != null) Color.Red else colorResource(id = R.color.textfield_border_col),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .background(
                            color = colorResource(id = R.color.textfield_bg_col),
                            shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()){
                        BasicText(
                            text = placeholder,
                            style = TextStyle(
                                color = colorResource(id = R.color.placeholder_col),
                                fontSize = 16.sp
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
        
        if (errorMessage != null) {
            BasicText(
                text = errorMessage,
                style = TextStyle(
                    color = Color.Red,
                    fontSize = 12.sp
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}