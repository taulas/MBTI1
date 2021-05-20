package com.mbti.app.ui.home.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mbti.app.data.repositories.UserRepository

class HomeModelFactory(
    private val userRepository: UserRepository,
    private val firestore: FirebaseFirestore
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeActivityModel(userRepository, firestore) as T
    }
}