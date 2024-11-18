package com.niderkvel.iskraapispring.models

import com.niderkvel.iskraapispring.TABLE_EVENT
import jakarta.persistence.*
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Entity(name = TABLE_EVENT)
data class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,
    @ManyToOne
    val author: User,
    val title: String,
    @Column(columnDefinition = "TEXT")
    val description: String,
    @ManyToMany
    val members: List<User> = emptyList(),
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val startDate: LocalDateTime,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val endDate: LocalDateTime
)