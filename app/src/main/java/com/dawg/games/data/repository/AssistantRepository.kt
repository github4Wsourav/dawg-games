package com.dawg.games.data.repository

import com.dawg.games.data.local.ChatDao
import com.dawg.games.data.local.ChatEntity
import com.dawg.games.model.ChatMessage
import com.dawg.games.model.MessageSender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AssistantRepository(private val chatDao: ChatDao) {

    val chatHistory: Flow<List<ChatMessage>> = chatDao.getAllMessages().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun sendUserMessage(text: String): ChatMessage {
        val userMsg = ChatMessage(
            sender = MessageSender.USER,
            content = text
        )
        chatDao.insertMessage(ChatEntity.fromDomain(userMsg))

        // Clean-room generation of response (can also connect to remote LLM endpoint)
        val assistantReply = generateAssistantReply(text)
        chatDao.insertMessage(ChatEntity.fromDomain(assistantReply))

        return assistantReply
    }

    suspend fun clearHistory() {
        chatDao.clearHistory()
    }

    private fun generateAssistantReply(userQuery: String): ChatMessage {
        val query = userQuery.lowercase()
        return when {
            "offline" in query || "casual" in query -> {
                ChatMessage(
                    sender = MessageSender.GENIE_ASSISTANT,
                    content = "For offline and casual gaming on the go, check out **Cosmic Drifter** and **Zen Garden Solitaire**. They require zero internet connection and track your progress 100% locally on your device!",
                    suggestedGames = listOf("Cosmic Drifter", "Zen Garden Solitaire"),
                    followUps = listOf("Show me strategy games", "Check my local playtime stats")
                )
            }
            "strategy" in query || "tactics" in query -> {
                ChatMessage(
                    sender = MessageSender.GENIE_ASSISTANT,
                    content = "**Kingdom Tactics** is your top pick! Command medieval legions, upgrade castle garrisons, and execute turn-based campaigns without pay-to-win microtransactions.",
                    suggestedGames = listOf("Kingdom Tactics"),
                    followUps = listOf("How to beat District 9 in Cyber Odyssey?", "Offline games")
                )
            }
            "rpg" in query || "action" in query || "cyber" in query -> {
                ChatMessage(
                    sender = MessageSender.GENIE_ASSISTANT,
                    content = "Try **Cyber Odyssey** for fast-paced action and **Shadow Realm RPG** for dark fantasy dungeon crawling and deep progression.",
                    suggestedGames = listOf("Cyber Odyssey", "Shadow Realm RPG"),
                    followUps = listOf("Recommend offline games", "How do I backup progress to Git?")
                )
            }
            "sync" in query || "backup" in query || "repo" in query || "export" in query -> {
                ChatMessage(
                    sender = MessageSender.GENIE_ASSISTANT,
                    content = "DAWG Games saves all your progress locally in Room SQLite. You can go to **My Library > Export JSON**, copy the payload, and commit it to your git repository or sync across any internet-connected device.",
                    suggestedGames = emptyList(),
                    followUps = listOf("Open My Library", "Add custom game")
                )
            }
            else -> {
                ChatMessage(
                    sender = MessageSender.GENIE_ASSISTANT,
                    content = "Hi! I'm GameGenie, your clean-room local gaming assistant. I can help you discover games, view local stats, find offline titles, and manage your progress without ads or trackers.",
                    suggestedGames = listOf("Cyber Odyssey", "Kingdom Tactics"),
                    followUps = listOf("Offline casual games", "Best strategy games", "Show my progress")
                )
            }
        }
    }
}
