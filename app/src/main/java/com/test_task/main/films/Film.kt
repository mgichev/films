package com.test_task.main.films

data class Film (
    val kinopoiskId: Int,
    val nameRu: String,
    val nameOriginal: String,
    val genres: List<Genre>,
    val countries: List<Country>,
    val year: String,
    val ratingKinopoisk: String,
    val posterUrlPreview: String,
    val coverUrl: String,
    val description: String = "",
    val webUrl: String,
    val startYear: String,
    val endYear: String,
)

data class Genre (
    val genre: String
)

data class Country (
    val country: String
)

data class FrameItem (
    val items: List<Frame>
)

data class Frame (
    val previewUrl: String
)

