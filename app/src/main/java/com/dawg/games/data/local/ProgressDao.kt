package com.dawg.games.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM game_progress ORDER BY lastPlayedTimestamp DESC")
    fun getAllProgress(): Flow<List<ProgressEntity>>

    @Query("SELECT * FROM game_progress WHERE gameId = :gameId LIMIT 1")
    suspend fun getProgressForGame(gameId: String): ProgressEntity?

    @Query("SELECT * FROM game_progress WHERE isCompleted = 1")
    fun getCompletedGames(): Flow<List<ProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: ProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(progressList: List<ProgressEntity>)

    @Update
    suspend fun updateProgress(progress: ProgressEntity)

    @Delete
    suspend fun deleteProgress(progress: ProgressEntity)

    @Query("SELECT SUM(totalPlayTimeMinutes) FROM game_progress")
    suspend fun getTotalPlayTime(): Long?
}
