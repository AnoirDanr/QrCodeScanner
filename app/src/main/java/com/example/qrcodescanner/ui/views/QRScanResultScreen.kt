package com.example.qrcodescanner.ui.views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qrcodescanner.presentation.QRScanViewModel
import com.example.qrcodescanner.ui.components.BarcodeTypes
import com.example.qrcodescanner.ui.components.QRScaffold
import com.example.qrcodescanner.ui.components.QRTopAppBar
import com.example.qrcodescanner.ui.theme.QrCodeScannerTheme
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.launch


@Composable
fun QRScanResult(
    upPress:() -> Unit,
    viewModel: QRScanViewModel
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState= remember{ SnackbarHostState() }

    val state = viewModel.QRCodeValue.collectAsState()
    val valueQRCode = state.value.first
    val barcodeType = BarcodeTypes.valueByType(state.value.third)

    // on back press
    BackHandler {
        viewModel.resetValue()
        upPress()
        upPress()
    }
    QRScaffold(
        topBar = {
            QRTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.resetValue()
                        upPress()
                        upPress()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = null)
                    }
                }
            )
        },
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)}

    ) { padding->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    start = 30.dp,
                    end = 30.dp
                )
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 15.dp),
                text = "Result" ,
                style= MaterialTheme.typography.headlineLarge
            )
            Column(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                barcodeType.icon()
            }


            Card(
                Modifier
                    .fillMaxWidth()


            ) {
                Column(
                    modifier = Modifier
                        .padding(30.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Rounded.QrCode,
                        contentDescription = null)
                    Text(
                        modifier = Modifier
                            .paddingFromBaseline(top=20.dp),
                        text = valueQRCode,
                        style= MaterialTheme.typography.labelLarge
                    )
                    Row {


                        barcodeType.buttons(valueQRCode){
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message="\"$valueQRCode\" has been copied.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                }

            }
        }
    }


}

@Preview
@Composable
fun Test(){
    QrCodeScannerTheme {
        val barcodeType = BarcodeTypes.valueByType(Barcode.TYPE_TEXT)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 15.dp),
                text = "Result" ,
                style= MaterialTheme.typography.headlineLarge
            )
            Column(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                barcodeType.icon()
            }


            Card(
                Modifier
                    .fillMaxWidth()


            ) {
                Column(
                    modifier = Modifier
                        .padding(30.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Rounded.QrCode,
                        contentDescription = null)
                    Text(
                        modifier = Modifier
                            .paddingFromBaseline(top=20.dp),
                        text = "random text",
                        style= MaterialTheme.typography.labelLarge
                    )

                }
            }
        }
    }

}