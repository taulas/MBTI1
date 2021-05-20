package com.mbti.app.ui.home.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mbti.app.R
import com.mbti.app.data.firebase.FirebaseSource
import com.mbti.app.data.repositories.UserRepository
import com.mbti.app.ui.register.RegisterData
import com.mbti.app.utils.Constants
import com.mbti.app.utils.SharedPreference
import com.mbti.app.utils.hide
import com.mbti.app.utils.show
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ProfileCallback {

    private lateinit var homeViewModel: ProfileViewModel
    lateinit var tv_type: TextView
    lateinit var tv_type_description: TextView
    lateinit var pbar: ProgressBar
    lateinit var btn_update: Button
    var pass: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val firestore = FirebaseFirestore.getInstance()
        val _source = FirebaseSource()
        val repository = UserRepository(_source)
        val factory = ProfileModelFactory(repository, firestore)
        homeViewModel =
            ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        homeViewModel._interface = this
        val _preference: SharedPreference = SharedPreference(requireContext())
        tv_type = root.findViewById<TextView>(R.id.tv_type)
        tv_type_description = root.findViewById<TextView>(R.id.tv_type_description)
        pbar = root.findViewById<ProgressBar>(R.id.progressbar)
        btn_update = root.findViewById<Button>(R.id.btn_update)
        homeViewModel.getProfileData(_preference.getValueString(Constants.email)!!)
        btn_update.setOnClickListener(View.OnClickListener {

            val _model = RegisterData(
                edt_fname.text.toString(),
                edt_lname.text.toString(),
                edt_uname.text.toString(),
                edt_email.text.toString(), pass, edt_basic_info.text.toString()
            )
            homeViewModel.storeUserInformation(_model)
        })
        return root
    }

    override fun onDataReceived(registerModel: RegisterData) {
        pbar.hide()
        pass = registerModel.password
        edt_fname.setText(registerModel.firstname)
        edt_lname.setText(registerModel.lastname)
        edt_uname.setText(registerModel.username)
        edt_email.setText(registerModel.email)
        tv_full_name.setText(registerModel.email)
        edt_basic_info.setText(registerModel.basicinfo)
    }

    override fun onStartProcess() {
        pbar.show()
    }

    override fun onError(msg: String) {
        pbar.hide()
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    override fun onSuccess() {
        pbar.hide()
//        tv_full_name.setText(edt_fname.text.toString() + " " + edt_lname.text.toString())
        Toast.makeText(requireContext(), getString(R.string.success), Toast.LENGTH_LONG).show()
    }

    override fun setupPersonality(type: String, description: String) {
        tv_type.text = type
        tv_type_description.text = description
    }
}