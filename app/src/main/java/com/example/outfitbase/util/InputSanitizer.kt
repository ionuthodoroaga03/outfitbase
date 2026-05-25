package com.example.outfitbase.util

object InputSanitizer {
    fun sanitizeSearchQuery(query: String): String {
        return query
            .replace(Regex("\\s{2,}"), " ")
            .take(60)
    }

    fun sanitizeStoredText(text: String, maxLength: Int = 120): String {
        return text
            .trim()
            .replace(Regex("\\s{2,}"), " ")
            .take(maxLength)
    }

    fun sanitizePhone(phone: String): String {
        return phone
            .filter { character -> character.isDigit() || character == '+' || character == ' ' }
            .trim()
            .take(20)
    }
}
