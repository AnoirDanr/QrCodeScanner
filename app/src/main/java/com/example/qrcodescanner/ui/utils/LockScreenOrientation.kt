package com.example.qrcodescanner.ui.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit){
        val activity = context as Activity
        val originalOrientation = activity.requestedOrientation
        onDispose {
            activity.requestedOrientation= originalOrientation
        }
    }

}



fun Context.getDefaultBrowserPackage():String{
    //startActivity( Intent(Intent.ACTION_VIEW, Uri.parse("https://www.icacciastorie.it/")))
    val resolve = packageManager
        .resolveActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("http://")),
            PackageManager.MATCH_DEFAULT_ONLY
            )

    return if (resolve != null){
        resolve.activityInfo.packageName
    }else{
        ""
    }

}

fun Context.getIconFromPackage(p:String):Drawable{
    return packageManager.getApplicationIcon(p)
}