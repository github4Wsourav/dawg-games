package com.dawg.games.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Gamepad)
    object Library : Screen("library", "Library", Icons.Default.ListAlt)
    object Assistant : Screen("assistant", "GameGenie", Icons.Default.SmartToy)
    object GameDetail : Screen("game_detail/{gameId}", "Details", Icons.Default.Gamepad) {
        fun createRoute(gameId: String) = "game_detail/$gameId"
    }
}
