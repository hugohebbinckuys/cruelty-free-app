package com.example.cruelty_free_app.ui.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.cruelty_free_app.domain.repository.ScanRepository
import com.example.cruelty_free_app.domain.usecase.CrueltyFreeUseCase

@Composable
fun ProductScreen(
    barcode: String,
    useCase: CrueltyFreeUseCase,
    scanRepository: ScanRepository,
    onBackClick: () -> Unit
) {
    val vm: ProductViewModel = viewModel(
        factory = ProductViewModel.factory(useCase, scanRepository, barcode)
    )
    val state by vm.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
            }
            Text("Produit", fontSize = 20.sp)
        }

        when (val s = state) {
            is ProductUiState.Loading        -> LoadingContent()
            is ProductUiState.Success        -> SuccessContent(s)
            is ProductUiState.ProductNotFound -> ErrorContent("Produit introuvable pour ce code-barres.")
            is ProductUiState.AliasUnknown    -> ErrorContent("Marque non reconnue dans notre base.")
            is ProductUiState.BrandNotFound   -> ErrorContent("Marque absente de notre base de données.")
            is ProductUiState.UnknownError    -> ErrorContent("Une erreur inattendue s'est produite.")
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SuccessContent(state: ProductUiState.Success) {
    val isNonCrueltyFree = state.brand.nonCrueltyFree
    val statusText = if (isNonCrueltyFree) "Non cruelty-free" else "Cruelty-free"
    val statusColor = if (isNonCrueltyFree) Color(0xFFB71C1C) else Color(0xFF2E7D32)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = state.product.title,
            fontSize = 22.sp,
            style = MaterialTheme.typography.headlineSmall
        )

        if (state.product.images.isNotEmpty()) {
            AsyncImage(
                model = state.product.images.first(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isNonCrueltyFree) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = statusText,
                fontSize = 18.sp,
                color = statusColor,
                modifier = Modifier.padding(16.dp)
            )
        }

        Text("Marque : ${state.brand.brandName}", fontSize = 16.sp)

        if (state.product.category.isNotBlank()) {
            Text("Catégorie : ${state.product.category}", fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        if (state.product.description.isNotBlank()) {
            HorizontalDivider()
            Text(state.product.description, fontSize = 14.sp)
        }

    }
}

@Composable
private fun ErrorContent(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
