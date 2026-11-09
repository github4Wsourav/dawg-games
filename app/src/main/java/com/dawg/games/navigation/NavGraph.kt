package com.dawg.games.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.dawg.games.ui.MainViewModel
import com.dawg.games.ui.screens.*
import com.dawg.games.ui.theme.GameBackground
import com.dawg.games.ui.theme.GamePrimary
import com.dawg.games.ui.theme.GameSurface
import com.dawg.games.ui.theme.GameTextSecondary

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        Screen.Dashboard,
        Screen.Library,
        Screen.Assistant
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in items.map { it.route }) {
                NavigationBar(
                    containerColor = GameSurface
                ) {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = GamePrimary,
                                unselectedIconColor = GameTextSecondary,
                                indicatorColor = GamePrimary.copy(alpha = 0.15f)
                            ),
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        },
        containerColor = GameBackground
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                val games by viewModel.allGames.collectAsState()
                DashboardScreen(
                    games = games,
                    onGameClick = { game ->
                        navController.navigate(Screen.GameDetail.createRoute(game.id))
                    },
                    onFavoriteClick = { game ->
                        viewModel.toggleFavorite(game)
                    },
                    onAddCustomGame = { title, category, desc ->
                        viewModel.addCustomGame(title, category, desc)
                    }
                )
            }

            composable(Screen.Library.route) {
                val progressList by viewModel.allProgress.collectAsState()
                val totalPlayTime = progressList.sumOf { it.totalPlayTimeMinutes }
                LibraryScreen(
                    progressList = progressList,
                    totalPlayTime = totalPlayTime,
                    onExportJson = { viewModel.exportProgressJson() },
                    onImportJson = { json -> viewModel.importProgressJson(json) }
                )
            }

            composable(Screen.Assistant.route) {
                val chatMessages by viewModel.chatMessages.collectAsState()
                AssistantScreen(
                    messages = chatMessages,
                    onSendMessage = { text -> viewModel.sendChatMessage(text) },
                    onClearHistory = { viewModel.clearChat() }
                )
            }

            composable(
                route = Screen.GameDetail.route,
                arguments = listOf(navArgument("gameId") { type = NavType.StringType })
            ) { backStackEntry ->
                val gameId = backStackEntry.arguments?.getString("gameId") ?: ""
                val games by viewModel.allGames.collectAsState()
                val progressList by viewModel.allProgress.collectAsState()

                val game = games.find { it.id == gameId }
                val progress = progressList.find { it.gameId == gameId }

                if (game != null) {
                    GameDetailScreen(
                        game = game,
                        progress = progress,
                        onBack = { navController.popBackStack() },
                        onSaveProgress = { updated -> viewModel.saveProgress(updated) },
                        onLogPlayTime = { minutes -> viewModel.logPlayTime(game.id, game.title, minutes) }
                    )
                }
            }
        }
    }
}
