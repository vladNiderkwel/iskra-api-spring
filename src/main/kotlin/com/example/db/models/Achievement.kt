package com.example.db.models

import com.example.db.query
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

@Serializable
data class Achievement(
    val id: Int = -1,
    val title: String,
    val photoUrl: String
)

class AchievementEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AchievementEntity>(AchievementTable)

    var title by AchievementTable.title
    var photoUrl by AchievementTable.photoUrl

    fun toAchievement(): Achievement = Achievement(
        id = id.value,
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

class AchievementController {
    suspend fun create(achv: Achievement): Int = query {
        AchievementEntity.new {
            title = achv.title
            photoUrl = achv.photoUrl
        }.id.value
    }

    suspend fun all(): List<Achievement> = query {
        AchievementEntity
            .all()
            .map { it.toAchievement() }
    }

    suspend fun find(id: Int): Achievement? = query {
        AchievementEntity.findById(id)?.toAchievement()
    }

    suspend fun update(id: Int, achv: Achievement) = query {
        AchievementEntity.findById(id)?.let {
            it.title = achv.title
            it.photoUrl = achv.photoUrl
        }
    }

    suspend fun delete(id: Int) = query {
        AchievementEntity.findById(id)?.delete()
    }
}