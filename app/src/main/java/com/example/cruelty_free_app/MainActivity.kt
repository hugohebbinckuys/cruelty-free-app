package com.example.cruelty_free_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cruelty_free_app.data.local.datasource.AliasesBrandDataSource
import com.example.cruelty_free_app.data.local.datasource.BrandDataSource
import com.example.cruelty_free_app.data.local.datasource.ScanLocalDataSource
import com.example.cruelty_free_app.data.remote.api.ProductApi
import com.example.cruelty_free_app.data.repository.BrandedRepositoryImp
import com.example.cruelty_free_app.data.repository.ProductRepositoryImpl
import com.example.cruelty_free_app.data.repository.ScanRepositoryImpl
import com.example.cruelty_free_app.domain.repository.ScanRepository
import com.example.cruelty_free_app.domain.usecase.CrueltyFreeUseCase
import com.example.cruelty_free_app.ui.HistoryScreen
import com.example.cruelty_free_app.ui.HomeScreen
import com.example.cruelty_free_app.ui.ScannerScreen
import com.example.cruelty_free_app.ui.product.ProductScreen
import com.example.cruelty_free_app.ui.scanner.ScannerViewModel
import com.example.cruelty_free_app.ui.theme.CrueltyfreeappTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {

    private val cameraExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- DI manuel : construction de toutes les dépendances ---
        val scanRepository: ScanRepository = ScanRepositoryImpl(ScanLocalDataSource(this))

        val productApi = Retrofit.Builder()
            .baseUrl(ProductApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)

        val crueltyFreeUseCase = CrueltyFreeUseCase(
            brandRepository = BrandedRepositoryImp(
                brandDataSource = BrandDataSource(this),
                aliasesBrandDataSource = AliasesBrandDataSource(this)
            ),
            productRepository = ProductRepositoryImpl(productApi)
        )

        setContent {
            CrueltyfreeappTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {

                    composable("home") {
                        HomeScreen(
                            onScanClick = { navController.navigate("scanner") },
                            onHistoryClick = { navController.navigate("history") }
                        )
                    }

                    composable("scanner") {
                        val vm: ScannerViewModel = viewModel(
                            factory = ScannerViewModel.factory(scanRepository)
                        )
                        ScannerScreen(
                            cameraExecutor = cameraExecutor,
                            viewModel = vm,
                            onBackClick = { navController.navigateUp() },
                            onNavigateToProduct = { barcode ->
                                navController.navigate("product/$barcode")
                            }
                        )
                    }

                    composable("history") {
                        HistoryScreen(
                            scanRepository = scanRepository,
                            onBackClick = { navController.navigateUp() }
                        )
                    }

                    composable("product/{barcode}") { backStackEntry ->
                        val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
                        ProductScreen(
                            barcode = barcode,
                            useCase = crueltyFreeUseCase,
                            onBackClick = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
