package com.mbti.app.data.repositories

import com.mbti.app.data.firebase.FirebaseSource
import com.mbti.app.ui.home.ui.mbtitest.MbtiTestData
import com.mbti.app.ui.register.RegisterData


class UserRepository(
    private val firebase: FirebaseSource
) {
    fun login(email: String, password: String) = firebase.login(email, password)

    fun register(email: String, password: String) = firebase.register(email, password)

    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()

    fun storeUserData(
        regModel: RegisterData
    ) = firebase.storeUserData(regModel!!)

    fun getUserData(email: String) = firebase.getProfileData(email)

    fun setupMbtiTypeAnswers(
        email: String,
        question1: String,
        answer1: String,
        question2: String,
        answer2: String
    ) =
        firebase.storeMbtiType(email!!, question1!!, answer1!!, question2, answer2)

    fun setupMbtiTestAnswer(email: String, arrayList: ArrayList<MbtiTestData>) =
        firebase.storeMbtiTestData(email, arrayList)


    fun storeUserPersonality(email: String, type: String, description: String) =
        firebase.storePersonality(email, type, description)


}