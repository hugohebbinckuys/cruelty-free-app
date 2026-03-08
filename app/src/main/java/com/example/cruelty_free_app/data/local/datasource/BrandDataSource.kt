package com.example.cruelty_free_app.data.local.datasource

import android.content.Context
import com.example.cruelty_free_app.data.local.dto.BrandDto

class BrandDataSource(private val context: Context){

    fun getBrands(): List<BrandDto> {
        val reader = context.assets.open("non_cruelty_free.csv")
            .bufferedReader()

        val brands = reader.lineSequence()
            .drop(1)  // saute la première ligne (les en-têtes du CSV)
            .map { line ->
                val columns = line.split(";")
                BrandDto(
                    brandName = columns[0].trim(),
                    nonCrueltyFree = columns[1].trim() == "1"
                )
            }
            .toList()

        reader.close()
        return brands
    }
}