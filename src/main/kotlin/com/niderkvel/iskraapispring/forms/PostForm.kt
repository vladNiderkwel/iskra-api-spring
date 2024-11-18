package com.niderkvel.iskraapispring.forms

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class PostForm(
    val title: String,
    val description: String,
    val body: String,
    val photo: MultipartFile? = null,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val publicationDate: LocalDateTime = LocalDateTime.now(),
    val author: Int,
)