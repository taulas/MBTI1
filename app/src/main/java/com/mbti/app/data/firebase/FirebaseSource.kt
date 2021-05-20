package com.mbti.app.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mbti.app.ui.home.ui.mbtitest.MbtiTestData
import com.mbti.app.ui.register.RegisterData
import com.mbti.app.ui.register.RegisterModel
import com.mbti.app.utils.Constants
import io.reactivex.Completable


class FirebaseSource {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseFireStore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }


    fun login(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun register(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

    fun storeUserData(registerModel: RegisterData) = Completable.create { emitter ->
        firebaseFireStore.collection(Constants.MBTI_Collection).document(registerModel.email)
            .set(registerModel)
            .addOnSuccessListener {
                if (!emitter.isDisposed)
                    emitter.onComplete()
            }.addOnFailureListener {
                if (!emitter.isDisposed)
                    emitter.onError(it.cause!!)
            }

    }


    fun getProfileData(email: String) {
        firebaseFireStore.collection(Constants.MBTI_Collection).document(email).get()
            .addOnSuccessListener { documentSnapshot ->
                val _registerModel = documentSnapshot.toObject(RegisterData::class.java)
                Log.e("TAG", "User Information $_registerModel")
            }
    }

    fun storeMbtiType(
        email: String,
        question1: String,
        answer1: String,
        question2: String,
        answer2: String
    ) =
        Completable.create { emitter ->

            var items = HashMap<String, String>()
            items.put(question1, answer1)
            Log.e("TAG", "Data at submit " + items)
            firebaseFireStore.collection(Constants.MBTI_Collection).document(email)
                .collection(Constants.MBTI_Type).document(Constants.MBTI_Q1)
                .set(items)
                .addOnSuccessListener {

                }.addOnFailureListener {
                    if (!emitter.isDisposed)
                        emitter.onError(it.cause!!)
                }

            items = HashMap<String, String>()
            items.put(question2, answer2)
            Log.e("TAG", "Data at submit " + items)
            firebaseFireStore.collection(Constants.MBTI_Collection).document(email)
                .collection(Constants.MBTI_Type).document(Constants.MBTI_Q2)
                .set(items)
                .addOnSuccessListener {
                    if (!emitter.isDisposed)
                        emitter.onComplete()
                }.addOnFailureListener {
                    if (!emitter.isDisposed)
                        emitter.onError(it.cause!!)
                }
        }

    fun storeMbtiTestData(
        email: String,
        arrayList: ArrayList<MbtiTestData>
    ) =
        Completable.create { emitter ->

            var items = HashMap<String, String>()
            items.put(arrayList.get(0).question, arrayList.get(0).selected_answer)
            Log.e("TAG", "Data at submit " + items)
            firebaseFireStore.collection(Constants.MBTI_Collection).document(email)
                .collection(Constants.MBTI_Test).document(Constants.MBTI_Q1)
                .set(items)
                .addOnSuccessListener {

                }.addOnFailureListener {
                    if (!emitter.isDisposed)
                        emitter.onError(it.cause!!)
                }

            items = HashMap<String, String>()
            items.put(arrayList.get(1).question, arrayList.get(1).selected_answer)
            Log.e("TAG", "Data at submit " + items)
            firebaseFireStore.collection(Constants.MBTI_Collection).document(email)
                .collection(Constants.MBTI_Test).document(Constants.MBTI_Q2)
                .set(items)
                .addOnSuccessListener {
//
                }.addOnFailureListener {
                    if (!emitter.isDisposed)
                        emitter.onError(it.cause!!)
                }


            items = HashMap<String, String>()
            items.put(arrayList.get(2).question, arrayList.get(2).selected_answer)
            Log.e("TAG", "Data at submit " + items)
            firebaseFireStore.collection(Constants.MBTI_Collection).document(email)
                .collection(Constants.MBTI_Test).document(Constants.MBTI_Q3)
                .set(items)
                .addOnSuccessListener {
//
                }.addOnFailureListener {
                    if (!emitter.isDisposed)
                        emitter.onError(it.cause!!)
                }

            items = HashMap<String, String>()
            items.put(arrayList.get(3).question, arrayList.get(3).selected_answer)
            Log.e("TAG", "Data at submit " + items)
            firebaseFireStore.collection(Constants.MBTI_Collection).document(email)
                .collection(Constants.MBTI_Test).document(Constants.MBTI_Q4)
                .set(items)
                .addOnSuccessListener {
                    if (!emitter.isDisposed)
                        emitter.onComplete()
                }.addOnFailureListener {
                    if (!emitter.isDisposed)
                        emitter.onError(it.cause!!)
                }
        }

    fun storePersonality(email: String, type: String, description: String) =
        Completable.create { emitter ->
            var items = HashMap<String, String>()
            items.put(type, description)

            firebaseFireStore.collection(Constants.MBTI_Collection).document(email)
                .collection(Constants.MBTI_Personality).document(Constants.MBTI_Personality)
                .set(items)
                .addOnSuccessListener {
                    if (!emitter.isDisposed)
                        emitter.onComplete()
                }.addOnFailureListener {
                    if (!emitter.isDisposed)
                        emitter.onError(it.cause!!)
                }
        }
}