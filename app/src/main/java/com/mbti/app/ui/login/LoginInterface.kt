package com.mbti.app.ui.login

interface LoginInterface {

    fun redirectToSignUp()

    fun loginSuccess()

    fun onError(msg: String)

    fun onLoginStart()
}