package com.dawg.games.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dawg.games.data.repository.AssistantRepository
import com.dawg.games.data.repository.GameRepository
import com.dawg.games.data.repository.ProgressRepository
import com.dawg.games.model.ChatMessage
import com.dawg.games.model.Game
import com.dawg.games.model.GameProgress
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val gameRepository: GameRepository,
    private val progressRepository: ProgressRepository,
    private val assistantRepository: AssistantRepository
) : ViewModel() {

    val allGames: StateFlow<List<Game>> = gameRepository.allGames.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allProgress: StateFlow<List<GameProgress>> = progressRepository.allProgress.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val chatMessages: StateFlow<List<ChatMessage>> = assistantRepository.chatHistory.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addCustomGame(title: String, category: String, desc: String) {
        viewModelScope.launch {
            val customGame = Game(
                id = "custom_" + System.currentTimeMillis(),
                title = title,
                packageName = "com.custom." + title.lowercase().replace(" ", ""),
                category = category,
                description = desc,
                rating = 5.0f,
                developer = "User Added",
                isCustom = true
            )
            gameRepository.addCustomGame(customGame)
        }
    }

    fun toggleFavorite(game: Game) {
        viewModelScope.launch {
            gameRepository.toggleFavorite(game)
        }
    }

    fun saveProgress(progress: GameProgress) {
        viewModelScope.launch {
            progressRepository.saveOrUpdateProgress(progress)
        }
    }

    fun logPlayTime(gameId: String, gameTitle: String, additionalMinutes: Long) {
        viewModelScope.launch {
            progressRepository.logPlayTime(gameId, gameTitle, additionalMinutes)
        }
    }

    suspend fun exportProgressJson(): String {
        return progressRepository.exportProgressToJson(allProgress.value)
    }

    fun importProgressJson(json: String) {
        viewModelScope.launch {
            progressRepository.importProgressFromJson(json)
        }
    }

    fun sendChatMessage(text: String) {
        viewModelScope.launch {
            assistantRepository.sendUserMessage(text)
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            assistantRepository.clearHistory()
        }
    }
}

class MainViewModelFactory(
    private val gameRepository: GameRepository,
    private val progressRepository: ProgressRepository,
    private val assistantRepository: AssistantRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(gameRepository, progressRepository, assistantRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
