package com.example.db

const val INVALID_ID_FORMAT = "Неверный формат ID"
const val INVALID_EMAIL_FORMAT = "Неверный формат почты"

enum class TaskTypes(val value: Byte) {
    QUESTION(1),
    COMPOUND(2)
}