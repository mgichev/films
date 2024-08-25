package com.test_task.main.films

class SearchService {

    companion object {
        @Volatile
        private var instance: SearchService? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: SearchService().also { instance = it }
            }
    }

    private fun isInNames(film: Film, key: String) =
        (film.nameRu?.contains(key) == true) || (film.nameOriginal?.contains(key) == true)

    fun isContainsKey(film: Film, key: String) = (key == "")
                || isInNames(film, key)
                || isInCountries(film, key)
                || isInGenres(film, key)

    private fun isInCountries(film: Film, key: String) : Boolean {
        for (el in film.countries) {
            if (el.country?.contains(key) == true)
                return true
        }
        return false
    }

    private fun isInGenres(film: Film, key: String) : Boolean {
        for (el in film.genres) {
            if (el.genre?.contains(key) == true)
                return true
        }
        return false
    }
}