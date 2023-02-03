package com.cs501.cs501app.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

enum class AlertType {
    SUCCESS, ERROR, INFO, WARNING, FAIL
}

class Alert {

    companion object {
        private const val SNACKBAR_DURATION = Snackbar.LENGTH_LONG

        private fun makeAlert(success: Boolean, view: View, msg: String) {
            val alertMsg: String = (if (success) "√" else "X").plus(": ").plus(msg)

            Snackbar.make(view, alertMsg, SNACKBAR_DURATION).show()
        }

        private fun makeAlert(type: AlertType, view: View, msg: String) {
            val alertMsg: String = type.toString().plus(": ").plus(msg)
            var snackbar: Snackbar = Snackbar.make(view, alertMsg, SNACKBAR_DURATION)
            snackbar = when (type) {
                AlertType.SUCCESS -> {
                    snackbar.setBackgroundTint(0xFF00FF00.toInt())
                    snackbar.setActionTextColor(0xFF000000.toInt())
                }
                AlertType.ERROR -> snackbar.setBackgroundTint(0xFFFF0000.toInt())
                AlertType.INFO -> snackbar.setBackgroundTint(0xFF0000FF.toInt())
                AlertType.WARNING -> snackbar.setBackgroundTint(0xFFFFFF00.toInt())
                AlertType.FAIL -> snackbar.setBackgroundTint(0xFFFF0000.toInt())
            }
            snackbar.show()
        }

        fun success(view: View, msg: String) {
//            makeAlert(true, view, msg)
            makeAlert(AlertType.SUCCESS, view, msg)
        }

        fun fail(view: View, msg: String) {
//            makeAlert(false, view, msg)
            makeAlert(AlertType.FAIL, view, msg)
        }
    }
}