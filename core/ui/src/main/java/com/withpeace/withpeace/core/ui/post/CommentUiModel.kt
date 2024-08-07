package com.withpeace.withpeace.core.ui.post

import com.withpeace.withpeace.core.domain.model.post.Comment
import com.withpeace.withpeace.core.ui.DateUiModel
import com.withpeace.withpeace.core.ui.toUiModel
import java.time.LocalDateTime

data class CommentUiModel(
    val id: Long,
    val content: String,
    val createDate: DateUiModel = DateUiModel(
        LocalDateTime.now(),
    ),
    val commentUser: CommentUserUiModel,
    val isMyComment: Boolean,
)

data class CommentUserUiModel(
    val id: Long,
    val nickname: String,
    val profileImageUrl: String,
)

fun Comment.toUiModel(currentUserId: Long) = CommentUiModel(
    id = commentId,
    content = content,
    createDate = createDate.toUiModel(),
    commentUser = CommentUserUiModel(
        id = commentUser.id,
        nickname = commentUser.nickname,
        profileImageUrl = commentUser.profileImageUrl,
    ),
    isMyComment = currentUserId == commentUser.id,
)
