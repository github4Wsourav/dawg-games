package com.dawg.games.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games ORDER BY title ASC")
    fun getAllGames(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE id = :id LIMIT 1")
    suspend fun getGameById(id: String): GameEntity?

    @Query("SELECT * FROM games WHERE isFavorite = 1")
    fun getFavoriteGames(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE category = :category")
    fun getGamesByCategory(category: String): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<GameEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity)

    @Update
    suspend fun updateGame(game: GameEntity)

    @Delete
    suspend fun deleteGame(game: GameEntity)

    @Query("SELECT COUNT(*) FROM games")
    suspend fun getGameCount(): Int
}
