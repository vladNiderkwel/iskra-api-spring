package com.niderkvel.iskraapispring

fun randomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + '_'
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}