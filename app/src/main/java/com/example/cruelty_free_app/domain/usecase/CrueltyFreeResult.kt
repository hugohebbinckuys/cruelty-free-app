package com.example.cruelty_free_app.domain.usecase

import com.example.cruelty_free_app.domain.model.Brand
import com.example.cruelty_free_app.domain.model.Product


sealed class CrueltyFreeResult {
    data class Success(val product: Product, val brand: Brand): CrueltyFreeResult()
    object ProductNotFound: CrueltyFreeResult()
    object AliasUnknown: CrueltyFreeResult()
    object BrandNotFound: CrueltyFreeResult()
    object CrueltyFreeNotFound: CrueltyFreeResult()
    object UnknownError: CrueltyFreeResult()

}