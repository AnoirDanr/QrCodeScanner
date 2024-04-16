package com.example.qrcodescanner.presentation

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodescanner.domain.repository.CameraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QRScanViewModel @Inject constructor(
    private val cameraRepository: CameraRepository
) : ViewModel() {

    val QRCodeValue = MutableStateFlow(Triple("1231",false,0))

    fun scanQRCode(imageProxy: ImageProxy){
        viewModelScope.launch {
            val result = cameraRepository.scanQRCode(imageProxy)
            val value = result.first
            val valueType = result.second
            if(value != null)
                QRCodeValue.value = Triple(value,true,valueType) }
    }

    fun resetValue(){
        viewModelScope.launch {
            QRCodeValue.value = Triple("1231",false,0) }
    }
}