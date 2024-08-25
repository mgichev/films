package com.test_task.main.page

import com.test_task.main.films.Frame
import com.test_task.main.network_services.RetrofitClient

class FrameDataService {

    private var numberOfPages = 0
    private var pageId = 0
    private var filmId = 0

    companion object {
        @Volatile
        private var instance: FrameDataService? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: FrameDataService().also { instance = it }
            }
    }

    private suspend fun refreshPagesData(id: Int) {
        pageId = 0
        numberOfPages =
            RetrofitClient.getInstance().getNumberOfPagesFrames(id)
    }

    suspend fun loadNextPage() : List<Frame>? {
        if (pageId < numberOfPages) {
            pageId++
            val response = ArrayList(
                RetrofitClient
                    .getInstance()
                    .getFrames(filmId, pageId)
            )

            return response
        }
        return null
    }

    suspend fun firstLoad(id: Int) : List<Frame>? {
        filmId = id
        refreshPagesData(filmId)
        return loadNextPage()
    }
}