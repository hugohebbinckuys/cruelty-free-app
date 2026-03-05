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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cruelty_free_app.camera.CameraManager
import java.util.concurrent.ExecutorService
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import kotlinx.coroutines.launch

@Composable
fun ScannerScreen(
    cameraExecutor: ExecutorService,
    onBackClick: () -> Unit
) {
    var showManualInput by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }
    var scanResult by remember { mutableStateOf<String?>(null) }
    var hasScanned by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        // Bouton retour
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

        // Bouton "Enter manually" — toujours visible en bas
        if (scanResult == null) {
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
                Text("Enter manually")
            }
        }

        // Résultat scan
        scanResult?.let { result ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Résultat :", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = result, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = {
                        scanResult = null
                        hasScanned = false
                    }) {
                        Text("Retry")
                    }
                }
            }
        }

        if (showManualInput) {
            ManualInputDialog(
                onDismiss = { showManualInput = false },
                onConfirm = { code ->
                    scanResult = code
                    hasScanned = true
                    showManualInput = false
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        //bloc qui s'execute une seule fois car on attend que Unit change avec le LaunchEffects sauf que Unit changera jamais (objet unique qui ne change jamais).
        // On va avoir un "changement" uniquement si y a un resultat
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            CameraManager.startCamera(
                context = context,
                lifecycleOwner = context as androidx.lifecycle.LifecycleOwner,
                previewView = previewView,
                cameraExecutor = cameraExecutor
            ) { result ->
                if (!hasScanned) {
                    hasScanned = true
                    scanResult = result
                }
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
        title = { Text("Enter barcode manually") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it.filter { c -> c.isDigit() } },
                label = { Text("Barcode number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = { if (text.isNotEmpty()) onConfirm(text) },
                enabled = text.isNotEmpty()
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}