package com.mbti.app.ui.home.ui.mbtitest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mbti.app.R
import com.mbti.app.data.firebase.FirebaseSource
import com.mbti.app.data.repositories.UserRepository
import com.mbti.app.ui.home.main.HomeActivity
import com.mbti.app.ui.home.ui.mbtitype.MbtiTypeInterface
import com.mbti.app.utils.Constants
import com.mbti.app.utils.SharedPreference
import com.mbti.app.utils.hide
import com.mbti.app.utils.show

class MbtiTestFragment : Fragment(), View.OnClickListener, MbtiTypeInterface {

    private lateinit var mbtitestViewModel: MbtiTestViewModel

    lateinit var btn_ok: Button
    lateinit var btn_submit: Button
    lateinit var tv_question_test: TextView
    lateinit var tv_previous: TextView
    lateinit var tv_personality_name: TextView
    lateinit var tv_personality_content: TextView

    lateinit var ll_personality: LinearLayout

    lateinit var rg_test: RadioGroup
    lateinit var rb_test_1: RadioButton
    lateinit var rb_test_2: RadioButton
    lateinit var pbar: ProgressBar

    var _index: Int = 0;

    var arrayList = ArrayList<MbtiTestData>()

    var _repository: UserRepository? = null
    var email: String = ""
    var _preference: SharedPreference? = null
    var _str: String = ""
    var _builder: StringBuilder = StringBuilder()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mbtitestViewModel =
            ViewModelProvider(this).get(MbtiTestViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_mbti_test, container, false)

        mbtitestViewModel._interface = this

        _preference = SharedPreference(requireContext())
        email = _preference?.getValueString(Constants.email)!!
        val _firesource = FirebaseSource()
        _repository = UserRepository(_firesource)
        pbar = root.findViewById<ProgressBar>(R.id.progressbar)
        btn_submit = root.findViewById<Button>(R.id.btn_submit)
        btn_submit.setOnClickListener(this)
        btn_ok = root.findViewById<Button>(R.id.btn_ok)
        btn_ok.setOnClickListener(this)

        tv_previous = root.findViewById<TextView>(R.id.tv_previous)
        tv_previous.setOnClickListener(this)

        ll_personality = root.findViewById<LinearLayout>(R.id.ll_personality)
        tv_personality_name = root.findViewById<TextView>(R.id.tv_personality_name)
        tv_personality_content = root.findViewById<TextView>(R.id.tv_personality_content)
        tv_question_test = root.findViewById<TextView>(R.id.tv_question_test)
        rb_test_1 = root.findViewById<RadioButton>(R.id.rb_test_1)
        rb_test_2 = root.findViewById<RadioButton>(R.id.rb_test_2)
        rg_test = root.findViewById<RadioGroup>(R.id.rg_test)

