package com.example.outfitbase.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.outfitbase.OutfitBaseApplication
import com.example.outfitbase.domain.model.Language
import com.example.outfitbase.domain.model.Order
import com.example.outfitbase.domain.model.UserProfile
import com.example.outfitbase.util.PriceFormatter

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit
) {
    val application = LocalContext.current.applicationContext as OutfitBaseApplication
    val viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            userProfileRepository = application.appContainer.userProfileRepository,
            settingsRepository = application.appContainer.settingsRepository,
            orderRepository = application.appContainer.orderRepository
        )
    )
    val uiState by viewModel.uiState.collectAsState()

    ProfileContent(
        uiState = uiState,
        onNavigateToSettings = onNavigateToSettings
    )
}

@Composable
private fun ProfileContent(
    uiState: ProfileUiState,
    onNavigateToSettings: () -> Unit
) {
    val text = ProfileText.from(uiState.language)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = text.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            ProfileCard(
                userProfile = uiState.userProfile,
                text = text
            )
        }
        item {
            Button(
                onClick = onNavigateToSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text.settings)
            }
        }
        item {
            OrderHistory(
                orders = uiState.orders,
                text = text
            )
        }
    }
}

@Composable
private fun ProfileCard(
    userProfile: UserProfile,
    text: ProfileText
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProfileRow(label = text.name, value = userProfile.userName)
            ProfileRow(label = text.email, value = userProfile.email)
            ProfileRow(label = text.phone, value = userProfile.phone)
        }
    }
}

@Composable
private fun ProfileRow(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun OrderHistory(
    orders: List<Order>,
    text: ProfileText
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = text.orders,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        if (orders.isEmpty()) {
            Text(text.noOrders)
        } else {
            orders.forEach { order ->
                OrderCard(order = order)
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = order.createdAt,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(PriceFormatter.format(order.totalPrice))
            Text(order.items.joinToString { item -> "${item.quantity} x ${item.name}" })
        }
    }
}

private data class ProfileText(
    val title: String,
    val name: String,
    val email: String,
    val phone: String,
    val settings: String,
    val orders: String,
    val noOrders: String
) {
    companion object {
        fun from(language: Language): ProfileText {
            return when (language) {
                Language.ROMANIAN -> ProfileText(
                    title = "Profil",
                    name = "Nume",
                    email = "Email",
                    phone = "Telefon",
                    settings = "Setari",
                    orders = "Comenzi plasate",
                    noOrders = "Nu ai comenzi plasate."
                )
                Language.ENGLISH -> ProfileText(
                    title = "Profile",
                    name = "Name",
                    email = "Email",
                    phone = "Phone",
                    settings = "Settings",
                    orders = "Placed orders",
                    noOrders = "You have no placed orders."
                )
            }
        }
    }
}
