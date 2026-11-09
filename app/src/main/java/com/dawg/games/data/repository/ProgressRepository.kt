package com.dawg.games.data.repository

import com.dawg.games.data.local.ProgressDao
import com.dawg.games.data.local.ProgressEntity
import com.dawg.games.model.GameProgress
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProgressRepository(private val progressDao: ProgressDao) {

    private val gson = Gson()

    val allProgress: Flow<List<GameProgress>> = progressDao.getAllProgress().map { list ->
        list.map { it.toDomain() }
    }

    val completedGames: Flow<List<GameProgress>> = progressDao.getCompletedGames().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun getProgressForGame(gameId: String): GameProgress? {
        return progressDao.getProgressForGame(gameId)?.toDomain()
    }

    suspend fun saveOrUpdateProgress(progress: GameProgress) {
        progressDao.insertProgress(ProgressEntity.fromDomain(progress))
    }

    suspend fun logPlayTime(gameId: String, gameTitle: String, additionalMinutes: Long) {
        val existing = progressDao.getProgressForGame(gameId)?.toDomain() ?: GameProgress(
            gameId = gameId,
            gameTitle = gameTitle
        )
        val updated = existing.copy(
            totalPlayTimeMinutes = existing.totalPlayTimeMinutes + additionalMinutes,
            lastPlayedTimestamp = System.currentTimeMillis()
        )
        progressDao.insertProgress(ProgressEntity.fromDomain(updated))
    }

    suspend fun getTotalPlayTime(): Long {
        return progressDao.getTotalPlayTime() ?: 0L
    }

    /**
     * Export all local progress as JSON so user can commit/push it to repository
     * or sync across devices over internet without any proprietary cloud lock-in.
     */
    suspend fun exportProgressToJson(progressList: List<GameProgress>): String {
        return gson.toJson(progressList)
    }

    /**
     * Import progress from repository JSON string into local Room database.
     */
    suspend fun importProgressFromJson(jsonString: String) {
        val type = object : TypeToken<List<GameProgress>>() {}.type
        val importedList: List<GameProgress> = gson.fromJson(jsonString, type)
        val entities = importedList.map { ProgressEntity.fromDomain(it) }
        progressDao.insertAll(entities)
    }
}
