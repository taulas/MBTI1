package com.mbti.app.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.mbti.app.R
import com.mbti.app.data.firebase.FirebaseSource
import com.mbti.app.data.repositories.UserRepository
import com.mbti.app.databinding.ActivitySplashBinding
import com.mbti.app.ui.home.main.HomeActivity
import com.mbti.app.ui.login.LoginActivity

class SplashActivity : AppCompatActivity(), SplashCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val _source = FirebaseSource()
        val _repository: UserRepository = UserRepository(_source)
        val _factory = SplashModelFactory(_repository)
        val viewModel: SplashViewModel =
            ViewModelProviders.of(this, _factory).get(SplashViewModel::class.java)
        viewModel._interface = this
        var _binding: ActivitySplashBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_splash)

        _binding.modelsplash = viewModel
        _binding.lifecycleOwner = this

        viewModel.checkisLoggedIn()
    }

    override fun redirectToLogin() {
        val _intent = Intent(this, LoginActivity::class.java)
        _intent.also {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(_intent)
            finish()
        }
    }

    override fun redirectToHome() {
        val _intent = Intent(this, HomeActivity::class.java)
        _intent.also {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(_intent)
            finish()
        }
    }
}