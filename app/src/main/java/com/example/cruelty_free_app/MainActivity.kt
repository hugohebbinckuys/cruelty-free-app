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



import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cruelty_free_app.ui.HistoryScreen
import com.example.cruelty_free_app.ui.HomeScreen
import com.example.cruelty_free_app.ui.ScannerScreen
import java.util.concurrent.Executors


class MainActivity : ComponentActivity() {

    private val cameraExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        //function onCreate = fonction de lifecycle (c a d aappelée automatiquement par le systeme à différents moments dans le cylc de vie de l'app (par exemple Oncreate quand l'activité est créée, onStart juste avant que l'activité soit créée. onDestroy quand l'activité est détruite etc.)

        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(
                        onScanClick = { navController.navigate("scanner") },
                        onHistoryClick = { navController.navigate("history") }
                    )
                }
                composable("scanner") {
                    ScannerScreen(
                        cameraExecutor = cameraExecutor,
                        onBackClick = { navController.navigateUp() }
                    )
                }
                composable("history") {
                    HistoryScreen(onBackClick = { navController.navigateUp() })
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

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}