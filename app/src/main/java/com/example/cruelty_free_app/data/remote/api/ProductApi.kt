package com.example.cruelty_free_app.data.remote.api


import com.example.cruelty_free_app.data.remote.dto.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ProductApi{

    @GET("prod/trial/lookup")
    suspend fun getProduct(
        @Query("upc") barcode: String
    ): ProductResponse

    companion object {
        const val BASE_URL = "https://api.upcitemdb.com/"
    }

}