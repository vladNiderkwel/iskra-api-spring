package com.niderkvel.iskraapispring.controllers

import com.niderkvel.iskraapispring.forms.LevelForm
import com.niderkvel.iskraapispring.models.Level
import com.niderkvel.iskraapispring.repositories.LevelRepository
import com.niderkvel.iskraapispring.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/level")
class LevelController {

    @Autowired
    lateinit var userRepo: UserRepository

    @Autowired
    lateinit var levelRepo: LevelRepository

    @GetMapping("/")
    fun all(): List<Level> =
        levelRepo.findAll().take(10).sortedByDescending { it.current }

    @GetMapping("/{id}")
    fun getByUserId(
        @PathVariable id: Int
    ): Level? =
        levelRepo.findByUser(id)

    @PutMapping(
        path = ["", "/"],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun update(
        @RequestPart exp: LevelForm
    ) {
        val user = userRepo.findById(exp.userId)

        if (user.isEmpty) { return }

        val level = levelRepo.findByUser(exp.userId) ?: return

        while (exp.exp > 0) {
            if (level.expToNext <= exp.exp) {
                exp.exp -= level.expToNext
                level.current++
                level.expToNext = level.current * 100
            }
        }

        levelRepo.save(level)
    }

    @DeleteMapping("/{id}")
    fun deleteById(
        @PathVariable id: Int
    ) =
        userRepo.deleteById(id)
}