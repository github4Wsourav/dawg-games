package com.dawg.games.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dawg.games.model.GameProgress
import com.dawg.games.ui.components.ProgressStatCard
import com.dawg.games.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    progressList: List<GameProgress>,
    totalPlayTime: Long,
    onExportJson: suspend () -> String,
    onImportJson: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showImportDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var exportedJsonText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val completedCount = progressList.count { it.isCompleted }
    val totalAchievementsUnlocked = progressList.sumOf { it.unlockedAchievements }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("My Local Library", fontWeight = FontWeight.Bold, color = GameTextPrimary)
                        Text(
                            "Synced with Local Room DB",
                            fontSize = 11.sp,
                            color = GameSuccess
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showExportDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Export JSON",
                            tint = GameSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GameBackground)
            )
        },
        containerColor = GameBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Stats Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProgressStatCard(
                        title = "Playtime",
                        value = "${totalPlayTime / 60}h ${totalPlayTime % 60}m",
                        subtitle = "Tracked 100% locally",
                        icon = Icons.Default.Timer,
                        modifier = Modifier.weight(1f)
                    )
                    ProgressStatCard(
                        title = "Completed",
                        value = "$completedCount / ${progressList.size}",
                        subtitle = "Unlocked titles",
                        icon = Icons.Default.CheckCircle,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Sync with Repo / Internet devices Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GameSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = GameSecondary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Git Repository & Device Sync",
                                style = MaterialTheme.typography.titleMedium,
                                color = GameTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Export your local gameplay progress as portable JSON to commit to your Git repository, or import it on any device connected to the internet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GameTextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { showExportDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = GamePrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Export JSON", fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = { showImportDialog = true },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = GameSecondary),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Import JSON", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Tracked Game Items
            item {
                Text(
                    text = "Tracked Games Progress",
                    style = MaterialTheme.typography.titleMedium,
                    color = GameTextPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (progressList.isEmpty()) {
                item {
                    Text(
                        text = "No gameplay logged yet. Open any game from the Dashboard to record your playtime!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GameTextSecondary
                    )
                }
            } else {
                items(progressList, key = { it.gameId }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = GameSurface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.gameTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = GameTextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (item.isCompleted) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = GameSuccess.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = "COMPLETED",
                                            color = GameSuccess,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Playtime: ${item.totalPlayTimeMinutes}m • Achievements: ${item.unlockedAchievements}/${item.totalAchievements}",
                                fontSize = 12.sp,
                                color = GameTextSecondary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            LinearProgressIndicator(
                                progress = { item.completionPercentage / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                color = if (item.isCompleted) GameSuccess else GamePrimary,
                                trackColor = GameSurfaceVariant
                            )

                            if (item.userNotes.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Notes: \"${item.userNotes}\"",
                                    fontSize = 12.sp,
                                    color = GameTextSecondary
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Export Dialog
    if (showExportDialog) {
        var jsonState by remember { mutableStateOf("Generating local progress export...") }
        LaunchedEffect(Unit) {
            jsonState = onExportJson()
        }

        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Export Progress JSON (Git / Internet Sync)", color = GameTextPrimary) },
            text = {
                Column {
                    Text(
                        "Copy this JSON to save it into your Git repository:",
                        fontSize = 12.sp,
                        color = GameTextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = jsonState,
                        onValueChange = {},
                        readOnly = true,
                        maxLines = 8,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("DAWG_Progress", jsonState)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Progress JSON copied to clipboard!", Toast.LENGTH_SHORT).show()
                        showExportDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GamePrimary)
                ) {
                    Text("Copy to Clipboard")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Close")
                }
            },
            containerColor = GameSurface
        )
    }

    // Import Dialog
    if (showImportDialog) {
        var importInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = { Text("Import Progress from Repo JSON", color = GameTextPrimary) },
            text = {
                Column {
                    Text(
                        "Paste the JSON from your repository to restore progress locally on this device:",
                        fontSize = 12.sp,
                        color = GameTextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = importInput,
                        onValueChange = { importInput = it },
                        placeholder = { Text("Paste JSON here...") },
                        maxLines = 8,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (importInput.isNotBlank()) {
                            try {
                                onImportJson(importInput.trim())
                                Toast.makeText(context, "Progress restored successfully!", Toast.LENGTH_SHORT).show()
                                showImportDialog = false
                            } catch (e: Exception) {
                                Toast.makeText(context, "Invalid JSON format: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GamePrimary)
                ) {
                    Text("Import to Local DB")
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = GameSurface
        )
    }
}
