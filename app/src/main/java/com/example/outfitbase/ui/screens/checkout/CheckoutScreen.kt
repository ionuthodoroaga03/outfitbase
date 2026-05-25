package com.example.outfitbase.ui.screens.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.outfitbase.OutfitBaseApplication
import com.example.outfitbase.util.PriceFormatter

@Composable
fun CheckoutScreen(
    onOrderConfirmed: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val application = LocalContext.current.applicationContext as OutfitBaseApplication
    val viewModel: CheckoutViewModel = viewModel(
        factory = CheckoutViewModelFactory(
            cartRepository = application.appContainer.cartRepository,
            orderRepository = application.appContainer.orderRepository
        )
    )
    val uiState by viewModel.uiState.collectAsState()

    CheckoutContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onCustomerNameChange = viewModel::updateCustomerName,
        onCustomerAddressChange = viewModel::updateCustomerAddress,
        onCustomerPhoneChange = viewModel::updateCustomerPhone,
        onPlaceOrder = { viewModel.placeOrder(onOrderConfirmed) }
    )
}

@Composable
private fun CheckoutContent(
    uiState: CheckoutUiState,
    onNavigateBack: () -> Unit,
    onCustomerNameChange: (String) -> Unit,
    onCustomerAddressChange: (String) -> Unit,
    onCustomerPhoneChange: (String) -> Unit,
    onPlaceOrder: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TextButton(onClick = onNavigateBack) {
                Text("Back")
            }
        }
        item {
            Text(
                text = "Checkout",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            CheckoutForm(
                uiState = uiState,
                onCustomerNameChange = onCustomerNameChange,
                onCustomerAddressChange = onCustomerAddressChange,
                onCustomerPhoneChange = onCustomerPhoneChange
            )
        }
        item {
            OrderSummary(uiState = uiState)
        }
        if (uiState.errorMessage != null) {
            item {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        item {
            Button(
                onClick = onPlaceOrder,
                enabled = !uiState.isSubmitting,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.isSubmitting) "Placing order..." else "Place order")
            }
        }
    }
}

@Composable
private fun CheckoutForm(
    uiState: CheckoutUiState,
    onCustomerNameChange: (String) -> Unit,
    onCustomerAddressChange: (String) -> Unit,
    onCustomerPhoneChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = uiState.customerName,
            onValueChange = onCustomerNameChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Name") }
        )
        OutlinedTextField(
            value = uiState.customerAddress,
            onValueChange = onCustomerAddressChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Address") }
        )
        OutlinedTextField(
            value = uiState.customerPhone,
            onValueChange = onCustomerPhoneChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Phone") }
        )
    }
}

@Composable
private fun OrderSummary(uiState: CheckoutUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Order summary",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        uiState.cartItems.forEach { cartItem ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${cartItem.quantity} x ${cartItem.name}")
                Text(PriceFormatter.format(cartItem.price * cartItem.quantity))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = PriceFormatter.format(uiState.totalPrice),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
