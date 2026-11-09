package com.dawg.games.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dawg.games.model.GameProgress

@Entity(tableName = "game_progress")
data class ProgressEntity(
    @PrimaryKey val gameId: String,
    val gameTitle: String,
    val totalPlayTimeMinutes: Long,
    val completionPercentage: Int,
    val lastPlayedTimestamp: Long,
    val isCompleted: Boolean,
    val userNotes: String,
    val unlockedAchievements: Int,
    val totalAchievements: Int
) {
    fun toDomain(): GameProgress = GameProgress(
        gameId = gameId,
        gameTitle = gameTitle,
        totalPlayTimeMinutes = totalPlayTimeMinutes,
        completionPercentage = completionPercentage,
        lastPlayedTimestamp = lastPlayedTimestamp,
        isCompleted = isCompleted,
        userNotes = userNotes,
        unlockedAchievements = unlockedAchievements,
        totalAchievements = totalAchievements
    )

    companion object {
        fun fromDomain(progress: GameProgress): ProgressEntity = ProgressEntity(
            gameId = progress.gameId,
            gameTitle = progress.gameTitle,
            totalPlayTimeMinutes = progress.totalPlayTimeMinutes,
            completionPercentage = progress.completionPercentage,
            lastPlayedTimestamp = progress.lastPlayedTimestamp,
            isCompleted = progress.isCompleted,
            userNotes = progress.userNotes,
            unlockedAchievements = progress.unlockedAchievements,
            totalAchievements = progress.totalAchievements
        )
    }
}
