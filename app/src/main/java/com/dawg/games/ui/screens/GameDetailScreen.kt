package com.dawg.games.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dawg.games.model.Game
import com.dawg.games.model.GameProgress
import com.dawg.games.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    game: Game,
    progress: GameProgress?,
    onBack: () -> Unit,
    onSaveProgress: (GameProgress) -> Unit,
    onLogPlayTime: (additionalMinutes: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var completion by remember(progress) { mutableFloatStateOf((progress?.completionPercentage ?: 0).toFloat()) }
    var notes by remember(progress) { mutableStateOf(progress?.userNotes ?: "") }
    var isCompleted by remember(progress) { mutableStateOf(progress?.isCompleted ?: false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(game.title, fontWeight = FontWeight.Bold, color = GameTextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = GameTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GameBackground)
            )
        },
        containerColor = GameBackground
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GameSurface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(GameSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = game.title.take(2).uppercase(),
                            color = GameSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = game.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = GameTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${game.category} • Developer: ${game.developer}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GameTextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = GameAccentGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${game.rating} (${game.downloads} plays)",
                                fontSize = 12.sp,
                                color = GameTextSecondary
                            )
                        }
                    }
                }
            }

            // Description
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GameSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "About Game",
                        style = MaterialTheme.typography.titleMedium,
                        color = GameTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = game.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = GameTextSecondary
                    )
                }
            }

            // Local Progress & Playtime Tracker (100% Local Saving)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GameSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Local Progress Tracker",
                            style = MaterialTheme.typography.titleMedium,
                            color = GameTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GameSuccess.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Offline Room DB",
                                color = GameSuccess,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Total Recorded Playtime: ${progress?.totalPlayTimeMinutes ?: 0} minutes",
                        color = GameSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Log Session Time:", fontSize = 12.sp, color = GameTextSecondary)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Button(
                            onClick = { onLogPlayTime(15) },
                            colors = ButtonDefaults.buttonColors(containerColor = GameSurfaceVariant)
                        ) {
                            Text("+15m", fontSize = 12.sp, color = GameTextPrimary)
                        }
                        Button(
                            onClick = { onLogPlayTime(30) },
                            colors = ButtonDefaults.buttonColors(containerColor = GameSurfaceVariant)
                        ) {
                            Text("+30m", fontSize = 12.sp, color = GameTextPrimary)
                        }
                        Button(
                            onClick = { onLogPlayTime(60) },
                            colors = ButtonDefaults.buttonColors(containerColor = GameSurfaceVariant)
                        ) {
                            Text("+1 hr", fontSize = 12.sp, color = GameTextPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Completion slider
                    Text(
                        text = "Completion: ${completion.toInt()}%",
                        color = GameTextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Slider(
                        value = completion,
                        onValueChange = { completion = it },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = GamePrimary,
                            activeTrackColor = GamePrimary
                        )
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isCompleted,
                            onCheckedChange = {
                                isCompleted = it
                                if (it) completion = 100f
                            },
                            colors = CheckboxDefaults.colors(checkedColor = GameSuccess)
                        )
                        Text(
                            text = "Mark game as fully completed",
                            color = GameTextPrimary,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Personal Gamer Notes / Checkpoints") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GamePrimary,
                            focusedTextColor = GameTextPrimary,
                            unfocusedTextColor = GameTextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val updated = (progress ?: GameProgress(
                                gameId = game.id,
                                gameTitle = game.title
                            )).copy(
                                completionPercentage = completion.toInt(),
                                isCompleted = isCompleted,
                                userNotes = notes.trim(),
                                lastPlayedTimestamp = System.currentTimeMillis()
                            )
                            onSaveProgress(updated)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GamePrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Progress Locally")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
