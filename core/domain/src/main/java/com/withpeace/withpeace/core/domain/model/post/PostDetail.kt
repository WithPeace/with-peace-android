package com.withpeace.withpeace.core.domain.model.post

import com.withpeace.withpeace.core.domain.model.date.Date

data class PostDetail(
    val user: PostUser,
    val id: Long,
    val title: PostTitle,
    val content: PostContent,
    val postTopic: PostTopic,
    val imageUrls: List<String>,
    val createDate: Date,
)

data class PostUser(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
)
