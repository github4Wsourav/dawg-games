package com.dawg.games.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dawg.games.model.Game
import com.dawg.games.ui.theme.*

@Composable
fun GameCard(
    game: Game,
    onGameClick: (Game) -> Unit,
    onFavoriteClick: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onGameClick(game) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = GameSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon placeholder / badge
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GameSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = game.title.take(2).uppercase(),
                    color = GameSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = game.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = GameTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (game.isCustom) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = GamePrimary.copy(alpha = 0.2f),
                            modifier = Modifier.padding(start = 6.dp)
                        ) {
                            Text(
                                text = "CUSTOM",
                                color = GamePrimary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${game.category} • ${game.developer}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GameTextSecondary,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = GameAccentGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = game.rating.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GameAccentGold
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "${game.downloads} plays",
                        fontSize = 11.sp,
                        color = GameTextSecondary
                    )
                }
            }

            IconButton(onClick = { onFavoriteClick(game) }) {
                Icon(
                    imageVector = if (game.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (game.isFavorite) Color.Red else GameTextSecondary
                )
            }
        }
    }
}
