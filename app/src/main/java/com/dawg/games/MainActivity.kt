package com.dawg.games

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.dawg.games.navigation.AppNavigation
import com.dawg.games.ui.MainViewModel
import com.dawg.games.ui.MainViewModelFactory
import com.dawg.games.ui.theme.DAWGTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels {
        val app = application as DawgApplication
        MainViewModelFactory(
            app.gameRepository,
            app.progressRepository,
            app.assistantRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DAWGTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}
