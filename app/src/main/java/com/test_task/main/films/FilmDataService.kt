package com.test_task.main.films

import com.test_task.main.network_services.RetrofitClient

class FilmDataService {

    private var numberOfPages = 0
    private var pageId = 0
    private lateinit var sortParams: SortParams

    companion object {
        @Volatile
        private var instance: FilmDataService? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: FilmDataService().also { instance = it }
            }
    }

    suspend fun loadNextPage() : List<Film>? {
        if (pageId < numberOfPages) {
            pageId++
            val response = ArrayList(
                RetrofitClient
                .getInstance()
                .getFilms(pageId, sortParams.order, sortParams.year)
            )

            if (sortParams.wordFilter != "") {
                val sortWithKeyArray = selectWithKeys(response, sortParams.wordFilter)
                return sortWithKeyArray
            }
            return response
        }
        return null
    }

    private fun selectWithKeys(films: List<Film>, key: String?) : List<Film> {
        val filmsWithKey = ArrayList<Film>()
        for (el in films) {
            if(SearchService.getInstance().isContainsKey(el, key!!)) {
                filmsWithKey.add(el)
            }
        }
        return filmsWithKey
    }


    suspend fun loadDataByParams(sortParams: SortParams) : List<Film>? {
        this.sortParams = sortParams
        refreshPagesData(sortParams)
        return loadNextPage()
    }

    private suspend fun refreshPagesData(sortParams: SortParams) {
        pageId = 0
        numberOfPages =
            RetrofitClient.getInstance().getNumberOfPagesFilms(sortParams.order, sortParams.year)
    }

    suspend fun firstLoad() : List<Film>? {
        sortParams = SortParams(SortType.RATING, null, null, "")
        refreshPagesData(sortParams)
        return loadDataByParams(sortParams)
    }

}