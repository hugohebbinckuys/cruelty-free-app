package com.example.cruelty_free_app.ui.product

import com.example.cruelty_free_app.domain.model.Brand
import com.example.cruelty_free_app.domain.model.Product

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val product: Product, val brand: Brand) : ProductUiState()
    object ProductNotFound : ProductUiState()
    object AliasUnknown : ProductUiState()
    object BrandNotFound : ProductUiState()
    object UnknownError : ProductUiState()
}
