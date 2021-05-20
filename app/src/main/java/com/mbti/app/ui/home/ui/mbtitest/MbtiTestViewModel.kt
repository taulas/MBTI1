package com.mbti.app.ui.home.ui.mbtitest

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.mbti.app.R
import com.mbti.app.data.repositories.UserRepository
import com.mbti.app.ui.home.ui.mbtitype.MbtiTypeInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MbtiTestViewModel : ViewModel() {


    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    var _interface: MbtiTypeInterface? = null

    fun submitTestData(repository: UserRepository, _list: ArrayList<MbtiTestData>, email: String) {
        try {
            _interface!!.onProcessStart()
            //setup question 1
            val disposable = repository.setupMbtiTestAnswer(
                email, _list
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
        } catch (E: Exception) {
            Log.e("TAG", "Exception at submitTestData ${E.toString()}")
        }
    }

    fun submitPersonality(
        repository: UserRepository,
        email: String,
        type: String,
        description: String
    ) {
        try {
            //setup question 1
            val disposable = repository.storeUserPersonality(
                email, type, description
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e("TAG", "On Success Called")
                }, {
                    _interface?.onError(it.message!!)
                })
            disposables.add(disposable)
        } catch (E: Exception) {
            Log.e("TAG", "Exception at submitTestData ${E.toString()}")
        }
    }


    fun setupType(context: Context, _str: String) {
        var personality: String = ""
        var personalityDescr: String = ""
        when (_str) {
            "estj" -> {
                personality = context.getString(R.string.manager)
                personalityDescr = context.resources.getString(R.string.estj_content)
            }

            "entj" -> {
                personality = context.getString(R.string.commander)
                personalityDescr = context.resources.getString(R.string.entj_content)
            }

            "esfj" -> {
                personality = context.getString(R.string.teacher)
                personalityDescr = context.resources.getString(R.string.esfj_content)
            }
            "estp" -> {
                personality = context.getString(R.string.marshal)
                personalityDescr = context.resources.getString(R.string.estp_content)
            }

            "enfj" -> {
                personality = context.getString(R.string.mentor)
                personalityDescr = context.resources.getString(R.string.enfj_content)
            }

            "entp" -> {
                personality = context.getString(R.string.inventor)
                personalityDescr = context.resources.getString(R.string.entp_content)
            }

            "esfp" -> {
                personality = context.getString(R.string.politician)
                personalityDescr = context.resources.getString(R.string.esfp_content)
            }

            "enfp" -> {
                personality = context.getString(R.string.champion)
                personalityDescr = context.resources.getString(R.string.enfp_content)
            }

            "infp" -> {
                personality = context.getString(R.string.healer)
                personalityDescr = context.resources.getString(R.string.infp_content)
            }

            "isfp" -> {
                personality = context.getString(R.string.composer)
                personalityDescr = context.resources.getString(R.string.isfp_content)
            }
            "intp" -> {
                personality = context.getString(R.string.architect)
                personalityDescr = context.resources.getString(R.string.intp_content)
            }

            "infj" -> {
                personality = context.getString(R.string.advisor)
                personalityDescr = context.resources.getString(R.string.infj_content)
            }
            "intj" -> {
                personality = context.getString(R.string.inspirer)
                personalityDescr = context.resources.getString(R.string.intj_content)
            }
            "isfj" -> {
                personality = context.getString(R.string.protector)
                personalityDescr = context.resources.getString(R.string.isfj_content)
            }
            "istp" -> {
                personality = context.getString(R.string.handyman)
                personalityDescr = context.resources.getString(R.string.istp_content)
            }
            "istj" -> {
                personality = context.getString(R.string.inspector)
                personalityDescr = context.resources.getString(R.string.istj_content)
            }
        }
        _interface?.setupPestonality(personality, personalityDescr)
    }

}