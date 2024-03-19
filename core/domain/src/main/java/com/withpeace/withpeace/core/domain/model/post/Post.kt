package com.withpeace.withpeace.core.domain.model.post

import com.withpeace.withpeace.core.domain.model.date.Date

data class Post(
    val postId: Long,
    val title: String,
    val content: String,
    val postTopic: PostTopic,
    val createDate: Date,
    val postImageUrl: String,
)
