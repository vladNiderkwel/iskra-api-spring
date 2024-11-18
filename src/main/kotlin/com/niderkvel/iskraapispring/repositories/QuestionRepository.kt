package com.niderkvel.iskraapispring.repositories

import com.niderkvel.iskraapispring.TABLE_QUESTION
import com.niderkvel.iskraapispring.models.Question
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository : CrudRepository<Question, Int> {

    @Query("SELECT qu FROM $TABLE_QUESTION qu WHERE qu.question LIKE :q")
    fun findAll(@Param("q") query: String, page: Pageable): Page<Question>

    @Query("SELECT qu FROM $TABLE_QUESTION qu JOIN FETCH qu.author a WHERE a.id = :userId")
    fun findByUser(@Param("userId") id: Int): List<Question>
}