package com.niderkvel.iskraapispring.repositories

import com.niderkvel.iskraapispring.TABLE_STAFF
import com.niderkvel.iskraapispring.models.Staff
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StaffRepository : CrudRepository<Staff, Int> {

    @Query("SELECT s FROM $TABLE_STAFF s WHERE s.name LIKE :q OR s.email LIKE :q")
    fun findAll(@Param("q") query: String, page: Pageable): Page<Staff>

    @Query("SELECT * FROM $TABLE_STAFF WHERE email = :e", nativeQuery = true)
    fun findByEmail(@Param("e") email: String): Staff?
}