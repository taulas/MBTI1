package com.mbti.app.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.mbti.app.R

class CustomFont : TextView {

    constructor(context: Context) : super(context) {
        applyCustomFont(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        applyCustomFont(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        applyCustomFont(context)
    }

    private fun applyCustomFont(context: Context) {
//        val customFont: Typeface = FontCache.getTypeface("SourceSansPro-Regular.ttf", context)
        var typeFace: Typeface? = ResourcesCompat.getFont(context, R.font.segoe_ui_regular)
        setTypeface(typeFace)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)
}