package com.dawg.games.model

data class GameProgress(
    val gameId: String,
    val gameTitle: String,
    val totalPlayTimeMinutes: Long = 0,
    val completionPercentage: Int = 0,
    val lastPlayedTimestamp: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false,
    val userNotes: String = "",
    val unlockedAchievements: Int = 0,
    val totalAchievements: Int = 10
)
