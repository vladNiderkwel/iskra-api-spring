package com.niderkvel.iskraapispring.controllers

import com.niderkvel.iskraapispring.ELEMENTS_ON_PAGE
import com.niderkvel.iskraapispring.forms.PageResponse
import com.niderkvel.iskraapispring.models.Question
import com.niderkvel.iskraapispring.repositories.QuestionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/question")
class QuestionController {

    @Autowired
    lateinit var questionRepo: QuestionRepository

    @GetMapping(
        path = ["", "/"]
    )
    fun getAll(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "") query: String
    ): PageResponse<List<Question>> {
        val content = questionRepo.findAll(
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
    ): Question? =
        questionRepo.findByIdOrNull(id)

    @GetMapping("/user/{id}")
    fun getByUser(
        @PathVariable id: Int
    ): List<Question> =
        questionRepo.findByUser(id)

    @PostMapping(
        path = ["", "/"]
    )
    fun save(
        @RequestBody question: Question
    ): Int =
        questionRepo.save(question).id

    @PutMapping(
        path = ["", "/"]
    )
    fun update(
        @RequestBody question: Question
    ): Int {
        return questionRepo.save(question).id
    }


    @DeleteMapping("/{id}")
    fun deleteById(
        @PathVariable id: Int
    ) =
        questionRepo.deleteById(id)
}