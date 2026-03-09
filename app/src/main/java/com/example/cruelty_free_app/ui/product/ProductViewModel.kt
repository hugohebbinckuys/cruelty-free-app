package com.example.cruelty_free_app.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cruelty_free_app.domain.model.ScanEntry
import com.example.cruelty_free_app.domain.repository.ScanRepository
import com.example.cruelty_free_app.domain.usecase.CrueltyFreeResult
import com.example.cruelty_free_app.domain.usecase.CrueltyFreeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val useCase: CrueltyFreeUseCase,
    private val scanRepository: ScanRepository,
    private val barcode: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState

    init {
        loadProduct()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            _uiState.value = when (val result = useCase.checkCrueltyFree(barcode)) {
                is CrueltyFreeResult.Success -> {
                    scanRepository.save(
                        ScanEntry(
                            barcode = barcode,
                            title = result.product.title,
                            imageUrl = result.product.images.firstOrNull()
                        )
                    )
                    ProductUiState.Success(result.product, result.brand)
                }
                is CrueltyFreeResult.ProductNotFound -> ProductUiState.ProductNotFound
                is CrueltyFreeResult.AliasUnknown    -> ProductUiState.AliasUnknown
                is CrueltyFreeResult.BrandNotFound   -> ProductUiState.BrandNotFound
                else                                 -> ProductUiState.UnknownError
            }
        }
    }

    companion object {
        fun factory(useCase: CrueltyFreeUseCase, scanRepository: ScanRepository, barcode: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    ProductViewModel(useCase, scanRepository, barcode) as T
            }
    }
}
