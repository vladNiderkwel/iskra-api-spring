package com.niderkvel.iskraapispring.controllers

import com.niderkvel.iskraapispring.ELEMENTS_ON_PAGE
import com.niderkvel.iskraapispring.forms.PageResponse
import com.niderkvel.iskraapispring.models.Event
import com.niderkvel.iskraapispring.repositories.EventRepository
import com.niderkvel.iskraapispring.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/event")
class EventController {

    @Autowired
    lateinit var userRepo: UserRepository

    @Autowired
    lateinit var eventRepo: EventRepository

    @GetMapping(
        path = ["", "/"]
    )
    fun getAll(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "") query: String
    ): PageResponse<List<Event>> {
        val content = eventRepo.findAll(
            "%$query%",
            Pageable.ofSize(ELEMENTS_ON_PAGE).withPage(page - 1)
        )

        return PageResponse(
            content = content.content.toList(),
            totalPages = content.totalPages,
            currentPage = content.number
        )
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Int
    ): Event? =
        eventRepo.findByIdOrNull(id)

    @PostMapping(
        path = ["", "/"]
    )
    fun save(
        @RequestBody event: Event
    ): Int =
        eventRepo.save(event).id

    @DeleteMapping("/{id}")
    fun deleteById(
        @PathVariable id: Int
    ) =
        eventRepo.deleteById(id)

    @GetMapping("/toggle")
    fun toggleAttendant(
        @RequestParam eventId: Int,
        @RequestParam userId: Int
    ): Boolean {
        val event = eventRepo.findByIdOrNull(eventId) ?: return false

        val user = userRepo.findByIdOrNull(userId) ?: return false

        if (event.members.contains(user)) {
            eventRepo.save(
                Event(
                    id = eventId,
                    title = event.title,
                    description = event.description,
                    author = event.author,
                    endDate = event.endDate,
                    startDate = event.startDate,
                    members = event.members.filter { it.id != userId }
                )
            )
            return true
        } else {
            val list = event.members.toMutableList()
            list.add(user)

            eventRepo.save(
                Event(
                    id = eventId,
                    title = event.title,
                    description = event.description,
                    author = event.author,
                    endDate = event.endDate,
                    startDate = event.startDate,
                    members = list
                )
            )
        }
        return false
    }
}