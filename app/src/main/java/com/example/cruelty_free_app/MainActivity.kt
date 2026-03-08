package com.example.cruelty_free_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.lifecycle.lifecycleScope
import com.example.cruelty_free_app.data.local.datasource.AliasesBrandDataSource
import com.example.cruelty_free_app.data.local.datasource.BrandDataSource
import com.example.cruelty_free_app.data.local.datasource.ScanLocalDataSource
import com.example.cruelty_free_app.data.repository.ProductRepositoryImpl
import com.example.cruelty_free_app.data.remote.api.ProductApi
import com.example.cruelty_free_app.data.repository.BrandedRepositoryImp
import com.example.cruelty_free_app.data.repository.ScanRepositoryImpl
import com.example.cruelty_free_app.domain.repository.BrandRepository
import com.example.cruelty_free_app.domain.repository.ProductRepository
import com.example.cruelty_free_app.domain.repository.ScanRepository
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

        val scanLocalDataSource = ScanLocalDataSource(this)
        val scanRepository: ScanRepository = ScanRepositoryImpl(scanLocalDataSource)

        //

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

        //

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
                        scanRepository = scanRepository,
                        onBackClick = { navController.navigateUp() },
                        onScanResult = { barcode ->
                            lifecycleScope.launch {
                                Log.e("MainActivity", crueltyFreeUseCase.checkCrueltyFree(barcode).toString())
                            }
                        }
                    )
                }
                composable("history") {
                    HistoryScreen(
                        scanRepository = scanRepository,
                        onBackClick = { navController.navigateUp() }
                    )
                }
            }
        }

        /*
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
        */
         

        lifecycleScope.launch {
            Log.e("MainActivity",crueltyFreeUseCase.checkCrueltyFree("071249206034").toString())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}