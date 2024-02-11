package com.example.db.models

import com.example.db.query
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SizedIterable

@Serializable
data class UserAchievement(
    val id: Int = -1,
    val user: User,
    val achievements: List<Achievement>
)

class UserAchievementEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserAchievementEntity>(UserAchievementTable)

    var user by UserEntity referencedOn UserAchievementTable.user
    var achievements by AchievementEntity via AchievementTable

    fun toUserAchievement(): UserAchievement = UserAchievement(
        id = id.value,
        user = user.toUser(),
        achievements = achievements.map { it.toAchievement() }
    )
}

object UserAchievementTable : IntIdTable("USER_ACHIEVEMENTS") {
    val user = reference("user", UserTable)
    val achievements = reference("achievements", AchievementTable)
}

class UserAchievementController {
    suspend fun create(uAchv: UserAchievement): Int = query {
        val achievementsList = mutableListOf<AchievementEntity>()

        uAchv.achievements.forEach {
            achievementsList.add(
                AchievementEntity.findById(it.id)!!
            )
        }

        UserAchievementEntity.new {
            user = UserEntity.findById(uAchv.user.id)!!
            achievements = SizedCollection(achievementsList)
        }.id.value
    }

    suspend fun all(): List<UserAchievement> = query {
        UserAchievementEntity
            .all()
            .map { it.toUserAchievement() }
    }

    suspend fun find(id: Int): UserAchievement? = query {
        UserAchievementEntity.findById(id)?.toUserAchievement()
    }

    suspend fun find(user: User): UserAchievement? = query {
        UserAchievementEntity
            .find {
                UserAchievementTable.user eq UserEntity.findById(user.id)!!.id
            }
            .singleOrNull()
            ?.toUserAchievement()
    }

    suspend fun update(id: Int, uAchv: UserAchievement) = query {
        val achievementsList = mutableListOf<AchievementEntity>()

        uAchv.achievements.forEach {
            achievementsList.add(
                AchievementEntity.findById(it.id)!!
            )
        }

        UserAchievementEntity.findById(id)?.let {
            it.user = UserEntity.findById(uAchv.user.id)!!
            it.achievements = SizedCollection(achievementsList)
        }
    }

    suspend fun delete(id: Int) = query {
        UserAchievementEntity.findById(id)?.delete()
    }
}