package com.mbti.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.mbti.app.R
import com.mbti.app.data.firebase.FirebaseSource
import com.mbti.app.data.repositories.UserRepository
import com.mbti.app.databinding.ActivityLoginBinding
import com.mbti.app.ui.home.main.HomeActivity
import com.mbti.app.ui.register.RegisterActivity
import com.mbti.app.utils.Constants
import com.mbti.app.utils.SharedPreference
import com.mbti.app.utils.hide
import com.mbti.app.utils.show
import kotlinx.android.synthetic.main.activity_login.edt_email
import kotlinx.android.synthetic.main.activity_login.progressbar


class LoginActivity : AppCompatActivity(), LoginInterface {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firebaseSource = FirebaseSource()
        val _repository: UserRepository = UserRepository(firebaseSource)
        val factory = LoginModelFactory(_repository)
        var viewmodel: LoginViewModel =
            ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
        viewmodel._loginInterface = this
        val _bindings: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        _bindings.model = viewmodel
        _bindings.lifecycleOwner = this
    }

    override fun redirectToSignUp() {
        val _intent = Intent(this, RegisterActivity::class.java)
        startActivity(_intent)
    }

    override fun loginSuccess() {
        progressbar.hide()
        val _preference: SharedPreference = SharedPreference(this)
        _preference.saveString(Constants.email, edt_email.text.toString())
//        _preference.saveString(Constants.uname, edt_uname.text.toString())
        val _intent = Intent(this, HomeActivity::class.java)
        _intent.also {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(_intent)
            finish()
        }
    }

    override fun onError(msg: String) {
        progressbar.hide()
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun onLoginStart() {
        progressbar.show()
    }

}