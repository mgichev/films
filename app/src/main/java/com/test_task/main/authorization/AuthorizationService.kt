package com.test_task.main.authorization

class AuthorizationService (private val userPrefs: UserPreferences) {

    init {
        setDefaultAccount()
    }

    companion object {
        private var currentUser: User? = null
    }

    fun tryAuth(login: String, password: String) : Boolean {

        if(isDefaultUser(login, password)) return true

        if (!isInputDataCorrect(login, password)) return false

        val user = User(login, password)

        val loginState = getInputLoginState(user)

        when (loginState) {
            LoginState.UNKNOWN_LOGIN -> {
                setUser(user, true)
                return true
            }

            LoginState.INCORRECT_PASSWORD -> return false

            LoginState.SUCCESSFUL -> {
                setUser(user, false)
                return true
            }
        }
    }

    fun getUser() = currentUser?.copy()

    fun setDefaultAccount() {
        currentUser = User("login", "password")
    }

    private fun isInputDataCorrect(login: String, password: String) : Boolean {
        return !(login.isEmpty() || password.isEmpty())
    }

    private fun getInputLoginState(user: User): LoginState {
        val answer = userPrefs.getUser(user)
        return if (answer == UserPreferences.NO_USER_VALUE) {
            LoginState.UNKNOWN_LOGIN
        } else if (answer != user.password){
            LoginState.INCORRECT_PASSWORD
        } else {
            LoginState.SUCCESSFUL
        }
    }

    private fun createNewUser(user: User) {
        userPrefs.addUser(user)
    }

    private fun isDefaultUser(login: String, password: String) =
        login.isEmpty() && password.isEmpty()

    private fun setUser(user: User, isNewAccount: Boolean) {
        if (isNewAccount) {
            createNewUser(user)
        }
        currentUser = user
    }
}