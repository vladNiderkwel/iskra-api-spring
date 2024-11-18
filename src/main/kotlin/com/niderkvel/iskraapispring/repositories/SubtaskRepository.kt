package com.niderkvel.iskraapispring.repositories

import com.niderkvel.iskraapispring.models.Subtask
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SubtaskRepository : CrudRepository<Subtask, Int>