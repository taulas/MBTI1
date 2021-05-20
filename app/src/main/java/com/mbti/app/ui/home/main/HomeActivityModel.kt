package com.mbti.app.ui.home.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.mbti.app.data.repositories.UserRepository
import com.mbti.app.utils.Constants

class HomeActivityModel(
    private val userRepository: UserRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {


    var _interface: HomeCallback? = null

    fun checkIsExisted(_email: String) {
        try {
            firestore.collection(Constants.MBTI_Collection).document(_email)
                .collection(Constants.MBTI_Personality).document(Constants.MBTI_Personality).get()
                .addOnSuccessListener { documentSnapshot ->
                    try {
                        val _data = documentSnapshot.data.toString()
                        if (_data != null && !_data.equals("null")) {
                            _interface?.hideMenu()

                        }
                        Log.e("TAG", "Called To Get Data ");
                    } catch (e: Exception) {
                    }
                }.addOnFailureListener { e ->
                    Log.e("TAG", "Called To Get Data addOnFailureListener ");
                }
        } catch (e: Exception) {
            Log.e("TAG", "Called To Get Data Exception ");
        }
    }
}