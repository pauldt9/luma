package com.example.luma.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.luma.R
import com.example.luma.components.AppBackground
import com.example.luma.components.ScreenTitle

@Composable
fun HomeScreen(navController: NavController){
    AppBackground() {
        ScreenTitle(stringResource(id = R.string.home_greeting))
    }
}