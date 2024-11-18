package com.niderkvel.iskraapispring.repositories

import com.niderkvel.iskraapispring.TABLE_POST
import com.niderkvel.iskraapispring.models.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : CrudRepository<Post, Int> {

    @Query("SELECT p FROM $TABLE_POST p WHERE p.title LIKE :q OR p.description LIKE :q")
    fun findAll(@Param("q") query: String, page: Pageable): Page<Post>

    @Query("SELECT * FROM $TABLE_POST ORDER BY views DESC LIMIT 5", nativeQuery = true)
    fun topByViews(): List<Post>
}