        /*if (!_preference!!.getValueString(Constants.MBTI_Personality)
                .isNullOrEmpty() && !_preference!!.getValueString(Constants.MBTI_Personality_Description)
                .isNullOrEmpty()
        ) {
            ll_personality.visibility = View.VISIBLE
            tv_personality_name.text = _preference!!.getValueString(Constants.MBTI_Personality)
            tv_personality_content.text =
                _preference!!.getValueString(Constants.MBTI_Personality_Description)
        } else {
        }*/
        setupList()
        return root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_submit -> {
                if (rb_test_1.isChecked || rb_test_2.isChecked) {
                    if (_index < arrayList.size) {
                        configradiobuttonValue()
                        _index++;
                        setupNextText()
                        setupRadioButton()
                    }
                    if (_index == arrayList.size) {
                        mbtitestViewModel.submitTestData(_repository!!, arrayList, email)
                        Log.e("TAG", "OnSubmit list is $arrayList")
                    }
                } else {
                    Toast.makeText(requireContext(), "Please select option!!", Toast.LENGTH_LONG)
                        .show()
                }
            }
            R.id.tv_previous -> {
                if (_index <= arrayList.size) {
                    configradiobuttonValue()
                    _index--;
                    setupNextText()
                    setupRadioButton()
                }
            }
            R.id.btn_ok -> {
                tv_personality_content.setText("")
                tv_personality_name.setText("")
//                ll_personality.visibility = View.GONE
            }
        }
    }

    fun configradiobuttonValue() {
        try {
            if (_index < arrayList.size) {
                if (rb_test_1.isChecked)
                    arrayList.get(_index).selected_index = 0
                else
                    arrayList.get(_index).selected_index = 1
                arrayList.get(_index).selected_answer = getAnswer()
            }
        } catch (E: java.lang.Exception) {

        }
    }

    fun getAnswer(): String {
        if (_index == 0) {
            if (arrayList.get(_index).selected_index == 0)
                return "E"
            else
                return "I"
        } else if (_index == 1) {
            if (arrayList.get(_index).selected_index == 0)
                return "S"
            else
                return "N"
        } else if (_index == 2) {
            if (arrayList.get(_index).selected_index == 0)
                return "T"
            else
                return "F"

        } else if (_index == 3) {
            if (arrayList.get(_index).selected_index == 0)
                return "J"
            else
                return "P"
        }
        return ""
    }

    fun setupRadioButton() {
        try {
            if (_index < arrayList.size) {
                if (arrayList.get(_index).selected_index != -1) {
                    if (arrayList.get(_index).selected_index == 0)
                        rb_test_1.isChecked = true
                    else
                        rb_test_2.isChecked = true
                } else {
                    rg_test.clearCheck()
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "Exception at setup button ${e.toString()}")
        }
    }

    fun setupList() {
        arrayList.clear()
        var _object = MbtiTestData(
            getString(R.string.test_q1),
            getString(R.string.test_ans_q1_1),
            getString(R.string.test_ans_q1_2),
            "",
            -1
        )
        arrayList.add(_object)

        _object = MbtiTestData(
            getString(R.string.test_q2),
            getString(R.string.test_ans_q2_1),
            getString(R.string.test_ans_q2_2),
            "",
            -1
        )
        arrayList.add(_object)

        _object = MbtiTestData(
            getString(R.string.test_q3),
            getString(R.string.test_ans_q3_1),
            getString(R.string.test_ans_q3_2),
            "",
            -1
        )
        arrayList.add(_object)

        _object = MbtiTestData(
            getString(R.string.test_q4),
            getString(R.string.test_ans_q4_1),
            getString(R.string.test_ans_q4_2),
            "",
            -1
        )
        arrayList.add(_object)
        _index = 0;
        setupNextText()
    }

    fun setupNextText() {
        try {
            if (_index < arrayList.size) {
                if (_index < 0) {
                    _index = 0
                }
                tv_question_test.text = arrayList.get(_index).question
                rb_test_1.text = arrayList.get(_index).answer1
                rb_test_2.text = arrayList.get(_index).answer2

                if (_index == 0) {
                    tv_previous.setTextColor(resources.getColor(R.color.grey))
                } else {
                    tv_previous.setTextColor(resources.getColor(R.color.black))
                }
                if (_index == arrayList.size - 1) {
                    btn_submit.text = resources.getString(R.string.submit)
                } else {
                    btn_submit.text = resources.getString(R.string.next)
                }
                Log.e("TAG", "Current Index $_index")
            }
        } catch (E: Exception) {
            Log.e("TAG", "Exception at setup text ${E.toString()}")
        }

    }

    override fun onError(msg: String) {
        pbar.hide()
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    override fun onSuccess() {
        pbar.hide()
        _index = 0
        resetAll()
        setupList()

        Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
    }


    fun resetAll() {
        try {
            _builder.clear()
            _str = ""
            for (obj in arrayList) {
                _builder.append(obj.selected_answer)
                obj.selected_answer = ""
                obj.selected_index = -1
                rb_test_1.isChecked = false
                rb_test_2.isChecked = false
            }
            _str = _builder.toString().toLowerCase()
            Log.e("TAG", "Builder String ${_builder.toString()}")
            ll_personality.visibility = View.VISIBLE
//            setupType()
            mbtitestViewModel.setupType(requireContext(), _str)
        } catch (e: Exception) {
        }
    }

    override fun onProcessStart() {
        pbar.show()
    }

    override fun setupPestonality(type: String, description: String) {
        tv_personality_name.text = type
        tv_personality_content.text = description

        (activity as HomeActivity?)?.hideMenu()
        _preference?.saveString(Constants.MBTI_Personality, tv_personality_name.text.toString())
        _preference?.saveString(
            Constants.MBTI_Personality_Description,
            tv_personality_content.text.toString()
        )
        mbtitestViewModel.submitPersonality(
            _repository!!,
            email!!,
            tv_personality_name.text.toString(),
            tv_personality_content.text.toString()
        )
    }


    /*fun setupType() {

        when (_str) {
            "estj" -> {
                tv_personality_name.setText(getString(R.string.manager))
                tv_personality_content.text = resources.getString(R.string.estj_content)
            }

            "entj" -> {
                tv_personality_name.setText(getString(R.string.commander))
                tv_personality_content.text = resources.getString(R.string.entj_content)
            }

            "esfj" -> {
                tv_personality_name.setText(getString(R.string.teacher))
                tv_personality_content.text = resources.getString(R.string.esfj_content)
            }
            "estp" -> {
                tv_personality_name.setText(getString(R.string.marshal))
                tv_personality_content.text = resources.getString(R.string.estp_content)
            }

            "enfj" -> {
                tv_personality_name.setText(getString(R.string.mentor))
                tv_personality_content.text = resources.getString(R.string.enfj_content)
            }

            "entp" -> {
                tv_personality_name.setText(getString(R.string.inventor))
                tv_personality_content.text = resources.getString(R.string.entp_content)
            }

            "esfp" -> {
                tv_personality_name.setText(getString(R.string.politician))
                tv_personality_content.text = resources.getString(R.string.esfp_content)
            }

            "enfp" -> {
                tv_personality_name.setText(getString(R.string.champion))
                tv_personality_content.text = resources.getString(R.string.enfp_content)
            }

            "infp" -> {
                tv_personality_name.setText(getString(R.string.healer))
                tv_personality_content.text = resources.getString(R.string.infp_content)
            }

            "isfp" -> {
                tv_personality_name.setText(getString(R.string.composer))
                tv_personality_content.text = resources.getString(R.string.isfp_content)
            }
            "intp" -> {
                tv_personality_name.setText(getString(R.string.architect))
                tv_personality_content.text = resources.getString(R.string.intp_content)
            }

            "infj" -> {
                tv_personality_name.setText(getString(R.string.advisor))
                tv_personality_content.text = resources.getString(R.string.infj_content)
            }

            "intj" -> {
                tv_personality_name.setText(getString(R.string.inspirer))
                tv_personality_content.text = resources.getString(R.string.intj_content)
            }
            "isfj" -> {
                tv_personality_name.setText(getString(R.string.protector))
                tv_personality_content.text = resources.getString(R.string.isfj_content)
            }
            "istp" -> {
                tv_personality_name.setText(getString(R.string.handyman))
                tv_personality_content.text = resources.getString(R.string.istp_content)
            }
            "istj" -> {
                tv_personality_name.setText(getString(R.string.inspector))
                tv_personality_content.text = resources.getString(R.string.istj_content)
            }
        }
        (activity as HomeActivity?)?.hideMenu()
        _preference?.saveString(Constants.MBTI_Personality, tv_personality_name.text.toString())
        _preference?.saveString(
            Constants.MBTI_Personality_Description,
            tv_personality_content.text.toString()
        )
        mbtitestViewModel.submitPersonality(
            _repository!!,
            email!!,
            tv_personality_name.text.toString(),
            tv_personality_content.text.toString()
        )
    }*/
}