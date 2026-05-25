package com.example.outfitbase.ui.screens.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.outfitbase.OutfitBaseApplication
import com.example.outfitbase.domain.model.Product
import com.example.outfitbase.util.PriceFormatter
import com.example.outfitbase.util.UiState

@Composable
fun ProductDetailScreen(
    productId: Int,
    onNavigateBack: () -> Unit,
    onAddToCart: () -> Unit
) {
    val application = LocalContext.current.applicationContext as OutfitBaseApplication
    val viewModel: ProductDetailViewModel = viewModel(
        key = productId.toString(),
        factory = ProductDetailViewModelFactory(
            productId = productId,
            productRepository = application.appContainer.productRepository,
            cartRepository = application.appContainer.cartRepository
        )
    )
    val uiState by viewModel.uiState.collectAsState()
    val quantity by viewModel.quantity.collectAsState()
    val selectedSize by viewModel.selectedSize.collectAsState()
    val selectedColor by viewModel.selectedColor.collectAsState()
    val showAddedMessage by viewModel.showAddedMessage.collectAsState()

    when (val state = uiState) {
        UiState.Loading -> ProductLoadingContent(onNavigateBack = onNavigateBack)
        is UiState.Error -> ProductErrorContent(
            message = state.message,
            onNavigateBack = onNavigateBack,
            onRetry = viewModel::loadProduct
        )
        is UiState.Success -> ProductDetailContent(
            product = state.data,
            quantity = quantity,
            selectedSize = selectedSize,
            selectedColor = selectedColor,
            showAddedMessage = showAddedMessage,
            onNavigateBack = onNavigateBack,
            onSizeSelected = viewModel::selectSize,
            onColorSelected = viewModel::selectColor,
            onIncreaseQuantity = viewModel::increaseQuantity,
            onDecreaseQuantity = viewModel::decreaseQuantity,
            onAddToCart = {
                viewModel.addSelectedProductToCart(onAddToCart)
            }
        )
    }
}

@Composable
private fun ProductDetailContent(
    product: Product,
    quantity: Int,
    selectedSize: String,
    selectedColor: String,
    showAddedMessage: Boolean,
    onNavigateBack: () -> Unit,
    onSizeSelected: (String) -> Unit,
    onColorSelected: (String) -> Unit,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onAddToCart: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TextButton(onClick = onNavigateBack) {
                Text("Back")
            }
        }
        item {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Fit
            )
        }
        item {
            ProductHeader(product = product)
        }
        item {
            HorizontalDivider()
        }
        item {
            OptionSelector(
                title = "Size",
                options = product.sizes,
                selectedOption = selectedSize,
                onOptionSelected = onSizeSelected
            )
        }
        item {
            OptionSelector(
                title = "Color",
                options = product.colors,
                selectedOption = selectedColor,
                onOptionSelected = onColorSelected
            )
        }
        item {
            QuantitySelector(
                quantity = quantity,
                onIncreaseQuantity = onIncreaseQuantity,
                onDecreaseQuantity = onDecreaseQuantity
            )
        }
        item {
            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to cart")
            }
        }
        if (showAddedMessage) {
            item {
                Text(
                    text = "Product added to cart.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun ProductHeader(product: Product) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = product.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = product.category,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = PriceFormatter.format(product.price),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Rating ${product.rating} / 5"
        )
        Text(
            text = "Stock: ${product.stock}"
        )
        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun OptionSelector(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { option ->
                if (option == selectedOption) {
                    Button(onClick = { onOptionSelected(option) }) {
                        Text(option)
                    }
                } else {
                    OutlinedButton(onClick = { onOptionSelected(option) }) {
                        Text(option)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuantitySelector(
    quantity: Int,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Quantity",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = onDecreaseQuantity) {
                Text("-")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = quantity.toString(),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(onClick = onIncreaseQuantity) {
                Text("+")
            }
        }
    }
}

@Composable
private fun ProductLoadingContent(
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = onNavigateBack,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(120.dp))
        CircularProgressIndicator()
    }
}

@Composable
private fun ProductErrorContent(
    message: String,
    onNavigateBack: () -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = onNavigateBack,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(120.dp))
        Text(message)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(onClick = onRetry) {
            Text("Retry")
        }
    }
}
