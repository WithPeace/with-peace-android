package com.withpeace.withpeace.feature.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
    var nickname by mutableStateOf("")
        private set

    fun onNicknameChanged(nickname: String) {
        this.nickname = nickname
    }

    fun signUp() {

    }
}