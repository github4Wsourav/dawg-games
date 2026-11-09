package com.dawg.games.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [GameEntity::class, ProgressEntity::class, ChatEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun progressDao(): ProgressDao
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dawg_games_local.db"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDefaultDatabaseValues(database.gameDao(), database.progressDao())
                    }
                }
            }
        }

        suspend fun populateDefaultDatabaseValues(gameDao: GameDao, progressDao: ProgressDao) {
            val defaultGames = listOf(
                GameEntity(
                    id = "game_1",
                    title = "Cyber Odyssey",
                    packageName = "com.dawg.cyberodyssey",
                    category = "Action",
                    description = "An open-world cyberpunk action adventure with high-octane vehicular combat and dynamic storyline.",
                    rating = 4.8f,
                    downloads = "500K+",
                    iconUrl = "",
                    bannerUrl = "",
                    developer = "Neon Frontier Studio",
                    isFavorite = true,
                    isCustom = false
                ),
                GameEntity(
                    id = "game_2",
                    title = "Kingdom Tactics",
                    packageName = "com.dawg.kingdomtactics",
                    category = "Strategy",
                    description = "Turn-based tactical strategy game. Command medieval legions, besiege castles, and conquer territories.",
                    rating = 4.6f,
                    downloads = "250K+",
                    iconUrl = "",
                    bannerUrl = "",
                    developer = "Crown Games",
                    isFavorite = true,
                    isCustom = false
                ),
                GameEntity(
                    id = "game_3",
                    title = "Cosmic Drifter",
                    packageName = "com.dawg.cosmicdrifter",
                    category = "Casual",
                    description = "Relaxing gravity-assisted cosmic arcade game. Drift across solar systems without internet required.",
                    rating = 4.9f,
                    downloads = "1M+",
                    iconUrl = "",
                    bannerUrl = "",
                    developer = "Astral Works",
                    isFavorite = false,
                    isCustom = false
                ),
                GameEntity(
                    id = "game_4",
                    title = "Shadow Realm RPG",
                    packageName = "com.dawg.shadowrealm",
                    category = "RPG",
                    description = "Deep dark-fantasy roguelike role-playing game with dungeon crawling and procedural loot.",
                    rating = 4.7f,
                    downloads = "320K+",
                    iconUrl = "",
                    bannerUrl = "",
                    developer = "Obsidian Forge",
                    isFavorite = false,
                    isCustom = false
                ),
                GameEntity(
                    id = "game_5",
                    title = "Pixel Racer Retro",
                    packageName = "com.dawg.pixelracer",
                    category = "Retro",
                    description = "Classic 16-bit arcade racer with customizable retro cars and split-screen local challenges.",
                    rating = 4.5f,
                    downloads = "750K+",
                    iconUrl = "",
                    bannerUrl = "",
                    developer = "BitHop Labs",
                    isFavorite = true,
                    isCustom = false
                ),
                GameEntity(
                    id = "game_6",
                    title = "Zen Garden Solitaire",
                    packageName = "com.dawg.zengarden",
                    category = "Offline",
                    description = "Tranquil puzzle cards simulator designed for completely offline play anywhere on the go.",
                    rating = 4.8f,
                    downloads = "900K+",
                    iconUrl = "",
                    bannerUrl = "",
                    developer = "Mindful Play",
                    isFavorite = false,
                    isCustom = false
                )
            )

            gameDao.insertAll(defaultGames)

            // Pre-seed local progress (Custom Change 1: offline default progression values)
            val defaultProgress = listOf(
                ProgressEntity(
                    gameId = "game_1",
                    gameTitle = "Cyber Odyssey",
                    totalPlayTimeMinutes = 185,
                    completionPercentage = 42,
                    lastPlayedTimestamp = System.currentTimeMillis() - 3600000 * 2,
                    isCompleted = false,
                    userNotes = "Reachable checkpoint 4 in District 9.",
                    unlockedAchievements = 5,
                    totalAchievements = 12
                ),
                ProgressEntity(
                    gameId = "game_2",
                    gameTitle = "Kingdom Tactics",
                    totalPlayTimeMinutes = 340,
                    completionPercentage = 75,
                    lastPlayedTimestamp = System.currentTimeMillis() - 3600000 * 24,
                    isCompleted = false,
                    userNotes = "Siege on Western fortress completed.",
                    unlockedAchievements = 9,
                    totalAchievements = 15
                ),
                ProgressEntity(
                    gameId = "game_5",
                    gameTitle = "Pixel Racer Retro",
                    totalPlayTimeMinutes = 120,
                    completionPercentage = 100,
                    lastPlayedTimestamp = System.currentTimeMillis() - 3600000 * 48,
                    isCompleted = true,
                    userNotes = "Unlocked golden turbo championship car!",
                    unlockedAchievements = 10,
                    totalAchievements = 10
                )
            )
            progressDao.insertAll(defaultProgress)
        }
    }
}
