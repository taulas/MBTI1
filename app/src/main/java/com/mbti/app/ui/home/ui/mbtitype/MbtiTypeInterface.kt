package com.mbti.app.ui.home.ui.mbtitype

interface MbtiTypeInterface {

    fun onError(msg: String)
    fun onSuccess()
    fun onProcessStart()
    fun setupPestonality(type: String, description: String)
}