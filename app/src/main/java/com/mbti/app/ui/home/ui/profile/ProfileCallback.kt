package com.mbti.app.ui.home.ui.profile

import com.mbti.app.ui.register.RegisterData
import com.mbti.app.ui.register.RegisterModel

interface ProfileCallback {

    fun onDataReceived(registerModel: RegisterData)

    fun onStartProcess()

    fun onError(msg: String)

    fun onSuccess()

    fun setupPersonality(type: String, description: String)
}