package com.dawg.games.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dawg.games.model.ChatMessage
import com.dawg.games.model.MessageSender

@Entity(tableName = "chat_messages")
data class ChatEntity(
    @PrimaryKey val id: String,
    val sender: String,
    val content: String,
    val timestamp: Long,
    val suggestedGamesJson: String,
    val followUpsJson: String
) {
    fun toDomain(): ChatMessage = ChatMessage(
        id = id,
        sender = if (sender == MessageSender.USER.name) MessageSender.USER else MessageSender.GENIE_ASSISTANT,
        content = content,
        timestamp = timestamp,
        suggestedGames = if (suggestedGamesJson.isEmpty()) emptyList() else suggestedGamesJson.split(",").map { it.trim() },
        followUps = if (followUpsJson.isEmpty()) emptyList() else followUpsJson.split("|").map { it.trim() }
    )

    companion object {
        fun fromDomain(msg: ChatMessage): ChatEntity = ChatEntity(
            id = msg.id,
            sender = msg.sender.name,
            content = msg.content,
            timestamp = msg.timestamp,
            suggestedGamesJson = msg.suggestedGames.joinToString(","),
            followUpsJson = msg.followUps.joinToString("|")
        )
    }
}
