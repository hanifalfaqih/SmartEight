package sch.id.snapan.smarteight.ui

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Activity.snackbar(message: String) {
    Snackbar.make(window.decorView.rootView, message, Snackbar.LENGTH_LONG).show()
}