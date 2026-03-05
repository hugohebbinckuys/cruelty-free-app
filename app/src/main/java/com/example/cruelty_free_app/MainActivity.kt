package com.example.cruelty_free_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}