package com.outfitbase.util

import org.junit.Assert.assertEquals
import org.junit.Test

class InputSanitizerTest {
    @Test
    fun sanitizeSearchQueryCollapsesRepeatedSpaces() {
        val result = InputSanitizer.sanitizeSearchQuery("red     dress")

        assertEquals("red dress", result)
    }

    @Test
    fun sanitizeStoredTextTrimsAndLimitsLength() {
        val result = InputSanitizer.sanitizeStoredText("   Main     Street Apartment   ", maxLength = 11)

        assertEquals("Main Street", result)
    }

    @Test
    fun sanitizePhoneKeepsOnlyPhoneCharacters() {
        val result = InputSanitizer.sanitizePhone("+40 abc 712-345-678")

        assertEquals("+40 712345678", result)
    }
}
