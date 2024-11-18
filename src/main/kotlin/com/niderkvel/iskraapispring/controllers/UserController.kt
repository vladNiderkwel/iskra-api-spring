package com.niderkvel.iskraapispring.controllers

import com.niderkvel.iskraapispring.*
import com.niderkvel.iskraapispring.forms.LoginForm
import com.niderkvel.iskraapispring.forms.PageResponse
import com.niderkvel.iskraapispring.forms.UserForm
import com.niderkvel.iskraapispring.models.Level
import com.niderkvel.iskraapispring.models.User
import com.niderkvel.iskraapispring.repositories.LevelRepository
import com.niderkvel.iskraapispring.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@CrossOrigin
@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    lateinit var userRepo: UserRepository

    @Autowired
    lateinit var levelRepo: LevelRepository

    @GetMapping(
        path = ["", "/"]
    )
    fun getAll(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "") query: String
    ): PageResponse<List<User>> {
        val content = userRepo.findAll(
            "%$query%",
            Pageable.ofSize(ELEMENTS_ON_PAGE).withPage(page - 1)
        )

        return PageResponse(
            content = content.content.toList(),
            totalPages = content.totalPages,
            currentPage = page
        )
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Int
    ): User? =
        userRepo.findByIdOrNull(id)

    @GetMapping("/email/{email}")
    fun getByEmail(
        @PathVariable email: String
    ): User? =
        userRepo.findByEmail(email)

    @PostMapping(
        path = ["", "/"],
    )
    fun create(
        @RequestBody userForm: UserForm
    ): User? {
        val usr = userRepo.findByEmail(userForm.email)

        if (usr != null) return null

        val newUser = userRepo.save(
            User(
                name = userForm.name,
                email = userForm.email,
                password = encodePassword(userForm.password),
            )
        )

        levelRepo.save(
            Level(
                user = newUser,
                current = 1,
                expToNext = 100
            )
        )

        return newUser
    }

    @PutMapping(
        path = ["", "/"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun update(
        @ModelAttribute user: User,
        @ModelAttribute photo: MultipartFile? = null
    ): ResponseEntity<Boolean> {
        val usEmail = userRepo.findByEmail(user.email)

        usEmail?.let {
            if (usEmail.id != user.id)
                return ResponseEntity(false, HttpStatus.CONFLICT)
        }

        var currentPhoto = user.photoUrl

        photo?.let {
            if (currentPhoto.length < 64) currentPhoto = randomString(64)
            saveFile(currentPhoto, it, PHOTO_TYPE_PHOTOS)
        }

        kotlin.runCatching {
            userRepo.save(
                User(
                    id = user.id,
                    name = user.name,
                    email = user.email,
                    password = user.password,
                    photoUrl = currentPhoto
                )
            )
        }.onFailure {
            return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
        }
            .onSuccess {
                return ResponseEntity(true, HttpStatus.OK)
            }

        return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @DeleteMapping("/{id}")
    fun deleteById(
        @PathVariable id: Int
    ): ResponseEntity<Boolean> {

        var user = userRepo.findByIdOrNull(id) ?: return ResponseEntity(false, HttpStatus.NOT_FOUND)

        if (user.photoUrl.length < 64) {
            deleteFile(user.photoUrl, PHOTO_TYPE_PHOTOS)
            user = user.copy(
                photoUrl = "photo_placeholder"
            )
        }

        user = user.copy(
            name = "Удаленный пользователь",
            email = "",
            password = "-1",
            isDeleted = true,
            isBlocked = true
        )

        kotlin.runCatching {
            userRepo.save(user)
        }
            .onFailure {
                return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
            }
            .onSuccess {
                return ResponseEntity(true, HttpStatus.OK)
            }

        return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody form: LoginForm
    ): ResponseEntity<User?> {
        val user = userRepo.findByEmail(form.email) ?:
            return ResponseEntity(null, HttpStatus.NOT_FOUND)

        if (matchPasswords(form.password, user.password))
            return ResponseEntity(user, HttpStatus.OK)

        return ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @GetMapping("/block/{id}")
    fun toggleBlock(
        @PathVariable id: Int
    ): Boolean {
        val user = userRepo.findByIdOrNull(id) ?: return false

        user.isBlocked = !user.isBlocked

        return userRepo.save(user).isBlocked
    }
}