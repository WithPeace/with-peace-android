package com.withpeace.withpeace.feature.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class PostListViewModel @Inject constructor() : ViewModel() {

    private val _currentTopic = MutableStateFlow(PostTopic.FREEDOM)
    val currentTopic = _currentTopic.asStateFlow()

    val postList = currentTopic.map {
        getPost(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun onTopicChanged(postTopic: PostTopic) {
        _currentTopic.update { postTopic }
    }

    // API 나오기 전까지 임시로 이렇게 하겠습니다!
    private fun getPost(postTopic: PostTopic) = List(10) {
        Post(
            postId = it.toLong(),
            title = postTopic.toString(),
            content = postTopic.toString()+"\n${postTopic.toString()}",
            postTopic = postTopic,
            createDate = LocalDateTime.of(LocalDate.of(2024, 3, 18), LocalTime.of(12, 0, 0)),
            postImageUrl = "http://withpeace.s3-website.kr.object.ncloudstorage.com/userProfile/1",
        )
    }
}
