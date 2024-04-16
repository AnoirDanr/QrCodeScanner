package com.example.qrcodescanner

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cacciastorie.fourrooms.qrcode.presentation.QRScan
import com.example.qrcodescanner.presentation.QRScanViewModel
import com.example.qrcodescanner.ui.utils.LockScreenOrientation
import com.example.qrcodescanner.ui.theme.QrCodeScannerTheme
import com.example.qrcodescanner.ui.utils.getDefaultBrowserPackage
import com.example.qrcodescanner.ui.views.QRScanResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!arePermissionsGranted()){
            ActivityCompat.requestPermissions(this, PERMISSIONS,100)
        }
        setContent {
            val context = LocalContext.current
            getDefaultBrowserPackage()
            (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val sharedViewModel  = hiltViewModel<QRScanViewModel>()
            QrCodeScannerTheme {
                val navController = rememberNavController()
                val startDestination = MainDestinations.ROUTE_MAIN
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController =  navController,
                        startDestination = startDestination,
                        enterTransition = {
                            slideInHorizontally(
                                animationSpec = tween(durationMillis = 200)
                            )
                        },
                        exitTransition = {
                            slideOutHorizontally(
                                animationSpec = tween(durationMillis = 200)
                            )
                        }
                    ){

                        composable(
                            route = MainDestinations.ROUTE_MAIN
                        ){
                            QRScan(
                                onNavigateToRoute = navController::navigate,
                                viewModel = sharedViewModel
                            )
                        }
                        composable(
                            route = MainDestinations.ROUTE_RESULT
                        ){
                            QRScanResult(
                                upPress = navController::navigateUp,
                                sharedViewModel
                            )
                        }

                    }
                }
            }
        }
    }

    private fun arePermissionsGranted(): Boolean {
        return PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(
                applicationContext,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}

object MainDestinations{
    const val ROUTE_MAIN = "scan"
    const val ROUTE_RESULT = "result"

}

