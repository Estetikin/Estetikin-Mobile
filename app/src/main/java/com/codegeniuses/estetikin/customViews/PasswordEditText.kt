package com.codegeniuses.estetikin.customViews

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.codegeniuses.estetikin.R

class PasswordEditText : AppCompatEditText {

    private lateinit var passwordIconDrawable: Drawable
    private lateinit var eyeOnDrawable: Drawable
    private lateinit var eyeOffDrawable: Drawable
    private var isPasswordVisible = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        passwordIconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_lock)!!
        eyeOnDrawable = ContextCompat.getDrawable(context, R.drawable.ic_eye)!!
        eyeOffDrawable = ContextCompat.getDrawable(context, R.drawable.ic_eye_off)!!

        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        compoundDrawablePadding = 16

        updateCompoundDrawables()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateError()
                updateCompoundDrawables()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && event.rawX >= (right - (compoundPaddingEnd + eyeOffDrawable.intrinsicWidth))) {
                togglePasswordVisibility()
                performClick() // Call performClick() to handle the click event
                return@setOnTouchListener true
            }
            false
        }
    }

    private fun updateCompoundDrawables() {
        val compoundDrawables = compoundDrawablesRelative
        val eyeDrawable = if (isPasswordVisible) eyeOnDrawable else eyeOffDrawable
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            passwordIconDrawable,
            compoundDrawables[1],
            eyeDrawable,
            compoundDrawables[3]
        )
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        transformationMethod = if (isPasswordVisible) {
            HideReturnsTransformationMethod.getInstance()
        } else {
            PasswordTransformationMethod.getInstance()
        }
        updateCompoundDrawables()
    }

    private fun updateError() {
        val password = text?.toString() ?: ""
        val error =
            if (password.length >= 6) null else "Password should be at least 6 characters long."
        this.error = error
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}