package com.dawg.games.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dawg.games.model.ChatMessage
import com.dawg.games.model.MessageSender
import com.dawg.games.ui.theme.*

@Composable
fun ChatBubble(
    message: ChatMessage,
    onFollowUpClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isUser = message.sender == MessageSender.USER

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        // Label
        Text(
            text = if (isUser) "You" else "GameGenie Assistant",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isUser) GameSecondary else GamePrimary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) GamePrimary else GameSurface,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = GameTextPrimary
                )

                if (message.suggestedGames.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Featured Titles:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GameAccentGold
                    )
                    message.suggestedGames.forEach { gameTitle ->
                        Text(
                            text = "🎮 $gameTitle",
                            fontSize = 12.sp,
                            color = GameTextPrimary,
                            modifier = Modifier.padding(vertical = 1.dp)
                        )
                    }
                }
            }
        }

        // Follow up suggestions
        if (message.followUps.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.padding(top = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                message.followUps.forEach { prompt ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = GameSurfaceVariant,
                        modifier = Modifier.clickable { onFollowUpClick(prompt) }
                    ) {
                        Text(
                            text = prompt,
                            fontSize = 11.sp,
                            color = GameSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
