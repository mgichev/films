package com.test_task.main.network_services

import com.test_task.main.films.Film

data class FilmRequest(
    val items: List<Film>,
    val totalPages: Int
)