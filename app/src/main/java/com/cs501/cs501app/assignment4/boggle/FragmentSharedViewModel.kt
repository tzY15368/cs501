package com.cs501.cs501app.assignment4.boggle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.app.Application
class FragmentSharedViewModel(application: Application) : AndroidViewModel(application) {
    private val _messageClear = MutableLiveData<String>()
    private val _messageSubmit = MutableLiveData<String>()
    private val _messageNewGame = MutableLiveData<String>()

    fun setMessageClear(input: String) {
        _messageClear.value = input
    }

    fun getMessageClear(): LiveData<String> {
        return _messageClear
    }

    fun setMessageSubmit(input: String) {
        _messageSubmit.value = input
    }

    fun getMessageSubmit(): LiveData<String> {
        return _messageSubmit
    }

    fun setMessageNewGame(input: String) {
        _messageNewGame.value = input
    }

    fun getMessageNewGame(): LiveData<String> {
        return _messageNewGame
    }
}