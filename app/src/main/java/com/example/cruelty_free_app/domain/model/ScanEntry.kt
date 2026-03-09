package com.example.cruelty_free_app.domain.model

data class ScanEntry(
    val barcode: String,
    val scannedAt: Long = System.currentTimeMillis(),
    val title: String? = null,
    val imageUrl: String? = null
)