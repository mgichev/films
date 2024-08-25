package com.test_task.main.network_services

import com.test_task.main.films.Film
import com.test_task.main.films.Frame
import com.test_task.main.films.FrameItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    private lateinit var retrofit: Retrofit
    private lateinit var kinopoiskAPI: KinopoiskAPI

    init {
        initRetrofit()
        kinopoiskAPI = retrofit.create(KinopoiskAPI::class.java)
    }

    companion object {
        @Volatile
        private var instance: RetrofitClient? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: RetrofitClient().also { instance = it }
            }
    }

    suspend fun getFilms(pageId: Int, order: String?, year: Int?) : List<Film>{
        val filmRequest: FilmRequest?
        filmRequest = kinopoiskAPI.getFilms(pageId, order, year)
        return filmRequest.items
    }

    suspend fun getNumberOfPagesFilms(order: String?, year: Int?) =
        kinopoiskAPI.getNumberOfPagesFilms(1, order, year).totalPages

    suspend fun getNumberOfPagesFrames(id: Int) =
        kinopoiskAPI.getNumberOfPagesFrames(id).totalPages

    suspend fun getFilm(id: Int) = kinopoiskAPI.getFilm(id)

    suspend fun getFrames(filmId: Int, pageId: Int) : List<Frame> {
        val frameItem: FrameItem?
        frameItem = kinopoiskAPI.getFrames(filmId, pageId)
        return  frameItem.items
    }

    private fun initRetrofit() {
        val baseUrl = "https://kinopoiskapiunofficial.tech/"
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    private fun getOkHttpClient() = OkHttpClient
            .Builder()
            .addInterceptor(getInterceptor())
            .build()


    private fun getInterceptor() : HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

}