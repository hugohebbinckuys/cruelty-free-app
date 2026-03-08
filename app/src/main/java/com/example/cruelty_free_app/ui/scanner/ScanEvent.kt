package com.example.cruelty_free_app.ui.scanner

sealed class ScanEvent {
    data class NavigateToProduct(val barcode: String) : ScanEvent()
}
