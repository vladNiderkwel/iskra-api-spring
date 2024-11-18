package com.niderkvel.iskraapispring.repositories

import com.niderkvel.iskraapispring.models.MapMark
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MapMarkRepository : CrudRepository<MapMark, Int>