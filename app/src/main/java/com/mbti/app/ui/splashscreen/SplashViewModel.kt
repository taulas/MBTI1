package com.mbti.app.ui.splashscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mbti.app.data.repositories.UserRepository

class SplashViewModel(
    private val repository: UserRepository
) : ViewModel() {

    var _interface: SplashCallback? = null

    fun checkisLoggedIn() {
        Log.e("TAG", "Init Called")
        if (repository.currentUser() == null) {
            _interface!!.redirectToLogin()
            Log.e("TAG", "Redirect to login")
        } else {
            Log.e("TAG", "Redirect to main")
            _interface!!.redirectToHome()
        }
    }

}