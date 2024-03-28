package com.withpeace.withpeace.feature.postdetail

import com.withpeace.withpeace.core.domain.model.post.PostDetail

sealed interface PostDetailUiState {
    data object Loading : PostDetailUiState
    data class Success(val postDetail: PostDetail) : PostDetailUiState
    data object Fail : PostDetailUiState
}
