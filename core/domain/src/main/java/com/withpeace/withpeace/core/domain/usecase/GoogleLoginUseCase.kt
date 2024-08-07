package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import javax.inject.Inject

class GoogleLoginUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {
    operator fun invoke(
        idToken: String,
        onError: suspend (CheonghaError) -> Unit,
    ) = tokenRepository.getTokenByGoogle(
        idToken = idToken,
        onError = onError,
    )
}
