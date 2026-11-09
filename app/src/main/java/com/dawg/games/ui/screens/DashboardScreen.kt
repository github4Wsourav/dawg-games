package com.dawg.games.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dawg.games.model.Game
import com.dawg.games.model.GameCategory
import com.dawg.games.ui.components.CategoryChip
import com.dawg.games.ui.components.GameCard
import com.dawg.games.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    games: List<Game>,
    onGameClick: (Game) -> Unit,
    onFavoriteClick: (Game) -> Unit,
    onAddCustomGame: (title: String, category: String, desc: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(GameCategory.ALL.displayName) }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredGames = remember(games, searchQuery, selectedCategory) {
        games.filter { game ->
            val matchesCategory = selectedCategory == GameCategory.ALL.displayName ||
                    game.category.equals(selectedCategory, ignoreCase = true)
            val matchesSearch = game.title.contains(searchQuery, ignoreCase = true) ||
                    game.description.contains(searchQuery, ignoreCase = true) ||
                    game.developer.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "DAWG Games",
                            fontWeight = FontWeight.Bold,
                            color = GameTextPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CloudOff,
                                contentDescription = null,
                                tint = GameSuccess,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "100% Local • Zero Cloud Lock-in",
                                fontSize = 11.sp,
                                color = GameTextSecondary
                            )
                        }
                    }
                },
                actions = {
                    // CUSTOM ACTION BUTTON (Custom Feature Requirement 2)
                    Button(
                        onClick = { showAddDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GamePrimary),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Custom Game",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add Game",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GameBackground
                )
            )
        },
        containerColor = GameBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search Input
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search catalog & local games...", color = GameTextSecondary) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = GameSecondary
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = GameSurface,
                        unfocusedContainerColor = GameSurface,
                        focusedBorderColor = GamePrimary,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = GameTextPrimary,
                        unfocusedTextColor = GameTextPrimary
                    ),
                    singleLine = true
                )
            }

            // Category Chips Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GameCategory.values().forEach { cat ->
                        CategoryChip(
                            name = cat.displayName,
                            isSelected = selectedCategory == cat.displayName,
                            onSelect = { selectedCategory = cat.displayName }
                        )
                    }
                }
            }

            // Section Title
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedCategory == GameCategory.ALL.displayName) "Featured Games" else selectedCategory,
                        style = MaterialTheme.typography.titleMedium,
                        color = GameTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${filteredGames.size} available",
                        fontSize = 12.sp,
                        color = GameTextSecondary
                    )
                }
            }

            // Game Items
            items(filteredGames, key = { it.id }) { game ->
                GameCard(
                    game = game,
                    onGameClick = onGameClick,
                    onFavoriteClick = onFavoriteClick
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Custom Add Game Dialog (Custom Change 1 & 2)
    if (showAddDialog) {
        var title by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Action") }
        var desc by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = "Add Custom Local Game",
                    fontWeight = FontWeight.Bold,
                    color = GameTextPrimary
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Register any custom game or offline title into your local DAWG library:",
                        fontSize = 12.sp,
                        color = GameTextSecondary
                    )
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Game Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category (e.g. Action, RPG, Retro)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Description") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onAddCustomGame(title.trim(), category.trim(), desc.trim())
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GamePrimary)
                ) {
                    Text("Save to Local DB")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = GameTextSecondary)
                }
            },
            containerColor = GameSurface
        )
    }
}
