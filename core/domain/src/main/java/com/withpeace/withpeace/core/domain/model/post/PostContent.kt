package com.withpeace.withpeace.core.domain.model.post

@JvmInline
value class PostContent(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "게시글의 내용은 빈칸이어서는 안됩니다" }
    }
}
