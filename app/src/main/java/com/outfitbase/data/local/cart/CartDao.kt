package com.outfitbase.data.local.cart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY name ASC")
    fun observeCartItems(): Flow<List<CartItemEntity>>

    @Query(
        "SELECT * FROM cart_items WHERE productId = :productId AND selectedSize = :selectedSize AND selectedColor = :selectedColor LIMIT 1"
    )
    suspend fun getCartItem(productId: Int, selectedSize: String, selectedColor: String): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCartItem(cartItem: CartItemEntity)

    @Query(
        "DELETE FROM cart_items WHERE productId = :productId AND selectedSize = :selectedSize AND selectedColor = :selectedColor"
    )
    suspend fun removeCartItem(productId: Int, selectedSize: String, selectedColor: String)

    @Query(
        "UPDATE cart_items SET quantity = :quantity WHERE productId = :productId AND selectedSize = :selectedSize AND selectedColor = :selectedColor"
    )
    suspend fun updateQuantity(productId: Int, selectedSize: String, selectedColor: String, quantity: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
