package com.dawg.games.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dawg.games.model.ChatMessage
import com.dawg.games.ui.components.ChatBubble
import com.dawg.games.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantScreen(
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val quickPrompts = listOf(
        "Offline casual games",
        "Best strategy games",
        "Recommend RPG games",
        "How to sync progress to Git?",
        "Check my local playtime stats"
    )

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = null,
                            tint = GameSecondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "GameGenie Assistant",
                                fontWeight = FontWeight.Bold,
                                color = GameTextPrimary
                            )
                            Text(
                                text = "Clean-Room AI • Zero Telemetry",
                                fontSize = 11.sp,
                                color = GameSuccess
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onClearHistory) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear Chat",
                            tint = GameTextSecondary
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
        ) {
            // Quick Prompts Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quickPrompts.forEach { prompt ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = GameSurfaceVariant,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        TextButton(
                            onClick = { onSendMessage(prompt) },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = prompt,
                                fontSize = 12.sp,
                                color = GameTextPrimary
                            )
                        }
                    }
                }
            }

            // Message Stream
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (messages.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.SmartToy,
                                    contentDescription = null,
                                    tint = GamePrimary,
                                    modifier = Modifier.size(56.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Welcome to GameGenie!",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = GameTextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Ask for game recommendations, offline titles, or help with local progress tracking.",
                                    fontSize = 12.sp,
                                    color = GameTextSecondary
                                )
                            }
                        }
                    }
                } else {
                    items(messages, key = { it.id }) { msg ->
                        ChatBubble(
                            message = msg,
                            onFollowUpClick = { followUp ->
                                onSendMessage(followUp)
                            }
                        )
                    }
                }
            }

            // Input Bar
            Surface(
                color = GameSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Ask GameGenie anything...", color = GameTextSecondary) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = GameSurfaceVariant,
                            unfocusedContainerColor = GameSurfaceVariant,
                            focusedBorderColor = GamePrimary,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = GameTextPrimary,
                            unfocusedTextColor = GameTextPrimary
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                val text = inputText.trim()
                                inputText = ""
                                onSendMessage(text)
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = GamePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send"
                        )
                    }
                }
            }
        }
    }
}
