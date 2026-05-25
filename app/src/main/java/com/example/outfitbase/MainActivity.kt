package com.example.outfitbase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.outfitbase.ui.OutfitBaseApp
import com.example.outfitbase.ui.theme.OutfitbaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OutfitbaseTheme {
                OutfitBaseApp()
            }
        }
    }
}