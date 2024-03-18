package com.withpeace.withpeace.core.data.repository

import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnFailure
import com.withpeace.withpeace.core.data.mapper.roleToDomain
import com.withpeace.withpeace.core.datastore.dataStore.TokenPreferenceDataSource
import com.withpeace.withpeace.core.domain.model.role.Role
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import com.withpeace.withpeace.core.network.di.request.SignUpRequest
import com.withpeace.withpeace.core.network.di.service.AuthService
import com.withpeace.withpeace.core.network.di.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DefaultTokenRepository @Inject constructor(
    private val tokenPreferenceDataSource: TokenPreferenceDataSource,
    private val authService: AuthService,
    private val userService: UserService,
) : TokenRepository {
    override suspend fun isLogin(): Boolean {
        val token = tokenPreferenceDataSource.accessToken.firstOrNull()
        return token != null
    }

    override suspend fun signUp(
        email: String,
        nickname: String,
        onError: (String) -> Unit,
    ): Flow<Unit> = flow {
        userService.signUp(
            SignUpRequest(email = email, nickname = nickname, deviceToken = null),
        ).suspendMapSuccess {
            val data = this.data
            tokenPreferenceDataSource.updateAccessToken(data.accessToken)
            tokenPreferenceDataSource.updateRefreshToken(data.refreshToken)
            emit(Unit)
        }.suspendOnFailure { onError(message()) }
    }.flowOn(Dispatchers.IO)

    override fun getTokenByGoogle(
        idToken: String,
        onError: (String) -> Unit,
    ): Flow<Role> = flow {
        authService.googleLogin(AUTHORIZATION_FORMAT.format(idToken)).suspendMapSuccess {
            val data = this.data
            tokenPreferenceDataSource.updateAccessToken(data.tokenResponse.accessToken)
            tokenPreferenceDataSource.updateRefreshToken(data.tokenResponse.refreshToken)
            emit(data.role.roleToDomain())
        }.suspendOnFailure { onError(message()) }
    }.flowOn(Dispatchers.IO)

    companion object {
        private const val AUTHORIZATION_FORMAT = "Bearer %s"
    }
}
