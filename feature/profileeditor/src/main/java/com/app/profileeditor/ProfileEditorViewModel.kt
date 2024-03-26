package com.app.profileeditor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.profileeditor.navigation.PROFILE_IMAGE_URL_ARGUMENT
import com.app.profileeditor.navigation.PROFILE_NICKNAME_ARGUMENT
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.profile.ChangingProfileInfo
import com.withpeace.withpeace.core.domain.usecase.VerifyNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val verifyNicknameUseCase: VerifyNicknameUseCase,
) : ViewModel() {
    val baseProfileInfo = ChangingProfileInfo(
        nickname = savedStateHandle.get<String>(PROFILE_NICKNAME_ARGUMENT) ?: "",
        profileImage = savedStateHandle.get<String>(PROFILE_IMAGE_URL_ARGUMENT) ?: "default.png",
    ) // 최초 정보에서 변경사항이 있는지 비교를 위한 필드

    private val _profileEditUiState =
        MutableStateFlow<ProfileEditUiState>(
            ProfileEditUiState.NoChanges,
        )
    val profileEditUiState = _profileEditUiState.asStateFlow()

    private val _profileEditUiEvent = Channel<ProfileEditUiEvent>()
    val profileEditUiEvent = _profileEditUiEvent.receiveAsFlow()

    fun onImageChanged(imageUri: String) {
        _profileEditUiState.update {
            val updateData = ProfileEditUiState.Editing(
                (it as? ProfileEditUiState.Editing)?.nickname
                    ?: baseProfileInfo.nickname.value,
                profileImage = imageUri,
                isBasicTextValid = false,
            )
            if (baseProfileInfo.isSameTo(updateData.nickname, updateData.profileImage)) {
                return@update ProfileEditUiState.NoChanges
            }
            updateData
        }
    }

    fun onNickNameChanged(nickname: String) {
        _profileEditUiState.update {
            val updateData = ProfileEditUiState.Editing(
                nickname = nickname,
                profileImage = (it as? ProfileEditUiState.Editing)?.profileImage
                    ?: baseProfileInfo.profileImage ?: "default.png",
                isBasicTextValid = false,
            ) // Editing 중이면 값을 갱신, 아닐 경우 기본 값에 nickname만 값을 추가
            if (baseProfileInfo.isSameTo(updateData.nickname, updateData.profileImage)) {
                return@update ProfileEditUiState.NoChanges
            } // 변경 값이 기본 값일 경우 noChanges 상태
            updateData
        }
    }

    fun verifyNickname() {
        if (profileEditUiState.value is ProfileEditUiState.NoChanges) {
            return
        }
        viewModelScope.launch {
            verifyNicknameUseCase(
                onError = { error ->
                    this.launch {
                        _profileEditUiEvent.send(
                            when (error) {
                                is WithPeaceError.GeneralError -> {
                                    when (error.code) {
                                        1 -> ProfileEditUiEvent.ShowInvalidFormat
                                        2 -> ProfileEditUiEvent.ShowDuplicate
                                        else -> ProfileEditUiEvent.ShowFailure
                                    }
                                }

                                else -> ProfileEditUiEvent.ShowFailure
                            },
                        )
                    }
                },
                nickname = (profileEditUiState.value as ProfileEditUiState.Editing).nickname,
            ).collect { verified ->
                if (verified) {
                    _profileEditUiEvent.send(
                        ProfileEditUiEvent.ShowNicknameVerified,
                    )
                }
            }
        }
    }

    fun updateProfile() {
        if (_profileEditUiState.value is ProfileEditUiState.NoChanges) {
            viewModelScope.launch {
                _profileEditUiEvent.send(ProfileEditUiEvent.ShowUnchanged)
            }
        }
    }
}
// 1. 하단 문구 및 오류 표시
// 2. updateProfileUsecase() 3개 있어야겠다. ㅋㅋ
// 3. 오류 표시
// 4, 이전 화면 갱신