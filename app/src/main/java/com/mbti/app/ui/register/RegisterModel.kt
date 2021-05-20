package com.mbti.app.ui.register

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.mbti.app.data.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class RegisterModel(
    private val repository: UserRepository,
    private val db: FirebaseFirestore
) : ViewModel() {

    var email: String? = null
    var password: String? = null
    var username: String? = null
    var fname: String? = null
    var lname: String? = null


    private val disposables = CompositeDisposable()
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    var registerCallback: RegisterCallback? = null

    fun closeScreen(view: View) {
        registerCallback!!.finishScreen()
    }

    fun signUp() {

        if (fname.isNullOrEmpty()) {
            registerCallback?.onFailure("Enter first name")
        } else if (lname.isNullOrEmpty()) {
            registerCallback?.onFailure("Enter last name")
        } else if (username.isNullOrEmpty()) {
            registerCallback?.onFailure("Enter username")
        } else if (email.isNullOrEmpty()) {
            registerCallback?.onFailure("Enter email")
        } else if (email?.matches(emailPattern.toRegex()) == false) {
            registerCallback?.onFailure("Invalid email address")
        } else if (password.isNullOrEmpty()) {
            registerCallback?.onFailure("Enter password")
        } else if (password!!.length <= 5) {
            registerCallback?.onFailure("Password too short")
        } else {
            //authentication started
            registerCallback?.onStarted()
            val disposable = repository.register(email!!, password!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    storeUserInformation()
                }, {
                    registerCallback?.onFailure(it.message!!)
                })
            disposables.add(disposable)
        }
    }

    fun storeUserInformation() {
        var _basic_info: String = "$fname \n$lname \n$username"
        Log.e("TAG", "Basic Info $_basic_info")
        val _usermodel =
            RegisterData(fname!!, lname!!, username!!, email!!, password!!, _basic_info)
        val disposable = repository.storeUserData(_usermodel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("TAG", "On Success Called")
                registerCallback?.onSuccess()
            }, {
                registerCallback?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }
}