package com.niderkvel.iskraapispring.controllers

import com.niderkvel.iskraapispring.ServiceConfig
import com.niderkvel.iskraapispring.forms.ServiceForm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/service")
class ServiceController {

    @Autowired
    lateinit var serviceConfig: ServiceConfig

    @GetMapping(
        path = ["", "/"]
    )
    fun isActive(
        @RequestParam id: Int
    ): ResponseEntity<Boolean> = when (id) {
        0 -> ResponseEntity(serviceConfig.questions, HttpStatus.OK)
        1 -> ResponseEntity(serviceConfig.tasks, HttpStatus.OK)
        2 -> ResponseEntity(serviceConfig.events, HttpStatus.OK)
        3 -> ResponseEntity(serviceConfig.map, HttpStatus.OK)
        else -> ResponseEntity(false, HttpStatus.NOT_FOUND)
    }

    @GetMapping("/all", produces = ["application/json"])
    fun all(): List<ServiceForm> = listOf(
        ServiceForm(0, serviceConfig.questions),
        ServiceForm(1, serviceConfig.tasks),
        ServiceForm(2, serviceConfig.events),
        ServiceForm(3, serviceConfig.map),
    )

    @GetMapping("/toggle")
    fun toggle(
        @RequestParam id: Int
    ): ResponseEntity<Boolean> = when (id) {
        0 -> {
            serviceConfig.questions = !serviceConfig.questions
            ResponseEntity(serviceConfig.questions, HttpStatus.OK)
        }

        1 -> {
            serviceConfig.tasks = !serviceConfig.tasks
            ResponseEntity(serviceConfig.tasks, HttpStatus.OK)
        }

        2 -> {
            serviceConfig.events = !serviceConfig.events
            ResponseEntity(serviceConfig.events, HttpStatus.OK)
        }

        3 -> {
            serviceConfig.map = !serviceConfig.map
            ResponseEntity(serviceConfig.map, HttpStatus.OK)
        }

        else -> ResponseEntity(false, HttpStatus.NOT_FOUND)
    }
}