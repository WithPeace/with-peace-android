package com.withpeace.withpeace.core.domain.model.post

import com.withpeace.withpeace.core.domain.model.image.LimitedImages

data class RegisterPost(
    val title: String,
    val content: String,
    val topic: PostTopic?,
    val images: LimitedImages,
)
