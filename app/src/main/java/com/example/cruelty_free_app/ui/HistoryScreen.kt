package com.example.cruelty_free_app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cruelty_free_app.data.ScanEntry
import com.example.cruelty_free_app.data.ScanStorage
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    var entries by remember { mutableStateOf(ScanStorage.getAll(context)) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
            }
            Text(text = "Scan History", fontSize = 20.sp)
        }

        if (entries.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No scans yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(entries, key = { it.scannedAt }) { entry ->
                    HistoryItem(
                        entry = entry,
                        onDelete = {
                            ScanStorage.delete(context, entry)
                            entries = ScanStorage.getAll(context)
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun HistoryItem(entry: ScanEntry, onDelete: () -> Unit) {
    val date = remember(entry.scannedAt) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            .format(Date(entry.scannedAt))
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = entry.barcode, fontSize = 16.sp)
            Text(text = date, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Supprimer")
        }
    }
}