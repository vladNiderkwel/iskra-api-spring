package com.niderkvel.iskraapispring.models

import com.niderkvel.iskraapispring.TABLE_MAP_MARK
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = TABLE_MAP_MARK)
data class MapMark(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    val name: String,
    val type: Byte = MarkTypes.DEFAULT,
    val coordinates: Array<Float>,
)

object MarkTypes {
    const val DEFAULT: Byte = 0
    const val CLOTHES: Byte = 1
    const val BATTERY: Byte = 2
    const val SCRAP: Byte = 3
    const val GLASS: Byte = 4
    const val RECYCABLE: Byte = 5
    const val PLASTIC: Byte = 6
    const val APPLIANCES: Byte = 7
}