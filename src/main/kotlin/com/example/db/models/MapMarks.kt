package com.example.db.models

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

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

class MapMarksController {
    private suspend fun <T> query(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(mark: MapMark): Int = query {
        MapMarkEntity.new {
            name = mark.name
            type = mark.type
            lat = mark.lat
            lon = mark.lon
        }.id.value
    }

    suspend fun all(): List<MapMark> = query {
        MapMarkEntity
            .all()
            .map { it.toMapMark() }
    }

    suspend fun find(id: Int): MapMark? = query {
        MapMarkEntity.findById(id)?.toMapMark()
    }

    suspend fun find(type: Byte): List<MapMark> = query {
        MapMarkEntity
            .find { MapMarkTable.type eq type }
            .map { it.toMapMark() }
    }

    suspend fun update(id: Int, mark: MapMark) = query {
        MapMarkEntity.findById(id)?.let {
            it.name = mark.name
            it.type = mark.type
            it.lat = mark.lat
            it.lon = mark.lon
        }
    }

    suspend fun delete(id: Int) = query {
        MapMarkEntity.findById(id)?.delete()
    }
}