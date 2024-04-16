package com.example.qrcodescanner.data.repository

import android.app.Application
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.example.qrcodescanner.domain.repository.CameraRepository
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class CameraRepositoryImpl @Inject constructor(
    private val app:Application
) : CameraRepository {
    @OptIn(ExperimentalGetImage::class)
    override suspend fun scanQRCode(imageProxy:ImageProxy) : Pair<String?,Int>{
        Log.i("SCAN","SCANNING...")
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE)
            .build()
        val scanner = BarcodeScanning.getClient(options)

        val inputImage = InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
        return suspendCancellableCoroutine { cont->

            scanner.process(inputImage).apply {
                addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    val rawValue = barcode.rawValue
                    Log.i("SCAN","$rawValue")
                    val valueType = barcode.valueType
                    cont.resume(Pair(rawValue,valueType))
                    if(cont.isCompleted){
                        imageProxy.close()
                        break
                    }

                }
                    imageProxy.close()
                }
                addOnFailureListener{
                    cont.cancel()
                    imageProxy.close()
                }
                addOnCanceledListener {
                    cont.cancel()
                    imageProxy.close()
                }
            }
        }

    }
}