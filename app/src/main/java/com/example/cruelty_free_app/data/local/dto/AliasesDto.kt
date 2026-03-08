package com.example.cruelty_free_app.data.local.dto
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class AliasesDto (
    val aliase: String,
    val brandName: String
)
