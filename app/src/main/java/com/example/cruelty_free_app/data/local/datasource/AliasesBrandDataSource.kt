package com.example.cruelty_free_app.data.local.datasource

import android.content.Context
import com.example.cruelty_free_app.data.local.dto.AliasesDto
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.collections.*




import java.io.File



class AliasesBrandDataSource (private val context: Context){

    fun getAliases(): List<AliasesDto>{
        // Contenu du json
        val content = context.assets.open("branded_aliase.json")
            .bufferedReader()
            .readText()
        // map + serialization en aliaseDto + ajout dans une liste
        val map = Json.decodeFromString<Map<String,String>>(content)
            .map{
                (aliase,brandName) ->
                AliasesDto(aliase,brandName)
            }
        return map
    }

    fun getBrandNameByAliase(aliase: String): AliasesDto?{
        return getAliases().find{ it.aliase == aliase}
    }


}
