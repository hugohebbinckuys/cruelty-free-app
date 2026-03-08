package com.example.cruelty_free_app.domain.repository

import com.example.cruelty_free_app.domain.model.Brand

interface BrandRepository {
    fun findByName(name: String): Brand?
    fun resolveAlias(alias: String): String?


}