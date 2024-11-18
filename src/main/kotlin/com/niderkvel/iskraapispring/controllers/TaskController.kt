package com.niderkvel.iskraapispring.controllers

import com.niderkvel.iskraapispring.ELEMENTS_ON_PAGE
import com.niderkvel.iskraapispring.forms.PageResponse
import com.niderkvel.iskraapispring.models.Option
import com.niderkvel.iskraapispring.models.Subtask
import com.niderkvel.iskraapispring.models.Task
import com.niderkvel.iskraapispring.models.UserTask
import com.niderkvel.iskraapispring.repositories.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/task")
class TaskController {

    @Autowired
    lateinit var taskRepo: TaskRepository

    @Autowired
    lateinit var subtaskRepo: SubtaskRepository

    @Autowired
    lateinit var optionRepo: OptionRepository

    @Autowired
    lateinit var userTaskRepo: UserTaskRepository

    @Autowired
    lateinit var userAnswerRepo: UserAnswerRepository

    @GetMapping(
        path = ["", "/"]
    )
    fun getAll(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "") query: String
    ): PageResponse<List<Task>> {
        val content = taskRepo.findAll(
            "%$query%",
            Pageable.ofSize(ELEMENTS_ON_PAGE).withPage(page - 1)
        )

        return PageResponse(
            content = content.content,
            totalPages = content.totalPages,
            currentPage = content.number
        )
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Int
    ): Task? =
        taskRepo.findByIdOrNull(id)

    @PostMapping(
        path = ["", "/"]
    )
    fun save(
        @RequestBody task: Task
    ): Int {
        val subtaskList = mutableListOf<Subtask>()

        task.subtasks.forEach { tsk ->
            val options = mutableListOf<Option>()

            tsk.options.forEach { opt ->
                options.add(
                    optionRepo.save(
                        Option(
                            text = opt.text,
                            isAnswer = opt.isAnswer
                        )
                    )
                )
            }

            subtaskList.add(
                subtaskRepo.save(
                    Subtask(
                        question = tsk.question,
                        options = options,
                        type = tsk.type
                    )
                )
            )
        }

        return taskRepo.save(
            Task(
                title = task.title,
                subtasks = subtaskList,
                reward = task.reward
            )
        ).id
    }

    @GetMapping("/delete/{id}")
    fun deleteById(
        @PathVariable id: Int
    ): ResponseEntity<Boolean> {
        val task = taskRepo.findByIdOrNull(id) ?: return ResponseEntity(false, HttpStatus.NOT_FOUND)
        val ut = userTaskRepo.findAllByTask(id)

        kotlin.runCatching {
            task.available = false
            val subs = task.subtasks
            task.subtasks = emptyList()

            ut.forEach { userTask ->
                val answers = userTask.answers
                userTask.answers = emptyList()

                answers.forEach { userAnswer ->
                    userAnswerRepo.deleteById(userAnswer.id)
                }

                userTaskRepo.deleteById(userTask.id)
            }

            subs.forEach { subt ->
                subtaskRepo.delete(subt)
            }

            taskRepo.save(task)
        }
            .onFailure {
                println(it)
                return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
            }
            .onSuccess {
                return ResponseEntity(true, HttpStatus.OK)
            }

        return ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}