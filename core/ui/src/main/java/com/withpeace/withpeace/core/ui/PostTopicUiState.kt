package com.withpeace.withpeace.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.withpeace.withpeace.core.domain.model.post.PostTopic

enum class PostTopicUiState(
    val topic: PostTopic,
    @StringRes val textResId: Int,
    @DrawableRes val iconResId: Int,
) {
    FREE(PostTopic.FREEDOM, R.string.free, R.drawable.ic_freedom),
    INFORMATION(PostTopic.INFORMATION, R.string.information, R.drawable.ic_information),
    QUESTION(PostTopic.QUESTION, R.string.question, R.drawable.ic_question),
    LIFE(PostTopic.LIVING, R.string.life, R.drawable.ic_life),
    HOBBY(PostTopic.HOBBY, R.string.hobby, R.drawable.ic_hobby),
    ECONOMY(PostTopic.ECONOMY, R.string.economy, R.drawable.ic_economy);

    companion object {
        fun create(topic: PostTopic) = PostTopicUiState.entries.find {
            it.topic == topic
        } ?: throw IllegalStateException("UI에 정의하지 않은 Topic이 있어요!")
    }
}
