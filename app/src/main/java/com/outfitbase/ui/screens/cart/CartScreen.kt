package com.outfitbase.ui.screens.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.outfitbase.OutfitBaseApplication
import com.outfitbase.domain.model.CartItem
import com.outfitbase.util.PriceFormatter

@Composable
fun CartScreen(
    onNavigateToCheckout: () -> Unit
) {
    val application = LocalContext.current.applicationContext as OutfitBaseApplication
    val viewModel: CartViewModel = viewModel(
        factory = CartViewModelFactory(application.appContainer.cartRepository)
    )
    val uiState by viewModel.uiState.collectAsState()

    CartContent(
        uiState = uiState,
        onNavigateToCheckout = onNavigateToCheckout,
        onIncreaseQuantity = viewModel::increaseQuantity,
        onDecreaseQuantity = viewModel::decreaseQuantity,
        onRemoveCartItem = viewModel::removeCartItem
    )
}

@Composable
private fun CartContent(
    uiState: CartUiState,
    onNavigateToCheckout: () -> Unit,
    onIncreaseQuantity: (CartItem) -> Unit,
    onDecreaseQuantity: (CartItem) -> Unit,
    onRemoveCartItem: (CartItem) -> Unit
) {
    if (uiState.cartItems.isEmpty()) {
        EmptyCartContent()
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = uiState.cartItems,
                key = { cartItem ->
                    "${cartItem.productId}-${cartItem.selectedSize}-${cartItem.selectedColor}"
                }
            ) { cartItem ->
                CartItemCard(
                    cartItem = cartItem,
                    onIncreaseQuantity = { onIncreaseQuantity(cartItem) },
                    onDecreaseQuantity = { onDecreaseQuantity(cartItem) },
                    onRemoveCartItem = { onRemoveCartItem(cartItem) }
                )
            }
        }
        CartSummary(
            totalPrice = uiState.totalPrice,
            onNavigateToCheckout = onNavigateToCheckout
        )
    }
}

@Composable
private fun CartItemCard(
    cartItem: CartItem,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemoveCartItem: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = cartItem.imageUrl,
                contentDescription = cartItem.name,
                modifier = Modifier.size(88.dp),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = cartItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text("${cartItem.selectedSize} / ${cartItem.selectedColor}")
                Text(
                    text = PriceFormatter.format(cartItem.price),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                QuantityRow(
                    quantity = cartItem.quantity,
                    onIncreaseQuantity = onIncreaseQuantity,
                    onDecreaseQuantity = onDecreaseQuantity
                )
                OutlinedButton(onClick = onRemoveCartItem) {
                    Text("Remove")
                }
            }
        }
    }
}

@Composable
private fun QuantityRow(
    quantity: Int,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(onClick = onDecreaseQuantity) {
            Text("-")
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(quantity.toString())
        Spacer(modifier = Modifier.width(12.dp))
        OutlinedButton(onClick = onIncreaseQuantity) {
            Text("+")
        }
    }
}

@Composable
private fun CartSummary(
    totalPrice: Double,
    onNavigateToCheckout: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        HorizontalDivider()
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = PriceFormatter.format(totalPrice),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onNavigateToCheckout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Checkout")
        }
    }
}

@Composable
private fun EmptyCartContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Your cart is empty.")
    }
}
