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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cruelty_free_app.domain.model.ScanEntry
import com.example.cruelty_free_app.domain.repository.ScanRepository
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(scanRepository: ScanRepository, onBackClick: () -> Unit) {
    var entries by remember { mutableStateOf(scanRepository.getAll()) }

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
                            scanRepository.delete(entry)
                            entries = scanRepository.getAll()
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (entry.imageUrl != null) {
            AsyncImage(
                model = entry.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.small)
            )
        } else {
            Spacer(modifier = Modifier.size(56.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            if (entry.title != null) {
                Text(text = entry.title, fontSize = 16.sp, maxLines = 1)
            }
            Text(text = entry.barcode, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = date, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Supprimer")
        }
    }
}