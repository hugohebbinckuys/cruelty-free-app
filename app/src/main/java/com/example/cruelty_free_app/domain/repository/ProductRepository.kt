package com.example.cruelty_free_app.domain.repository

import com.example.cruelty_free_app.domain.model.Product


interface ProductRepository {
    suspend fun getByCode(barcode: String): Product?
}