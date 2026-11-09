package com.dawg.games

import android.app.Application
import com.dawg.games.data.local.AppDatabase
import com.dawg.games.data.repository.AssistantRepository
import com.dawg.games.data.repository.GameRepository
import com.dawg.games.data.repository.ProgressRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class DawgApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val gameRepository by lazy { GameRepository(database.gameDao()) }
    val progressRepository by lazy { ProgressRepository(database.progressDao()) }
    val assistantRepository by lazy { AssistantRepository(database.chatDao()) }

    override fun onCreate() {
        super.onCreate()
    }
}
