package com.dawg.games.data.repository

import com.dawg.games.data.local.GameDao
import com.dawg.games.data.local.GameEntity
import com.dawg.games.model.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameRepository(private val gameDao: GameDao) {

    val allGames: Flow<List<Game>> = gameDao.getAllGames().map { list ->
        list.map { it.toDomain() }
    }

    val favoriteGames: Flow<List<Game>> = gameDao.getFavoriteGames().map { list ->
        list.map { it.toDomain() }
    }

    fun getGamesByCategory(category: String): Flow<List<Game>> {
        return gameDao.getGamesByCategory(category).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getGameById(id: String): Game? {
        return gameDao.getGameById(id)?.toDomain()
    }

    suspend fun addCustomGame(game: Game) {
        gameDao.insertGame(GameEntity.fromDomain(game.copy(isCustom = true)))
    }

    suspend fun toggleFavorite(game: Game) {
        val updated = game.copy(isFavorite = !game.isFavorite)
        gameDao.updateGame(GameEntity.fromDomain(updated))
    }

    suspend fun deleteGame(game: Game) {
        gameDao.deleteGame(GameEntity.fromDomain(game))
    }
}
