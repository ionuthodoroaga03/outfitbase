package com.outfitbase.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.outfitbase.data.local.cart.CartDao
import com.outfitbase.data.local.cart.CartItemEntity
import com.outfitbase.data.local.order.OrderDao
import com.outfitbase.data.local.order.OrderEntity
import com.outfitbase.data.local.order.OrderItemEntity

@Database(
    entities = [
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class OutfitBaseDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var instance: OutfitBaseDatabase? = null

        fun getInstance(context: Context): OutfitBaseDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    OutfitBaseDatabase::class.java,
                    "outfitbase.db"
                ).build().also { database ->
                    instance = database
                }
            }
        }
    }
}
