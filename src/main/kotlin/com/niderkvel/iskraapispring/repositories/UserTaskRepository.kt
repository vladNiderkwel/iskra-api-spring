package com.niderkvel.iskraapispring.repositories

import com.niderkvel.iskraapispring.TABLE_USER_TASK
import com.niderkvel.iskraapispring.models.UserTask
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserTaskRepository: CrudRepository<UserTask, Int> {

    @Query("SELECT ut FROM $TABLE_USER_TASK ut")
    fun findAll(page: Pageable): Page<UserTask>

    @Query("SELECT ut FROM $TABLE_USER_TASK ut WHERE ut.status = :status")
    fun findAllByStatus(@Param("status") status: Byte, page: Pageable): Page<UserTask>

    @Query("SELECT ut FROM $TABLE_USER_TASK ut JOIN FETCH ut.user u WHERE u.id = :userId ")
    fun findAllByUser(@Param("userId") userId: Int): List<UserTask>

    @Query("SELECT ut FROM $TABLE_USER_TASK ut JOIN FETCH ut.task t WHERE t.id = :taskId ")
    fun findAllByTask(@Param("taskId") taskId: Int): List<UserTask>
}