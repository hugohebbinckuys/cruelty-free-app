package com.example.cruelty_free_app.domain.model

import java.util.Dictionary



data class Product(
    var ean: String,
    var title: String,
    var description: String,
    var brand: String,
    var category: String,
    var images: List<String>

)