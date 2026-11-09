package com.dawg.games.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dawg.games.model.Game

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val id: String,
    val title: String,
    val packageName: String,
    val category: String,
    val description: String,
    val rating: Float,
    val downloads: String,
    val iconUrl: String,
    val bannerUrl: String,
    val developer: String,
    val isFavorite: Boolean,
    val isCustom: Boolean
) {
    fun toDomain(): Game = Game(
        id = id,
        title = title,
        packageName = packageName,
        category = category,
        description = description,
        rating = rating,
        downloads = downloads,
        iconUrl = iconUrl,
        bannerUrl = bannerUrl,
        developer = developer,
        isFavorite = isFavorite,
        isCustom = isCustom
    )

    companion object {
        fun fromDomain(game: Game): GameEntity = GameEntity(
            id = game.id,
            title = game.title,
            packageName = game.packageName,
            category = game.category,
            description = game.description,
            rating = game.rating,
            downloads = game.downloads,
            iconUrl = game.iconUrl,
            bannerUrl = game.bannerUrl,
            developer = game.developer,
            isFavorite = game.isFavorite,
            isCustom = game.isCustom
        )
    }
}
