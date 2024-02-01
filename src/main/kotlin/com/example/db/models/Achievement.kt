package com.example.db.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Achievement(
    val title: String,
    val photoUrl: String
)

class AchievementEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AchievementEntity>(AchievementTable)

    var title by AchievementTable.title
    var photoUrl by AchievementTable.photoUrl

    fun toAchievement(): Achievement = Achievement(
        title = title,
        photoUrl = photoUrl
    )
}

object AchievementTable : IntIdTable("ACHIEVEMENTS") {
    val title = varchar(
        name = "title",
        length = 64,
    )
    val photoUrl = varchar(
        name = "photo_url",
        length = 64,
    )
}