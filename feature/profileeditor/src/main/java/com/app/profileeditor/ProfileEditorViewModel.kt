package com.app.profileeditor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.profileeditor.navigation.PROFILE_IMAGE_URL_ARGUMENT
import com.app.profileeditor.navigation.PROFILE_NICKNAME_ARGUMENT
import com.app.profileeditor.uistate.ProfileEditUiEvent
import com.app.profileeditor.uistate.ProfileUiModel
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.usecase.UpdateProfileUseCase
import com.withpeace.withpeace.core.domain.usecase.VerifyNicknameUseCase
import com.withpeace.withpeace.core.ui.profile.ProfileNicknameValidUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import toDomain
import toUiModel
import javax.inject.Inject

@HiltViewModel
class ProfileEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val verifyNicknameUseCase: VerifyNicknameUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : ViewModel() {
    private val baseProfileInfo = ProfileUiModel(
        nickname = savedStateHandle.get<String>(PROFILE_NICKNAME_ARGUMENT) ?: "",
        profileImage = savedStateHandle.get<String>(PROFILE_IMAGE_URL_ARGUMENT) ?: "default.png",
        isChanged = false,
    ) // 최초 정보에서 변경사항이 있는지 비교를 위한 필드

    private val _profileEditUiState =
        MutableStateFlow(
            ProfileUiModel(baseProfileInfo.nickname, baseProfileInfo.profileImage, false),
        )
    val profileEditUiState = _profileEditUiState.asStateFlow()

    private val _profileEditUiEvent = Channel<ProfileEditUiEvent>()
    val profileEditUiEvent = _profileEditUiEvent.receiveAsFlow()

    private val _profileNicknameValidUiState =
        MutableStateFlow<ProfileNicknameValidUiState>(ProfileNicknameValidUiState.Valid)
    val profileNicknameValidUiState = _profileNicknameValidUiState.asStateFlow()

    fun onImageChanged(imageUri: String) {
        _profileEditUiState.update {
            return@update it.toDomain().copy(profileImage = imageUri).toUiModel(baseProfileInfo)
        }
    }

    fun onNickNameChanged(nickname: String) {
        _profileNicknameValidUiState.update { ProfileNicknameValidUiState.UnVerified }
        _profileEditUiState.update {
            return@update it.toDomain().copy(nickname = nickname).toUiModel(baseProfileInfo)
        }
    }

    fun verifyNickname() { // 닉네임만 바뀐 경우, 기본 값이 아닌 경우
        if (_profileEditUiState.value.nickname == baseProfileInfo.nickname) {
            return _profileNicknameValidUiState.update { ProfileNicknameValidUiState.Valid }
        }
        viewModelScope.launch {
            verifyNicknameUseCase(
                onError = { error ->
                    when (error) {
                        ClientError.NicknameError.FormatInvalid ->
                            _profileNicknameValidUiState.update { ProfileNicknameValidUiState.InValidFormat }

                        ClientError.NicknameError.Duplicated ->
                            _profileNicknameValidUiState.update { ProfileNicknameValidUiState.InValidDuplicated }

                        else -> _profileEditUiEvent.send(ProfileEditUiEvent.UpdateFailure)
                    }
                },
                nickname = _profileEditUiState.value.nickname,
            ).collect {
                _profileNicknameValidUiState.update { ProfileNicknameValidUiState.Valid }
            }
        }
    }

    fun updateProfile() {
        viewModelScope.launch {
            updateProfileUseCase(
                beforeProfile = baseProfileInfo.toDomain(),
                afterProfile = _profileEditUiState.value.toDomain(),
                onError = {
                    _profileEditUiEvent.send(
                        when (it) {
                            ResponseError.INVALID_ARGUMENT -> ProfileEditUiEvent.NicknameInvalidFormat
                            ResponseError.DUPLICATE_RESOURCE -> ProfileEditUiEvent.NicknameDuplicated
                            ClientError.ProfileNotChanged -> ProfileEditUiEvent.ProfileUnchanged
                            ClientError.AuthExpired -> ProfileEditUiEvent.UnAuthorized
                            else -> ProfileEditUiEvent.UpdateFailure
                        },
                    )
                },
            ).collect {
                _profileEditUiEvent.send(
                    ProfileEditUiEvent.UpdateSuccess(
                        it.nickname?.value ?: _profileEditUiState.value.nickname,
                        it.profileImageUrl ?: _profileEditUiState.value.profileImage,
                    ),
                )
            }
        }
    }
}
