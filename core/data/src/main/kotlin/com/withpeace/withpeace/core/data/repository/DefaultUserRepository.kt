package com.withpeace.withpeace.core.data.repository

import android.content.Context
import android.net.Uri
import com.skydoves.sandwich.messageOrNull
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.data.util.convertToFile
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.profile.Nickname
import com.withpeace.withpeace.core.domain.model.profile.ProfileInfo
import com.withpeace.withpeace.core.domain.repository.UserRepository
import com.withpeace.withpeace.core.network.di.request.NicknameRequest
import com.withpeace.withpeace.core.network.di.service.UserService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userService: UserService,
) : UserRepository {
    override fun getProfile(
        onError: (WithPeaceError) -> Unit,
    ): Flow<ProfileInfo> = flow {
        userService.getProfile().suspendMapSuccess {
            emit(this.data.toDomain())
        }.suspendOnError {
            if (statusCode.code == 401) onError(WithPeaceError.UnAuthorized())
            else onError(WithPeaceError.GeneralError(statusCode.code, messageOrNull))
        }.suspendOnException {
            onError(WithPeaceError.GeneralError(message = messageOrNull))
        }
    }

    override fun registerProfile(
        nickname: String,
        profileImage: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun updateProfile(
        nickname: String,
        profileImage: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<Unit> = flow {
        val requestFile: File = Uri.parse(profileImage).convertToFile(context)
        val imageRequestBody = requestFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData(
            "imageFile",
            requestFile.name,
            imageRequestBody,
        )
        userService.updateProfile(
            nickname.toRequestBody("text/plain".toMediaTypeOrNull()), imagePart,
        ).suspendMapSuccess {
            emit(Unit)
        }.suspendOnError {
            if (statusCode.code == 401) onError(WithPeaceError.UnAuthorized())
            else onError(WithPeaceError.GeneralError(statusCode.code, messageOrNull))
        }.suspendOnException {
            onError(WithPeaceError.GeneralError(message = messageOrNull))
        }
    }

    override fun updateNickname(nickname: String, onError: (WithPeaceError) -> Unit): Flow<Unit> =
        flow {
            userService.updateNickname(NicknameRequest(nickname)).suspendMapSuccess {
                emit(Unit)
            }.suspendOnError {
                if (statusCode.code == 401) onError(WithPeaceError.UnAuthorized())
                else onError(WithPeaceError.GeneralError(statusCode.code, messageOrNull))
            }.suspendOnException {
                onError(WithPeaceError.GeneralError(message = messageOrNull))
            }
        }

    override fun updateProfileImage(
        profileImage: String,
        onError: (WithPeaceError) -> Unit,
    ): Flow<Unit> = flow {
        val requestFile: File = Uri.parse(profileImage).convertToFile(context)
        val imageRequestBody = requestFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData(
            "imageFile",
            requestFile.name,
            imageRequestBody,
        )
        userService.updateImage(imagePart).suspendMapSuccess {

            emit(Unit)
        }.suspendOnError {
            if (statusCode.code == 401) onError(WithPeaceError.UnAuthorized())
            else onError(WithPeaceError.GeneralError(statusCode.code, messageOrNull))
        }.suspendOnException {
            onError(WithPeaceError.GeneralError(message = messageOrNull))
        }
    }

    override fun verifyNicknameDuplicated(
        nickname: Nickname,
        onError: (WithPeaceError) -> Unit,
    ): Flow<Boolean> = flow {
        userService.isNicknameDuplicate(nickname.value).suspendMapSuccess {
            if (this.data == "true") {
                onError(WithPeaceError.GeneralError(code = 2))
                return@suspendMapSuccess emit(false)
            } else {
                emit(true)
            }
        }.suspendOnError {
            onError(WithPeaceError.GeneralError(statusCode.code, messageOrNull))
        }.suspendOnException {
            onError(WithPeaceError.GeneralError(message = messageOrNull))
        }
    }
}