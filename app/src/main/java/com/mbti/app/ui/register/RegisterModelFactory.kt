package com.mbti.app.ui.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mbti.app.data.repositories.UserRepository


class RegisterModelFactory(
    private val repository: UserRepository,
    private val db: FirebaseFirestore
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegisterModel(repository, db) as T
    }
}