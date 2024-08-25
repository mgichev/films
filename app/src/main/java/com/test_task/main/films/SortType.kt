package com.test_task.main.films

enum class SortType () {
    RATING {
        override fun next() = SortType.YEAR
    },
    YEAR {
        override fun next() = SortType.RATING
    };

    abstract fun next(): SortType
}