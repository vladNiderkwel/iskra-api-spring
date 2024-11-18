package com.niderkvel.iskraapispring.models

import com.niderkvel.iskraapispring.TABLE_POST
import jakarta.persistence.*
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Entity(name = TABLE_POST)
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    val title: String,
    @Column(columnDefinition = "TEXT")
    val description: String,
    @Column(columnDefinition = "TEXT")
    val body: String,
    val photoUrl: String = "post_placeholder",
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val publicationDate: LocalDateTime,
    @ManyToOne
    val author: Staff,
    var views: Int = 0
)