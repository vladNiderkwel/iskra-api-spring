package com.niderkvel.iskraapispring.repositories

import com.niderkvel.iskraapispring.models.Option
import org.springframework.data.repository.CrudRepository

interface OptionRepository: CrudRepository<Option, Int>