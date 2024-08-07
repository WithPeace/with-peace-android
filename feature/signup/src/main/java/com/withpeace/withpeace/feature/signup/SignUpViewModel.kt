package com.withpeace.withpeace.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.usecase.SignUpUseCase
import com.withpeace.withpeace.core.domain.usecase.VerifyNicknameUseCase
import com.withpeace.withpeace.core.ui.profile.ProfileNicknameValidUiState
import com.withpeace.withpeace.feature.signup.uistate.SignUpUiEvent
import com.withpeace.withpeace.feature.signup.uistate.SignUpUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val verifyNicknameUseCase: VerifyNicknameUseCase,
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {
    private val _signUpInfo = MutableStateFlow(
        SignUpUiModel(
            "", null,
        ),
    )
    val signUpUiModel = _signUpInfo.asStateFlow()

    private val _profileNicknameValidUiState =
        MutableStateFlow<ProfileNicknameValidUiState>(ProfileNicknameValidUiState.Valid)
    val profileNicknameValidUiState = _profileNicknameValidUiState.asStateFlow()

    private val _signUpEvent = Channel<SignUpUiEvent>()
    val signUpEvent = _signUpEvent.receiveAsFlow()

    fun onNickNameChanged(nickname: String) {
        _signUpInfo.update { it.copy(nickname = nickname) }
    }

    fun verifyNickname() {
        viewModelScope.launch {
            if (_signUpInfo.value.nickname.isEmpty()) {
                _profileNicknameValidUiState.update { ProfileNicknameValidUiState.Valid }
                return@launch
            }
            verifyNicknameUseCase(
                nickname = _signUpInfo.value.nickname,
                onError = { error ->
                    when (error) {
                        ClientError.NicknameError.FormatInvalid ->
                            _profileNicknameValidUiState.update { ProfileNicknameValidUiState.InValidFormat }

                        ClientError.NicknameError.Duplicated ->
                            _profileNicknameValidUiState.update { ProfileNicknameValidUiState.InValidDuplicated }

                        else -> _signUpEvent.send(SignUpUiEvent.VerifyFail)
                    }
                },
            ).collect {
                _profileNicknameValidUiState.update { ProfileNicknameValidUiState.Valid }
            }
        }
    }

    fun onImageChanged(imageUri: String?) {
        _signUpInfo.update { it.copy(profileImage = imageUri) }
    }

    fun signUp() {
        viewModelScope.launch {
            if (_signUpInfo.value.nickname.isEmpty() ||
                profileNicknameValidUiState.value !is ProfileNicknameValidUiState.Valid
            ) {
                _signUpEvent.send(SignUpUiEvent.NicknameInValid)
                return@launch
            }
            viewModelScope.launch {
                signUpUseCase(
                    signUpUiModel.value.toDomain(),
                    onError = {
                        when (it) {
                            ResponseError.INVALID_ARGUMENT -> _signUpEvent.send(SignUpUiEvent.NicknameInValid)
                            ResponseError.DUPLICATE_RESOURCE -> _signUpEvent.send(SignUpUiEvent.NicknameInValid)
                            else -> _signUpEvent.send(SignUpUiEvent.SignUpFail)
                        }
                    },
                ).collect {
                    _signUpEvent.send(SignUpUiEvent.SignUpSuccess)
                }
            }
        }
    }
}

