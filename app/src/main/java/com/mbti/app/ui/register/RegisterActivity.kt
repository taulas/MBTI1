package com.mbti.app.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore
import com.mbti.app.R
import com.mbti.app.data.firebase.FirebaseSource
import com.mbti.app.data.repositories.UserRepository
import com.mbti.app.databinding.ActivityRegisterBinding
import com.mbti.app.ui.home.main.HomeActivity
import com.mbti.app.utils.Constants
import com.mbti.app.utils.SharedPreference
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), RegisterCallback {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val _source = FirebaseSource()
        val db = FirebaseFirestore.getInstance()
        val repository: UserRepository = UserRepository(_source)
        val _factory = RegisterModelFactory(repository, db)
        val viewmodel: RegisterModel =
            ViewModelProviders.of(this, _factory).get(RegisterModel::class.java)
        val _bindings: ActivityRegisterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_register)
        _bindings.model = viewmodel
        viewmodel.registerCallback = this
        _bindings.lifecycleOwner = this
    }

    override fun finishScreen() {
        finish()
    }

    override fun onStarted() {
        Log.e("TAG", "Trackin SignUp onStarted")
        progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        Log.e("TAG", "Trackin SignUp onSuccess")
        progressbar.visibility = View.GONE

        val _preference: SharedPreference = SharedPreference(this)
        _preference.saveString(Constants.email, edt_email.text.toString())
        _preference.saveString(Constants.uname, edt_uname.text.toString())
        val _intents = Intent(this@RegisterActivity, HomeActivity::class.java)

        _intents.also {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(_intents)
            finish()
        }
    }

    override fun onFailure(message: String) {
        Log.e("TAG", "Trackin SignUp onFailure $message")
        progressbar.visibility = View.GONE
//        rl_main.snackbar(message)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}