package com.mbti.app.ui.home.ui.mbtitype

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.mbti.app.R
import com.mbti.app.data.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MbtiTypeViewModel : ViewModel() {

    var _interface: MbtiTypeInterface? = null

    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    fun submitMbtiType(
        context: Context,
        ans1: String,
        ans2: String,
        repository: UserRepository,
        email: String
    ) {
        Log.e("TAG", "submitMbtiType called")
        _interface!!.onProcessStart()
        //setup question 1
        val disposable = repository.setupMbtiTypeAnswers(
            email,
            context.resources.getString(R.string.question_1),
            ans1,
            context.resources.getString(R.string.question_2),
            ans2
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("TAG", "On Success Called")
                _interface?.onSuccess()
            }, {
                _interface?.onError(it.message!!)
            })
        disposables.add(disposable)
    }
}