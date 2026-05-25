package com.outfitbase.ui.screens.cart

import com.outfitbase.domain.model.CartItem
import com.outfitbase.testing.FakeCartRepository
import com.outfitbase.testing.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun cartTotalIsCalculatedFromItems() = runTest {
        val repository = FakeCartRepository(
            initialItems = listOf(
                testCartItem(productId = 1, price = 10.0, quantity = 2),
                testCartItem(productId = 2, price = 15.0, quantity = 1)
            )
        )
        val viewModel = CartViewModel(repository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { }
        }

        advanceUntilIdle()

        assertEquals(35.0, viewModel.uiState.value.totalPrice, 0.0)
    }

    @Test
    fun increaseQuantityUpdatesCartItem() = runTest {
        val cartItem = testCartItem(productId = 1, price = 10.0, quantity = 2)
        val repository = FakeCartRepository(initialItems = listOf(cartItem))
        val viewModel = CartViewModel(repository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { }
        }

        viewModel.increaseQuantity(cartItem)
        advanceUntilIdle()

        assertEquals(3, viewModel.uiState.value.cartItems.first().quantity)
        assertEquals(30.0, viewModel.uiState.value.totalPrice, 0.0)
    }

    @Test
    fun decreaseQuantityBelowOneRemovesCartItem() = runTest {
        val cartItem = testCartItem(productId = 1, price = 10.0, quantity = 1)
        val repository = FakeCartRepository(initialItems = listOf(cartItem))
        val viewModel = CartViewModel(repository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { }
        }

        viewModel.decreaseQuantity(cartItem)
        advanceUntilIdle()

        assertEquals(emptyList<CartItem>(), viewModel.uiState.value.cartItems)
        assertEquals(0.0, viewModel.uiState.value.totalPrice, 0.0)
    }

    private fun testCartItem(
        productId: Int,
        price: Double,
        quantity: Int
    ): CartItem {
        return CartItem(
            productId = productId,
            name = "Test item $productId",
            price = price,
            imageUrl = "https://example.com/item.png",
            category = "test",
            quantity = quantity,
            selectedSize = "M",
            selectedColor = "Black"
        )
    }
}
