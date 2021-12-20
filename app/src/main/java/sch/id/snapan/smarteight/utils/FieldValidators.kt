package sch.id.snapan.smarteight.utils

import android.util.Patterns

object FieldValidators {
    fun isFormatEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}