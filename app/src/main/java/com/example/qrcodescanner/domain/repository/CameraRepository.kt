package com.example.qrcodescanner.domain.repository

import androidx.camera.core.ImageProxy

interface  CameraRepository{
    suspend fun scanQRCode(imageProxy: ImageProxy) : Pair<String?,Int>
}