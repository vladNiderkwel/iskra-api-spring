package com.niderkvel.iskraapispring.repositories

import com.niderkvel.iskraapispring.TABLE_LEVEL
import com.niderkvel.iskraapispring.models.Level
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface LevelRepository : CrudRepository<Level, Int> {

    @Query("SELECT l FROM $TABLE_LEVEL l JOIN FETCH l.user u WHERE u.id = :userId ")
    fun findByUser(@Param("userId") userId: Int): Level?
}