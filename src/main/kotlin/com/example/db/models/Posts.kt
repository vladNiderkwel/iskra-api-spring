package com.example.db.models

import com.example.db.query
import com.example.plugins.LocalDateSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime


@Serializable
data class Post(
    val id: Int = -1,
    val title: String,
    val body: String,
    val photoUrl: String,
    @Serializable(with = LocalDateSerializer::class)
    val publicationDate: LocalDateTime
)

class PostEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PostEntity>(PostTable)

    var title by PostTable.title
    var body by PostTable.body
    var photoUrl by PostTable.photoUrl
    var publicationDate by PostTable.publicationDate

    fun toPost(): Post = Post(
        id = id.value,
        title = title,
        body = body,
        photoUrl = photoUrl,
        publicationDate = publicationDate,
    )
}

object PostTable : IntIdTable("POSTS") {
    val title = varchar(
        name = "title",
        length = 64,
    )
    val body = text("body")
    val photoUrl = varchar(
        name = "photo_url",
        length = 64,
    )
    val publicationDate = datetime("publication_date")
}

class PostController {
    suspend fun create(post: Post): Int = query {
        PostEntity.new {
            title = post.title
            body = post.body
            photoUrl = post.photoUrl
            publicationDate = post.publicationDate
        }.id.value
    }

    suspend fun all(): List<Post> = query {
        PostEntity
            .all()
            .map { it.toPost() }
    }

    suspend fun find(id: Int): Post? = query {
        PostEntity.findById(id)?.toPost()
    }

    suspend fun update(id: Int, post: Post) = query {
        PostEntity.findById(id)?.let {
            it.title = post.title
            it.body = post.body
            it.photoUrl = post.photoUrl
            it.publicationDate = post.publicationDate
        }
    }

    suspend fun delete(id: Int) = query {
        PostEntity.findById(id)?.delete()
    }
}