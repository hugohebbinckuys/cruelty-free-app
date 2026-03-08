package com.example.cruelty_free_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.cruelty_free_app.data.local.datasource.AliasesBrandDataSource
import com.example.cruelty_free_app.data.local.datasource.BrandDataSource
import com.example.cruelty_free_app.data.repository.ProductRepositoryImpl
import com.example.cruelty_free_app.data.remote.api.ProductApi
import com.example.cruelty_free_app.data.repository.BrandedRepositoryImp
import com.example.cruelty_free_app.domain.repository.BrandRepository
import com.example.cruelty_free_app.domain.repository.ProductRepository
import com.example.cruelty_free_app.domain.usecase.CrueltyFreeUseCase
import com.example.cruelty_free_app.ui.theme.CrueltyfreeappTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrueltyfreeappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(ProductApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val productApi = retrofit.create(ProductApi::class.java)

        val aliasesBrandDataSource = AliasesBrandDataSource(this)
        val brandDataSource = BrandDataSource(this)



        val brandRepository = BrandedRepositoryImp(brandDataSource,aliasesBrandDataSource)
        val productRepository = ProductRepositoryImpl(productApi)


        val crueltyFreeUseCase = CrueltyFreeUseCase(brandRepository,productRepository)

        lifecycleScope.launch {
            Log.e("MainActivity",crueltyFreeUseCase.checkCrueltyFree("071249206034").toString())

        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CrueltyfreeappTheme {
        Greeting("Android")
    }
}