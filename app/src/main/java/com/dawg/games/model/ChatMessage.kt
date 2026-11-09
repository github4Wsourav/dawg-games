package com.dawg.games.model

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val suggestedGames: List<String> = emptyList(),
    val followUps: List<String> = emptyList()
)

enum class MessageSender {
    USER,
    GENIE_ASSISTANT
}
