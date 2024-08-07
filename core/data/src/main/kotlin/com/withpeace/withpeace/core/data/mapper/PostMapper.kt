package com.withpeace.withpeace.core.data.mapper

import com.withpeace.withpeace.core.domain.model.date.Date
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.network.di.response.post.PostResponse

fun PostResponse.toDomain() =
    Post(
        postId = postId,
        title = title,
        content = content,
        postTopic = type.toDomain(),
        createDate = Date(createDate.toLocalDateTime()),
        postImageUrl = postImageUrl,
        commentCount = commentCount,
    )
