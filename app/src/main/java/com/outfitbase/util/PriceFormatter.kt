package com.outfitbase.util

import java.util.Locale

object PriceFormatter {
    fun format(price: Double): String {
        return String.format(Locale.US, "$%.2f", price)
    }
}
