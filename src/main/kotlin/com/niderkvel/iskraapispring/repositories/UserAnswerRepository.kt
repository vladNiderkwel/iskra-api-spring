package com.niderkvel.iskraapispring.repositories

import com.niderkvel.iskraapispring.models.UserAnswer
import org.springframework.data.repository.CrudRepository

interface UserAnswerRepository: CrudRepository<UserAnswer, Int>