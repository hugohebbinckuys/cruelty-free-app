package com.example.cruelty_free_app.ui.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cruelty_free_app.domain.model.ScanEntry
import com.example.cruelty_free_app.domain.repository.ScanRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ScannerViewModel(
    private val scanRepository: ScanRepository
) : ViewModel() {

    private val _events = MutableSharedFlow<ScanEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<ScanEvent> = _events

    private var hasScanned = false

    fun onBarcodeDetected(barcode: String) {
        if (hasScanned) return
        hasScanned = true
        scanRepository.save(ScanEntry(barcode = barcode))
        _events.tryEmit(ScanEvent.NavigateToProduct(barcode))
    }

    fun onManualCodeEntered(barcode: String) {
        if (barcode.isBlank()) return
        scanRepository.save(ScanEntry(barcode = barcode))
        _events.tryEmit(ScanEvent.NavigateToProduct(barcode))
    }

    companion object {
        fun factory(scanRepository: ScanRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    ScannerViewModel(scanRepository) as T
            }
    }
}
