package com.cacciastorie.fourrooms.qrcode.presentation

import android.content.pm.ActivityInfo
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.qrcodescanner.MainDestinations
import com.example.qrcodescanner.presentation.QRScanViewModel
import com.example.qrcodescanner.ui.utils.LockScreenOrientation
import java.util.concurrent.Executors

@OptIn(ExperimentalGetImage::class)
@Composable
fun QRScan(
    onNavigateToRoute:(String)->Unit,
    viewModel: QRScanViewModel
){
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraExecutor = remember{ Executors.newSingleThreadExecutor()}
    LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val resolutionBuilder = ResolutionSelector.Builder().apply {
        setAspectRatioStrategy(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
    }
    resolutionBuilder.build()
    val controller = remember { LifecycleCameraController(context) }.apply {
        this.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        bindToLifecycle(lifecycleOwner)
    }
    val QRCodeValue = viewModel.QRCodeValue.collectAsState().value
    //Contains the boolean
    val valueFound = QRCodeValue.second


    controller.setImageAnalysisAnalyzer(cameraExecutor) { imageProxy ->

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            viewModel.scanQRCode(imageProxy)

        }

    }




    val previewView = remember {
        PreviewView(context)
    }.apply{
        this.controller = controller
        controller.bindToLifecycle(lifecycleOwner)
        //onNavigateToRoute(MainDestinations.ROUTE_RESULT)
    }

    if(valueFound){
        controller.unbind()
        onNavigateToRoute(MainDestinations.ROUTE_RESULT)
    }


    QRScanLayout(previewView = previewView)

}




@Composable
fun QRScanLayout(previewView:PreviewView){
    Box(modifier = Modifier

        .fillMaxSize()
        .drawWithContent {
            drawContent()
            drawOther()
        },
        contentAlignment = Alignment.Center,





        ){
        AndroidView(factory = {previewView},

            modifier=Modifier.fillMaxSize()


        )

        Canvas(modifier = Modifier){

        }
    }
}

private fun DrawScope.drawOther(){
    var width = size.width / 2
    val height = size.height / 2

    var offsetX = this.center.x - (width / 2)
    var offsetY = this.center.y - (width / 2)
    clipRect(
        clipOp = ClipOp.Difference
    ){
        drawRect(
            color = Color.Blue,

            //style = Stroke(4.0f),
            topLeft = Offset(offsetX, offsetY),
            size = Size(width, width),

            )
    }

    width += 30
    offsetX = this.center.x - (width / 2)
    offsetY = this.center.y - (width / 2)
    drawRect(
        color = Color.Green,

        style = Stroke(4.0f),
        topLeft = Offset(offsetX, offsetY),
        size = Size(width, width),

        )
}

@Preview
@Composable
fun CanvasPreview(){
    Canvas(modifier = Modifier.fillMaxSize()){
        this.drawOther()
    }
}