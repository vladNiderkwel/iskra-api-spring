package com.niderkvel.iskraapispring.controllers

import com.niderkvel.iskraapispring.models.Event
import com.niderkvel.iskraapispring.models.Post
import com.niderkvel.iskraapispring.repositories.EventRepository
import com.niderkvel.iskraapispring.repositories.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping("/stats")
class StatsController {

    @Autowired
    lateinit var postRepo: PostRepository

    @Autowired
    lateinit var eventRepo: EventRepository

    @GetMapping("/post-views")
    fun getTopPostByViews(): List<Post> = postRepo.topByViews()

    @GetMapping("/event-members")
    fun getTopEventByMembers(): List<Event> =
        eventRepo.findAll().sortedByDescending { it.members.size }.take(5)
}