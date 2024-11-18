package com.niderkvel.iskraapispring.repositories

import com.niderkvel.iskraapispring.TABLE_TASK
import com.niderkvel.iskraapispring.models.Task
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : CrudRepository<Task, Int> {

    @Query("SELECT t FROM $TABLE_TASK t WHERE t.title LIKE :q")
    fun findAll(@Param("q") query: String, page: Pageable): Page<Task>
}