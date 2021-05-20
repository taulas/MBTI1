package com.mbti.app.ui.register

interface RegisterCallback {

    fun finishScreen();
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}