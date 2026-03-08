package com.example.cruelty_free_app.data.repository

import com.example.cruelty_free_app.data.remote.api.ProductApi

import com.example.cruelty_free_app.data.remote.dto.ProductDto
import com.example.cruelty_free_app.domain.model.Product
import com.example.cruelty_free_app.domain.repository.ProductRepository
import com.example.cruelty_free_app.data.mapper.toDomain
class ProductRepositoryImpl(
    private val api: ProductApi
): ProductRepository {
    override suspend fun getByCode(barcode: String): Product? {
        val response = api.getProduct(barcode)
        return response.items.firstOrNull()?.toDomain()


    }

}
