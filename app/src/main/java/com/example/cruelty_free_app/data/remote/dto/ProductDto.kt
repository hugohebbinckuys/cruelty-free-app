package com.example.cruelty_free_app.data.remote.dto

import java.util.Dictionary


data class ProductResponse(
    var code: String,
    var items: List<ProductDto>
)


data class ProductDto(
    var ean: String,
    var title: String,
    var description: String,
    var brand: String,
    var category: String,
    var images: List<String>

)