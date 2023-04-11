package com.cs501.cs501app.assignment3.geoquiz

import androidx.annotation.StringRes
data class Question(@StringRes val textResId: Int, val answer: Boolean, var isCheated: Boolean)