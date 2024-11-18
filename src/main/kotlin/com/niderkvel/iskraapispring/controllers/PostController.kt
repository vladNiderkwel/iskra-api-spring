package com.niderkvel.iskraapispring.controllers

import com.niderkvel.iskraapispring.*
import com.niderkvel.iskraapispring.forms.PageResponse
import com.niderkvel.iskraapispring.forms.PostForm
import com.niderkvel.iskraapispring.models.Post
import com.niderkvel.iskraapispring.repositories.PostRepository
import com.niderkvel.iskraapispring.repositories.StaffRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/post")
class PostController {

    @Autowired
    lateinit var postRepo: PostRepository

    @Autowired
    lateinit var staffRepo: StaffRepository

    @GetMapping(
        path = ["", "/"]
    )
    fun getAll(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "") query: String
    ): PageResponse<List<Post>> {
        val content = postRepo.findAll("%$query%", Pageable.ofSize(ELEMENTS_ON_PAGE).withPage(page - 1))

        return PageResponse(
            content = content.content.toList(),
            totalPages = content.totalPages,
            currentPage = content.number
        )
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Int
    ): ResponseEntity<Post?> {
        kotlin.runCatching {
            postRepo.findByIdOrNull(id)
        }
            .onFailure {
                return ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
            }
            .onSuccess {
                return ResponseEntity(it, HttpStatus.OK)
            }

        return ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @GetMapping("/views/{id}")
    fun increaseViews(
        @PathVariable id: Int
    ): ResponseEntity<Boolean> {
        kotlin.runCatching {
            val post = postRepo.findByIdOrNull(id)?:
                return ResponseEntity(false, HttpStatus.NOT_FOUND)
            post.views++
            postRepo.save(post)
        }
            .onFailure {
                return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
            }
            .onSuccess {
                return ResponseEntity(true, HttpStatus.OK)
            }

        return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @PostMapping(
        path = ["", "/"],
        consumes = [
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
        ]
    )
    fun save(
        @ModelAttribute postForm: PostForm
    ): ResponseEntity<Int> {
        var photoName = "post_placeholder"

        kotlin.runCatching {
            postForm.photo?.let {
                photoName = randomString(64)
                saveFile(photoName, it, PHOTO_TYPE_POSTS)
            }
        }.onFailure {
            return ResponseEntity(-1, HttpStatus.INTERNAL_SERVER_ERROR)
        }

        kotlin.runCatching {
            postRepo
                .save(
                    Post(
                        title = postForm.title,
                        description = postForm.description,
                        body = postForm.body,
                        publicationDate = postForm.publicationDate,
                        photoUrl = photoName,
                        author = staffRepo.findById(postForm.author).get()
                    )
                )
        }
            .onFailure {
                return ResponseEntity(-1, HttpStatus.INTERNAL_SERVER_ERROR)
            }
            .onSuccess {
                return ResponseEntity(it.id, HttpStatus.OK)
            }

        return ResponseEntity(-1, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @DeleteMapping("/{id}")
    fun deleteById(
        @PathVariable id: Int
    ): ResponseEntity<Boolean> {
        val post = postRepo.findByIdOrNull(id) ?: return ResponseEntity(false, HttpStatus.OK)

        kotlin.runCatching {
            if (post.photoUrl.length == 64)
                deleteFile(post.photoUrl, PHOTO_TYPE_POSTS)

            postRepo.delete(post)
        }.onFailure {
            return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
        }.onSuccess {
            return ResponseEntity(true, HttpStatus.OK)
        }

        return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}