package com.niderkvel.iskraapispring

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

fun encodePassword(password: String): String =
    BCryptPasswordEncoder().encode(password)

fun matchPasswords(rawPassword: String, encodedPassword: String): Boolean =
    BCryptPasswordEncoder().matches(rawPassword, encodedPassword)