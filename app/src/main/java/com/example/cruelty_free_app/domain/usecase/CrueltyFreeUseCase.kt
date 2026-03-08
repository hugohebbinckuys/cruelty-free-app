package com.example.cruelty_free_app.domain.usecase

import android.util.Log
import com.example.cruelty_free_app.domain.model.Brand
import com.example.cruelty_free_app.domain.model.Product
import com.example.cruelty_free_app.domain.repository.BrandRepository
import com.example.cruelty_free_app.domain.repository.ProductRepository

class CrueltyFreeUseCase(
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository

){
    suspend fun checkCrueltyFree(barcode: String): CrueltyFreeResult {
        return try{

            val product: Product = productRepository.getByCode(barcode)
                ?: return CrueltyFreeResult.ProductNotFound
            Log.e("UseCase",product.brand.toString())


            val brandName: String  = brandRepository.resolveAlias(product.brand)
                ?: return CrueltyFreeResult.AliasUnknown
            Log.e("UseCase",brandName)


            val brand: Brand = brandRepository.findByName(brandName)
                ?: return CrueltyFreeResult.BrandNotFound


            return CrueltyFreeResult.Success(brand)



        } catch(e: Exception){
            CrueltyFreeResult.UnknownError
        } as CrueltyFreeResult



    }






}