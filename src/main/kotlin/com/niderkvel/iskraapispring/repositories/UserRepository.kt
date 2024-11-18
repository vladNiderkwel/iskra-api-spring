package com.niderkvel.iskraapispring.repositories

import com.niderkvel.iskraapispring.TABLE_USER
import com.niderkvel.iskraapispring.models.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Int> {

    @Query("SELECT u FROM $TABLE_USER u WHERE u.name LIKE :q OR u.email LIKE :q")
    fun findAll(@Param("q") query: String, page: Pageable): Page<User>

    @Query("SELECT * FROM $TABLE_USER WHERE email = :e", nativeQuery = true)
    fun findByEmail(@Param("e") email: String): User?
}