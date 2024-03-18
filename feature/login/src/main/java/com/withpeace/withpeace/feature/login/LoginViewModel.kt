package com.withpeace.withpeace.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.role.Role
import com.withpeace.withpeace.core.domain.usecase.GoogleLoginUseCase
import com.withpeace.withpeace.core.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {

    private val _loginUiEvent: Channel<LoginUiEvent> = Channel()
    val loginUiEvent = _loginUiEvent.receiveAsFlow()

    fun googleLogin(idToken: String) {
        viewModelScope.launch {
            googleLoginUseCase(
                idToken = idToken,
                onError = { launch { _loginUiEvent.send(LoginUiEvent.LoginFail) } },
            ).collect {
                launch {
                    when (it) {
                        Role.USER -> _loginUiEvent.send(LoginUiEvent.LoginSuccess)
                        Role.GUEST -> _loginUiEvent.send(LoginUiEvent.SignUpNeeded)
                        Role.UNKNOWN -> _loginUiEvent.send(LoginUiEvent.LoginFail)
                        // UNKNOWN은 서버에서 내려주는 역할이 string이므로 휴먼에러 방지를 위함
                    }
                }
            }
        }
    }

    fun signUp(
        email: String,
        nickname: String,
    ) {
        viewModelScope.launch {
            signUpUseCase(
                email = email,
                nickname = nickname,
                onError = { launch { _loginUiEvent.send(LoginUiEvent.SignUpFail(it)) } },
            ).collect { _loginUiEvent.send(LoginUiEvent.SignUpSuccess) }
        }
    }
}
