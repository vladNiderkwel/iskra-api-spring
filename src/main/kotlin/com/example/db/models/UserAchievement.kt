package com.example.db.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class UserAchievement(
    val user: User,
    val achievements: List<Achievement>
)

class UserAchievementEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserAchievementEntity>(UserAchievementTable)

    var user by UserEntity referencedOn UserAchievementTable.user
    var achievements by AchievementEntity via AchievementTable

    fun toUserAchievement(): UserAchievement = UserAchievement(
        user = user.toUser(),
        achievements = achievements.map { it.toAchievement() }
    )
}

object UserAchievementTable : IntIdTable("USER_ACHIEVEMENTS") {
    val user = reference("user", UserTable)
    val achievements = reference("achievements", AchievementTable)
}