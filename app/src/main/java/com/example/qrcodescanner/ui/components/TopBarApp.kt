package com.example.qrcodescanner.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRTopAppBar(
    title :@Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon:@Composable () -> Unit = {}

){
    TopAppBar(
        title = title,
        modifier = Modifier,
        navigationIcon = navigationIcon
    )



}