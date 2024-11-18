package com.niderkvel.iskraapispring.repositories

import com.niderkvel.iskraapispring.TABLE_EVENT
import com.niderkvel.iskraapispring.models.Event
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : CrudRepository<Event, Int> {

    @Query("SELECT e FROM $TABLE_EVENT e WHERE e.description LIKE :q OR e.title LIKE :q")
    fun findAll(@Param("q") query: String, page: Pageable): Page<Event>
}