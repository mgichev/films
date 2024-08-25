package com.test_task.main.films

data class SortParams(
    val sortType: SortType,
    val year: Int? = null,
    val order: String? = null,
    val wordFilter: String? = null
)
