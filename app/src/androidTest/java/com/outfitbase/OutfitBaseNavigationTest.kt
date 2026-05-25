package com.outfitbase

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OutfitBaseNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun userCanOpenSearchScreen() {
        composeRule.onNode(hasText("Search") and hasClickAction()).performClick()

        composeRule.onNodeWithText("Search products").assertIsDisplayed()
    }

    @Test
    fun userCanOpenProfileAndSettings() {
        composeRule.onNode(hasText("Profile") and hasClickAction()).performClick()

        composeRule.onNodeWithTag("profile_title").assertIsDisplayed()
        composeRule.onNodeWithTag("profile_settings_button").performClick()
        composeRule.onNodeWithTag("settings_title").assertIsDisplayed()
        composeRule.onNodeWithTag("settings_back_button").performClick()
        composeRule.onNodeWithTag("profile_title").assertIsDisplayed()
    }
}
