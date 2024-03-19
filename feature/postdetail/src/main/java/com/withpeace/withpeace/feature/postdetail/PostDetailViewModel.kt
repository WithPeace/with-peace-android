package com.withpeace.withpeace.feature.postdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.withpeace.withpeace.core.domain.model.date.Date
import com.withpeace.withpeace.core.domain.model.post.PostContent
import com.withpeace.withpeace.core.domain.model.post.PostDetail
import com.withpeace.withpeace.core.domain.model.post.PostTitle
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.model.post.PostUser
import com.withpeace.withpeace.feature.postdetail.navigation.POST_DETAIL_ID_ARGUMENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val postId =
        checkNotNull(savedStateHandle.get<Long>(POST_DETAIL_ID_ARGUMENT)) { "게시글 아이디가 유효하지 않아요" }

    private val _postUiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val postUiState = _postUiState.asStateFlow()

    init {
        _postUiState.update {
            PostDetailUiState.Success(
                PostDetail(
                    user = PostUser(
                        id = 7947,
                        name = "Elaine Benton",
                        profileImageUrl = "https://duckduckgo.com/?q=dictas",
                    ),
                    id = 9350,
                    title = PostTitle(value = "laoreet"),
                    content = PostContent(value = "turpis"),
                    postTopic = PostTopic.INFORMATION,
                    imageUrls = listOf(),
                    createDate = Date(date = LocalDateTime.now()),
                ),
            )
        }
    }


}
