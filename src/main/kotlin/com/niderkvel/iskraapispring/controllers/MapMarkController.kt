package com.niderkvel.iskraapispring.controllers

import com.niderkvel.iskraapispring.models.MapMark
import com.niderkvel.iskraapispring.repositories.MapMarkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/map-mark")
class MapMarkController {

    @Autowired
    lateinit var mapMarkRepo: MapMarkRepository

    @GetMapping(
        path = ["", "/"]
    )
    fun getAll(): List<MapMark> =
        mapMarkRepo.findAll().toList()

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Int
    ): MapMark? =
        mapMarkRepo.findByIdOrNull(id)

    @PostMapping(
        path = ["", "/"]
    )
    fun save(
        @RequestBody mapMark: MapMark
    ): Int =
        mapMarkRepo.save(mapMark).id

    @GetMapping("/delete/{id}")
    fun deleteById(
        @PathVariable id: Int
    ) {
        mapMarkRepo.deleteById(id)
    }
}