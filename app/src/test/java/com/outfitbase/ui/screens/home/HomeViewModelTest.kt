package com.outfitbase.ui.screens.home

import com.outfitbase.testing.FakeProductRepository
import com.outfitbase.testing.MainDispatcherRule
import com.outfitbase.testing.testProduct
import com.outfitbase.util.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadProductsShowsProductsAndCategories() = runTest {
        val products = listOf(
            testProduct(id = 1, name = "Shirt", category = "men's clothing"),
            testProduct(id = 2, name = "Dress", category = "women's clothing")
        )
        val repository = FakeProductRepository(
            products = products,
            categories = listOf("men's clothing", "women's clothing")
        )

        val viewModel = HomeViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success<*>)
        val catalog = (state as UiState.Success<HomeCatalog>).data
        assertEquals(products, catalog.products)
        assertEquals(listOf("men's clothing", "women's clothing"), catalog.categories)
        assertNull(catalog.selectedCategory)
    }

    @Test
    fun loadProductsShowsErrorWhenRepositoryFails() = runTest {
        val repository = FakeProductRepository(error = IllegalStateException("Network failed"))

        val viewModel = HomeViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Error)
        assertEquals("Network failed", (state as UiState.Error).message)
    }

    @Test
    fun selectCategoryShowsFilteredProducts() = runTest {
        val products = listOf(
            testProduct(id = 1, name = "Shirt", category = "men's clothing"),
            testProduct(id = 2, name = "Dress", category = "women's clothing")
        )
        val repository = FakeProductRepository(
            products = products,
            categories = listOf("men's clothing", "women's clothing")
        )

        val viewModel = HomeViewModel(repository)
        advanceUntilIdle()
        viewModel.selectCategory("women's clothing")
        advanceUntilIdle()

        val state = viewModel.uiState.value as UiState.Success<HomeCatalog>
        assertEquals(listOf(products[1]), state.data.products)
        assertEquals("women's clothing", state.data.selectedCategory)
    }
}
