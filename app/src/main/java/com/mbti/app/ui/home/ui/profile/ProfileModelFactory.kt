package com.mbti.app.ui.home.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore
import com.mbti.app.data.repositories.UserRepository

class ProfileModelFactory(
    private val userRepository: UserRepository,
    private val firestore: FirebaseFirestore
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(userRepository, firestore) as T
    }
}