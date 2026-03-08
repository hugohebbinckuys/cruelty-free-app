package com.example.cruelty_free_app.data.mapper
import com.example.cruelty_free_app.data.remote.dto.ProductDto
import com.example.cruelty_free_app.domain.model.Product

fun ProductDto.toDomain(): Product{
    return Product(
        ean = ean,
        title = title,
        description = description,
        brand = brand,
        category = category,
        images = images
    )

}