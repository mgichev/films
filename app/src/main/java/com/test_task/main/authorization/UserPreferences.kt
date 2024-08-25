package com.test_task.main.authorization

import android.content.Context

class UserPreferences (private val context: Context) {

    companion object {
        const val NO_USER_VALUE = "-1"
    }

    fun getUser(user: User) : String {
        val sPreferences = context.getSharedPreferences(user.login, Context.MODE_PRIVATE)
        return sPreferences.getString(user.login, NO_USER_VALUE)!!
    }

    fun addUser(user: User) {
        val sPreferences = context.getSharedPreferences(user.login, Context.MODE_PRIVATE) ?: return
        with (sPreferences.edit()) {
            putString(user.login, user.password)
            apply()
        }
    }
}