package com.niderkvel.iskraapispring.controllers

import com.niderkvel.iskraapispring.ELEMENTS_ON_PAGE
import com.niderkvel.iskraapispring.forms.PageResponse
import com.niderkvel.iskraapispring.models.UserAnswer
import com.niderkvel.iskraapispring.models.UserTask
import com.niderkvel.iskraapispring.models.UserTaskStatus
import com.niderkvel.iskraapispring.repositories.LevelRepository
import com.niderkvel.iskraapispring.repositories.UserAnswerRepository
import com.niderkvel.iskraapispring.repositories.UserRepository
import com.niderkvel.iskraapispring.repositories.UserTaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.math.round

@RestController
@CrossOrigin
@RequestMapping("/user-task")
class UserTaskController {

    @Autowired
    lateinit var userTaskRepo: UserTaskRepository

    @Autowired
    lateinit var userAnswerRepo: UserAnswerRepository

    @Autowired
    lateinit var userRepo: UserRepository

    @Autowired
    lateinit var levelRepo: LevelRepository

    @GetMapping(path = ["", "/"])
    fun all(
        @RequestParam(defaultValue = "1") page: Int
    ): PageResponse<List<UserTask>> {
        val content = userTaskRepo.findAll(
            Pageable.ofSize(ELEMENTS_ON_PAGE)
                .withPage(page - 1)
        )

        return PageResponse(
            content = content.content.toList(),
            totalPages = content.totalPages,
            currentPage = content.number
        )
    }

    @GetMapping("/waiting")
    fun allWaiting(
        @RequestParam(defaultValue = "1") page: Int
    ): PageResponse<List<UserTask>> {
        val content = userTaskRepo.findAllByStatus(
            UserTaskStatus.WAITING_TO_CHECK,
            Pageable.ofSize(ELEMENTS_ON_PAGE)
                .withPage(page - 1)
        )

        return PageResponse(
            content = content.content.toList(),
            totalPages = content.totalPages,
            currentPage = content.number
        )
    }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: Int
    ): ResponseEntity<UserTask> {

        val ut = userTaskRepo.findById(id)

        if (ut.isEmpty) return ResponseEntity(HttpStatus.NOT_FOUND)

        return ResponseEntity(
            ut.get(), HttpStatus.OK
        )
    }

    @GetMapping("/user/{id}")
    fun allByUser(
        @PathVariable id: Int
    ): ResponseEntity<List<UserTask>> {

        if (userRepo.findById(id).isEmpty)
            return ResponseEntity(HttpStatus.NOT_FOUND)

        return ResponseEntity(
            userTaskRepo.findAllByUser(id),
            HttpStatus.OK
        )
    }

    @PostMapping(path = ["", "/"])
    fun create(
        @RequestBody userTask: UserTask
    ): ResponseEntity<UserTask?> {

        val answers = mutableListOf<UserAnswer>()

        userTask.answers.forEach { ans ->
            answers.add(
                userAnswerRepo.save(
                    UserAnswer(
                        subtask = ans.subtask,
                        answers = ans.answers,
                        writtenAnswer = ans.writtenAnswer
                    )
                )
            )
        }

        kotlin.runCatching {
            userTaskRepo.save(
                UserTask(
                    answers = answers,
                    task = userTask.task,
                    result = userTask.result,
                    user = userTask.user,
                    status = UserTaskStatus.WAITING_TO_CHECK
                )
            )
        }
            .onFailure {
                return ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
            }
            .onSuccess {
                return ResponseEntity(it, HttpStatus.OK)
            }

        return ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @PutMapping(path = ["", "/"])
    fun update(
        @RequestBody userTask: UserTask
    ): ResponseEntity<Boolean> {
        kotlin.runCatching {
            userTask.status = UserTaskStatus.CHECKED

            levelRepo.findByUser(userTask.user.id)?.let {
                var current = it.current
                var toNext = it.expToNext
                var gotExp = (userTask.result * 0.1f * userTask.task.reward).toInt()

                while (gotExp > 0) {
                    if (toNext <= gotExp) {
                        gotExp -= toNext
                        toNext = 0
                    } else {
                        toNext -= gotExp
                        gotExp = 0
                    }

                    if (toNext == 0) {
                        current++
                        toNext = current * 100
                    }
                }

                levelRepo.save(
                    it.copy(
                        current = current,
                        expToNext = toNext
                    )
                )
            }

            userTaskRepo.save(
                userTask.copy(
                    status = UserTaskStatus.CHECKED
                )
            )
        }
            .onFailure {
                return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
            }
            .onSuccess {
                return ResponseEntity(true, HttpStatus.OK)
            }

        return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Int
    ): ResponseEntity<Boolean> {
        kotlin.runCatching {
            userTaskRepo.deleteById(id)
        }
            .onFailure {
                return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
            }
            .onSuccess {
                return ResponseEntity(true, HttpStatus.OK)
            }

        return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}