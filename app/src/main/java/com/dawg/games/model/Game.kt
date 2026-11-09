package com.dawg.games.model

data class Game(
    val id: String,
    val title: String,
    val packageName: String,
    val category: String,
    val description: String,
    val rating: Float = 4.5f,
    val downloads: String = "100K+",
    val iconUrl: String = "",
    val bannerUrl: String = "",
    val developer: String = "Community Dev",
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false
)

enum class GameCategory(val displayName: String) {
    ALL("All Games"),
    ACTION("Action"),
    STRATEGY("Strategy"),
    CASUAL("Casual"),
    RPG("RPG"),
    OFFLINE("Offline"),
    RETRO("Retro")
}
