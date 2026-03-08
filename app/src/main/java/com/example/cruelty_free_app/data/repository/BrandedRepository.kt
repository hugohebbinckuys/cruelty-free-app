package com.example.cruelty_free_app.data.repository
import com.example.cruelty_free_app.data.local.datasource.AliasesBrandDataSource
import com.example.cruelty_free_app.data.local.datasource.BrandDataSource
import com.example.cruelty_free_app.domain.repository.BrandRepository
import com.example.cruelty_free_app.domain.model.Brand



class BrandedRepositoryImp(
    private val brandDataSource: BrandDataSource,
    private val aliasesBrandDataSource: AliasesBrandDataSource,
) : BrandRepository {

    override fun findByName(name: String): Brand? {
        return brandDataSource.getBrands()
            .find{ it.brandName == name}
            ?.let{ Brand(it.brandName, it.nonCrueltyFree) }

    }

    override  fun resolveAlias(alias: String): String? {
       return aliasesBrandDataSource.getBrandNameByAliase(alias)?.brandName
    }
}