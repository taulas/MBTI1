package com.mbti.app.ui.home.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.mbti.app.data.repositories.UserRepository
import com.mbti.app.ui.register.RegisterData
import com.mbti.app.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    var _interface: ProfileCallback? = null

    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    var email: String? = null
    var password: String? = null
    var username: String? = null
    var fname: String? = null
    var lname: String? = null

    fun getProfileData(_email: String) {
        try {
            _interface?.onStartProcess()
            firestore.collection(Constants.MBTI_Collection).document(_email).get()
                .addOnSuccessListener { documentSnapshot ->
                    val _registerModel = documentSnapshot.toObject(RegisterData::class.java)
                    email = _registerModel!!.email
                    password = _registerModel!!.password

                    getPersonalityDetail(_email!!)
                    Log.e("TAG", "User Information $_registerModel")
                    _interface?.onDataReceived(_registerModel!!)
                }.addOnFailureListener { e ->
                    _interface?.onError(e.toString())
                }
        } catch (e: Exception) {
            _interface?.onError(e.toString())
        }
    }

    fun getPersonalityDetail(_email: String) {

        try {
            firestore.collection(Constants.MBTI_Collection).document(_email)
                .collection(Constants.MBTI_Personality).document(Constants.MBTI_Personality).get()
                .addOnSuccessListener { documentSnapshot ->

                    try {
                        val _data = documentSnapshot.data.toString()

                        val lstValues: List<String> =
                            _data.substring(1, _data.length - 1).split("=").map { it -> it.trim() }

                        _interface?.setupPersonality(lstValues[0], lstValues[1])
                    } catch (e: Exception) {
                    }
                }.addOnFailureListener { e ->
                    _interface?.onError(e.toString())
                }
        } catch (e: Exception) {
            _interface?.onError(e.toString())
        }
    }

    fun storeUserInformation(registerData: RegisterData) {
        /* if (registerData.firstname.isNullOrEmpty()) {
             _interface?.onError("Enter first name")
         } else if (registerData.lastname.isNullOrEmpty()) {
             _interface?.onError("Enter last name")
         } else if (registerData.username.isNullOrEmpty()) {
             _interface?.onError("Enter username")
         } else {*/
        _interface?.onStartProcess();
        val disposable = userRepository.storeUserData(registerData)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("TAG", "On Success Called")
                _interface?.onSuccess()
            }, {
                _interface?.onError(it.toString())
            })
        disposables.add(disposable)
//        }
    }
}