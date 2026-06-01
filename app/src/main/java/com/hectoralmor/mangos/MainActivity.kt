package com.hectoralmor.mangos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hectoralmor.mangos.ui.screens.principal.AppNavigation
import com.hectoralmor.mangos.ui.theme.CorporativoDeMangosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CorporativoDeMangosTheme {
                AppNavigation()
            }
        }
    }
}