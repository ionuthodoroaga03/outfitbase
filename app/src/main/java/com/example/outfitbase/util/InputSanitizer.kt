package com.example.outfitbase.util

object InputSanitizer {
    fun sanitizeSearchQuery(query: String): String {
        return query
            .replace(Regex("\\s{2,}"), " ")
            .take(60)
    }
}
