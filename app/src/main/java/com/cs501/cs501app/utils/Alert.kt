package com.cs501.cs501app.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

class Alert {

    companion object {
        private const val SNACKBAR_DURATION = Snackbar.LENGTH_LONG

        private fun makeAlert(success: Boolean, view: View, msg: String) {
            val alertMsg: String = (if (success) "√" else "X").plus(": ").plus(msg)

            Snackbar.make(view, alertMsg, SNACKBAR_DURATION).show()
        }

        fun success(view: View, msg: String) {
            makeAlert(true, view, msg)
        }

        fun fail(view: View, msg: String) {
            makeAlert(false, view, msg)
        }
    }
}