package com.niderkvel.iskraapispring.forms

data class PageResponse<T>(
    val currentPage: Int,
    val totalPages: Int,
    val content: T
)