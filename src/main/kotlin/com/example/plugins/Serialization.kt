package com.example.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}

class LocalDateSerializer : KSerializer<LocalDateTime> {
    override val descriptor = PrimitiveSerialDescriptor("DATETIME", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: LocalDateTime) =
        encoder
            .encodeLong(
                value
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            )

    override fun deserialize(decoder: Decoder): LocalDateTime =
        Instant
            .ofEpochMilli(decoder.decodeLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
}
