package com.example.db.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class MapMark(
    val name: String,
    val type: Byte,
    val lat: Float,
    val lon: Float,
)

class MapMarkEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MapMarkEntity>(MapMarkTable)
    var name by MapMarkTable.name
    var type by MapMarkTable.type
    var lat by MapMarkTable.lat
    var lon by MapMarkTable.lon

    fun toMapMark(): MapMark = MapMark(
        name = name,
        type = type,
        lat = lat,
        lon = lon,
    )
}

object MapMarkTable : IntIdTable("MAP_MARKS") {
    val name = varchar(
        name = "name",
        length = 64,
    )
    val type = byte("type")
    val lat = float("lat")
    val lon = float("lon")
}