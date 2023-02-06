package com.cs501.cs501app.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.cs501.cs501app.R
import com.google.android.material.snackbar.Snackbar

enum class AlertType {
    SUCCESS, ERROR, INFO, WARNING, FAIL
}

class Alert {

    companion object {
        private const val SNACKBAR_DURATION = Snackbar.LENGTH_LONG

        private fun makeAlert(type: AlertType, view: View, msg: String) {
            val alertMsg: String = type.toString().plus(": ").plus(msg)
            var snackbar: Snackbar = Snackbar.make(view, alertMsg, SNACKBAR_DURATION)
            snackbar = when (type) {
                AlertType.SUCCESS -> snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.success))
                AlertType.ERROR -> snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.error))
                AlertType.INFO -> snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.info))
                AlertType.WARNING -> snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.warning))
                AlertType.FAIL -> snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.error))
            }
            snackbar.show()
        }

        private fun makeAlert(type: AlertType, view: View, msg: String) {
            val alertMsg: String = type.toString().plus(": ").plus(msg)
            var snackbar: Snackbar = Snackbar.make(view, alertMsg, SNACKBAR_DURATION)
            snackbar = when (type) {
                AlertType.SUCCESS -> snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.success))
                AlertType.ERROR -> snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.error))
                AlertType.INFO -> snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.info))
                AlertType.WARNING -> snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.warning))
                AlertType.FAIL -> snackbar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.error))
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