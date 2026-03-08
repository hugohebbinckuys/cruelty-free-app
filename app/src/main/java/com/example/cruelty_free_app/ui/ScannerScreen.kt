package com.example.cruelty_free_app.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cruelty_free_app.data.camera.CameraManager
import com.example.cruelty_free_app.ui.scanner.ScanEvent
import com.example.cruelty_free_app.ui.scanner.ScannerViewModel
import java.util.concurrent.ExecutorService

@Composable
fun ScannerScreen(
    cameraExecutor: ExecutorService,
    viewModel: ScannerViewModel,
    onBackClick: () -> Unit,
    onNavigateToProduct: (String) -> Unit
) {
    var showManualInput by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }


    LaunchedEffect("reset") {
        viewModel.reset()
    }

    // Collecte les events one-shot de navigation
    LaunchedEffect("events") {
        viewModel.events.collect { event ->
            when (event) {
                is ScanEvent.NavigateToProduct -> onNavigateToProduct(event.barcode)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Retour",
                tint = Color.White
            )
        }

        OutlinedButton(
            onClick = { showManualInput = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Black.copy(alpha = 0.5f),
                contentColor = Color.White
            )
        ) {
            Text("Saisir manuellement")
        }
    }

    if (showManualInput) {
        ManualInputDialog(
            onDismiss = { showManualInput = false },
            onConfirm = { code ->
                showManualInput = false
                viewModel.onManualCodeEntered(code)
            }
        )
    }

    LaunchedEffect("camera") {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            CameraManager.startCamera(
                context = context,
                lifecycleOwner = context as androidx.lifecycle.LifecycleOwner,
                previewView = previewView,
                cameraExecutor = cameraExecutor
            ) { barcode ->
                viewModel.onBarcodeDetected(barcode)
            }
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CAMERA),
                100
            )
        }
    }
}

@Composable
fun ManualInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Saisir le code-barres") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it.filter { c -> c.isDigit() } },
                label = { Text("Numéro de code-barres") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = { if (text.isNotEmpty()) onConfirm(text) },
                enabled = text.isNotEmpty()
            ) {
                Text("Confirmer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}
