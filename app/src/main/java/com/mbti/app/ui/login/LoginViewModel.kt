package com.mbti.app.ui.login

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.mbti.app.data.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import java.lang.Exception

class LoginViewModel(
    private val repository: UserRepository
) : ViewModel() {

    var email: String? = null
    var password: String? = null

    var _loginInterface: LoginInterface? = null
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private val disposables = CompositeDisposable()

    fun redirectToSignUp(view: View) {
        _loginInterface!!.redirectToSignUp()
    }

    fun doLogin(view: View) {

        if (email.isNullOrEmpty()) {
            _loginInterface?.onError("Email required")
        } else if (email?.matches(emailPattern.toRegex()) == false) {
            _loginInterface?.onError("Invalid email address")
        } else if (password.isNullOrEmpty()) {
            _loginInterface?.onError("Password required")
        } else {
            try {
                _loginInterface?.onLoginStart()
                val disposable = repository.login(email!!, password!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        _loginInterface?.loginSuccess()
                    }, {
                        _loginInterface?.onError(it.message!!)
                    })
                disposables.add(disposable)
            } catch (e: Exception) {
                Log.e("TAG", "Exception at login ${e.message}")
            }
        }
    }
}