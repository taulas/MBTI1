package com.mbti.app.ui.home.ui.mbtitype

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mbti.app.R
import com.mbti.app.data.firebase.FirebaseSource
import com.mbti.app.data.repositories.UserRepository
import com.mbti.app.utils.Constants
import com.mbti.app.utils.SharedPreference
import com.mbti.app.utils.hide
import com.mbti.app.utils.show
import kotlinx.android.synthetic.main.fragment_mbti_type.*

class MbtiType : Fragment(), MbtiTypeInterface {

    private lateinit var mbtiTypiViewModel: MbtiTypeViewModel

    lateinit var pbar: ProgressBar
    lateinit var btn_submit: Button
    lateinit var rb_1: RadioGroup
    lateinit var rb_2: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mbtiTypiViewModel =
            ViewModelProvider(this).get(MbtiTypeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mbti_type, container, false)

        mbtiTypiViewModel._interface = this

        rb_1 = root.findViewById<RadioGroup>(R.id.rb_1)
        rb_2 = root.findViewById<RadioGroup>(R.id.rb_2)
        pbar = root.findViewById<ProgressBar>(R.id.progressbar)
        btn_submit = root.findViewById<Button>(R.id.btn_submit)

        val firestore = FirebaseFirestore.getInstance()
        val _source = FirebaseSource()
        val repository = UserRepository(_source)

        val _pref: SharedPreference = SharedPreference(requireContext())

        btn_submit.setOnClickListener(View.OnClickListener {
            var _ans1: String = ""
            var _ans2: String = ""

            if (rb1_yes.isChecked)
                _ans1 = "E"
            else if (rb1_no.isChecked)
                _ans1 = "I"

            if (rb2_yes.isChecked)
                _ans2 = "N"
            else if (rb2_no.isChecked)
                _ans2 = "S"

            Log.e("TAG", "setOnClickListener called")
            if (_ans1.isNullOrEmpty() || _ans2.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please answer all questions", Toast.LENGTH_LONG)
                    .show()
            } else {
                mbtiTypiViewModel.submitMbtiType(
                    requireContext(),
                    _ans1,
                    _ans2,
                    repository,
                    _pref.getValueString(Constants.email)!!
                )
            }
        })
        return root
    }

    override fun onError(msg: String) {
        pbar.hide()
    }

    override fun onSuccess() {
        pbar.hide()
        Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
    }

    override fun onProcessStart() {
        pbar.show()
    }

    override fun setupPestonality(type: String, description: String) {
        TODO("Not yet implemented")
    }
}