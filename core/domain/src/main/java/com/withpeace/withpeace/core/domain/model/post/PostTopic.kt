package com.withpeace.withpeace.core.domain.model.post

enum class PostTopic {
    FREEDOM, INFORMATION, QUESTION, LIVING, HOBBY, ECONOMY;

    companion object {
        fun findIndex(postTopic: PostTopic) = entries.indexOf(postTopic)
    }
}
