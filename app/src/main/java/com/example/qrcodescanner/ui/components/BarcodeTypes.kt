package com.example.qrcodescanner.ui.components

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.NoteAlt
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.core.graphics.drawable.toBitmap
import com.example.qrcodescanner.ui.utils.getDefaultBrowserPackage
import com.example.qrcodescanner.ui.utils.getIconFromPackage
import com.google.mlkit.vision.barcode.common.Barcode
import  androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class BarcodeTypes(
    val valueType:Int,
    val icon:@Composable () -> Unit,
    val buttons:@Composable (String, ()->Unit)->Unit,
) {

    URL(
        valueType = Barcode.TYPE_URL,
        icon ={
            val context = LocalContext.current
            val browserPackage = context
                .getDefaultBrowserPackage()
            if(!browserPackage.contentEquals("")){
                val browserIcon =  context
                    .getIconFromPackage(browserPackage)
                    .toBitmap()
                    .asImageBitmap()
                Image(
                    painter = BitmapPainter(browserIcon),
                    contentDescription = null
                )
            }

        },
        buttons= {value,snackbar->

            val localUriHandler = LocalUriHandler.current
            FilledTonalIconButton(onClick = {
                localUriHandler.openUri(value)
            }) {
                Icon(
                    imageVector = Icons.Rounded.OpenInBrowser,
                    contentDescription = null
                )
                RAW_TEXT.DefaultButtons(value,snackbar)
            }

        }
    ),
    RAW_TEXT(
        valueType = Barcode.TYPE_TEXT,
        icon= {
            Icon(
                modifier = Modifier.size(168.dp,168.dp),
                imageVector = Icons.Rounded.NoteAlt,
                contentDescription = null
            )
        },
        buttons = { value,snackbar->
            RAW_TEXT.DefaultButtons(value,snackbar)
        }

    );

    @Composable
    fun DefaultButtons(value:String,snackBar:()->Unit){
        val clipboardManager = LocalClipboardManager.current
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, value)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        val context = LocalContext.current
        FilledTonalIconButton(onClick = {
            clipboardManager.setText(annotatedString = AnnotatedString(value))
            snackBar()
        }) {
            Icon(
                imageVector = Icons.Rounded.ContentCopy,
                contentDescription = null
            )
        }
        FilledTonalIconButton(onClick = {
            context.startActivity(shareIntent)
        }) {
            Icon(
                imageVector = Icons.Rounded.Share,
                contentDescription = null
            )
        }


    }
    companion object{
        fun valueByType(valueType:Int) : BarcodeTypes{
            entries.forEach {
                if(it.valueType == valueType)
                    return it
            }
            return RAW_TEXT
        }

    }

}