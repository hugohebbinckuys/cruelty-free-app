package com.example.cruelty_free_app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(onScanClick: () -> Unit, onHistoryClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "scan?", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onScanClick) {
                Text("Want to Scan")
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onHistoryClick) {
                Text("History")
            }
        }
    }
}