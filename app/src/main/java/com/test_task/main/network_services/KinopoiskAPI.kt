package com.test_task.main.network_services

import com.test_task.main.films.Film
import com.test_task.main.films.FrameItem
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface KinopoiskAPI {

    companion object {
        private const val API_KEY = "de1db718-950e-449d-88a1-39a41062cee6"
    }

    @Headers(
        "X-API-KEY: $API_KEY",
        "Content-Type: application/json"
    )
    @GET("api/v2.2/films")
    suspend fun getNumberOfPagesFilms(@Query("page") page: Int,
                                      @Query("order") order: String?,
                                      @Query("yearFrom") year: Int?) : FilmRequest

    @Headers(
        "X-API-KEY: $API_KEY",
        "Content-Type: application/json"
    )
    @GET("api/v2.2/films")
    suspend fun getNumberOfPagesFrames(@Query("id") filmId: Int) : FilmRequest

    @Headers(
        "X-API-KEY: $API_KEY",
        "Content-Type: application/json"
    )
    @GET("api/v2.2/films")
    suspend fun getFilms(@Query("page") page: Int,
                         @Query("order") order: String?,
                         @Query("yearFrom") year: Int?) : FilmRequest

    @Headers(
        "X-API-KEY: $API_KEY",
        "Content-Type: application/json"
    )
    @GET("api/v2.2/films/{id}")
    suspend fun getFilm(@Path("id") id: Int,) : Film

    @Headers(
        "X-API-KEY: $API_KEY",
        "Content-Type: application/json"
    )
    @GET("api/v2.2/films/{id}/images?type=STILL")
    suspend fun getFrames(@Path("id") id: Int, @Query("page") pageId: Int) : FrameItem
}