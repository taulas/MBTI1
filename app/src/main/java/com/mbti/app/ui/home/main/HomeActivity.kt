package com.mbti.app.ui.home.main

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.mbti.app.R
import com.mbti.app.data.firebase.FirebaseSource
import com.mbti.app.data.repositories.UserRepository
import com.mbti.app.databinding.ActivityHomeBinding
import com.mbti.app.databinding.ActivityLoginBinding
import com.mbti.app.ui.login.LoginModelFactory
import com.mbti.app.ui.login.LoginViewModel
import com.mbti.app.ui.splashscreen.SplashActivity
import com.mbti.app.utils.Constants
import com.mbti.app.utils.SharedPreference
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    HomeCallback {

    private lateinit var appBarConfiguration: AppBarConfiguration
    public var navView: NavigationView? = null
    lateinit var tv_logout: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );



        val firestore = FirebaseFirestore.getInstance()
        val firebaseSource = FirebaseSource()
        val _repository: UserRepository = UserRepository(firebaseSource)
        val factory = HomeModelFactory(_repository, firestore)
        var viewmodel: HomeActivityModel =
            ViewModelProviders.of(this, factory).get(HomeActivityModel::class.java)
        viewmodel._interface = this
        val _bindings: ActivityHomeBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_home)
        _bindings.model = viewmodel
        _bindings.lifecycleOwner = this

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        tv_logout = findViewById<TextView>(R.id.tv_logout)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        navView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_mbti_test,
                R.id.nav_profile
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView!!.setupWithNavController(navController)
        val _preference: SharedPreference = SharedPreference(this)

        val headerView: View = navView!!.getHeaderView(0)
        val navUsername = headerView.findViewById<TextView>(R.id.tv_email)
        navUsername.text = _preference.getValueString(Constants.email)

        tv_logout.setOnClickListener(View.OnClickListener {
            logoutDialog()
        })
        viewmodel.checkIsExisted(_preference.getValueString(Constants.email)!!)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        return true
    }


    override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun logoutDialog() {
        val builder = AlertDialog.Builder(this@HomeActivity)
        builder.setMessage("Are you sure you want to Logout?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->

                val _firenase: FirebaseSource = FirebaseSource()
                _firenase?.logout()
                val _pref = SharedPreference(this@HomeActivity)
                _pref.saveString(Constants.email, "")
                val _intent = Intent(this, SplashActivity::class.java)
                _intent.also {
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(_intent)
                    finish()
                }
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, id ->

                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    override fun hideMenu() {
//        Toast.makeText(this, "Hide Menu Called", Toast.LENGTH_LONG).show()
        val menu: Menu = navView!!.getMenu()
        val target = menu.findItem(R.id.nav_mbti_test)
        target.isVisible = false
    }

    override fun isTestSubmitted(value: Boolean) {

    }
}