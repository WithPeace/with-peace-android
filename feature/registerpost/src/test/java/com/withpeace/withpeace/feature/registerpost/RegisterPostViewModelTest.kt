package com.withpeace.withpeace.feature.registerpost

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.model.image.LimitedImages
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.model.post.RegisterPost
import com.withpeace.withpeace.core.domain.usecase.RegisterPostUseCase
import com.withpeace.withpeace.core.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegisterPostViewModelTest {
    private lateinit var viewModel: RegisterPostViewModel
    private val registerPostUseCase: RegisterPostUseCase = mockk(relaxed = true)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        viewModel = RegisterPostViewModel(registerPostUseCase)
    }

    @Test
    fun `처음 내용은 모두 비어있다`() {
        // when & then
        assertThat(viewModel.postUiState.value).isEqualTo(
            RegisterPost(
                title = "",
                content = "",
                topic = null,
                images = LimitedImages(emptyList()),
            ),
        )
    }

    @Test
    fun `제목을 바꿀 수 있다`() {
        // when
        viewModel.onTitleChanged("제목")
        // then
        assertThat(viewModel.postUiState.value.title).isEqualTo("제목")
    }

    @Test
    fun `내용을 바꿀 수 있다`() {
        // when
        viewModel.onContentChanged("내용")
        // then
        assertThat(viewModel.postUiState.value.content).isEqualTo("내용")
    }

    @Test
    fun `주제를 바꿀 수 있다`() {
        // when
        viewModel.onTopicChanged(PostTopic.FREEDOM)
        // then
        assertThat(viewModel.postUiState.value.topic).isEqualTo(PostTopic.FREEDOM)
    }

    @Test
    fun `이미지 여러개를 추가할 수 있다`() {
        // when
        viewModel.onImageUrlsAdded(listOf("1", "2"))
        // then
        assertThat(viewModel.postUiState.value.images.urls).isEqualTo(listOf("1", "2"))
    }

    @Test
    fun `이미지를 삭제할 수 있다`() {
        // given
        viewModel.onImageUrlsAdded(listOf("1,", "2"))
        // when
        viewModel.onImageUrlDeleted(0)
        // then
        assertThat(viewModel.postUiState.value.images.urls).isEqualTo(listOf("2"))
    }

    @Test
    fun `제목,내용,주제 모두 비어있을 때, 게시글 등록하면, TitleBlank 이벤트가 발생한다`() = runTest {
        // when
        viewModel.onRegisterPostCompleted()
        // then
        viewModel.uiEvent.test {
            val actual = awaitItem()
            assertThat(actual).isEqualTo(RegisterPostUiEvent.TitleBlank)
        }
    }

    @Test
    fun `주제,내용이 비어있을 때, 게시글 등록하면, ContentBlank 이벤트가 발생한다`() = runTest {
        // given
        viewModel.onTitleChanged("제목")
        // when
        viewModel.onRegisterPostCompleted()
        // then
        viewModel.uiEvent.test {
            val actual = awaitItem()
            assertThat(actual).isEqualTo(RegisterPostUiEvent.ContentBlank)
        }
    }

    @Test
    fun `주제가 비어있을 때, 게시글 등록하면, TopicBlank 이벤트가 발생한다`() = runTest {
        // given
        viewModel.onTitleChanged("제목")
        viewModel.onContentChanged("내용")
        // when
        viewModel.onRegisterPostCompleted()
        // then
        viewModel.uiEvent.test {
            val actual = awaitItem()
            assertThat(actual).isEqualTo(RegisterPostUiEvent.TopicBlank)
        }
    }

    @Test
    fun `게시글 내용이 모두 채워졌고, 성공적으로 게시글 등록하면, 등록 성공 이벤트가 발생한다`() = runTest {
        // given
        viewModel.onContentChanged("내용")
        viewModel.onTopicChanged(PostTopic.FREEDOM)
        viewModel.onTitleChanged("제목")
        coEvery {
            registerPostUseCase(
                post = RegisterPost(
                    title = "제목", content = "내용", topic = PostTopic.FREEDOM,
                    images = LimitedImages(
                        urls = listOf(),
                        maxCount = 5,
                        alreadyExistCount = 0,
                    ),
                ),
                onError = any(),
            )
        } returns flow { emit(1L) }
        // when
        viewModel.onRegisterPostCompleted()
        // then
        viewModel.uiEvent.test {
            val actual = awaitItem()
            assertThat(actual).isEqualTo(RegisterPostUiEvent.PostSuccess)
        }
    }

    @Test
    fun `게시글 내용이 모두 채워졌고, 게시글 등록에 실패하면, 등록 실패 이벤트가 발생한다`() = runTest {
        // given
        viewModel.onContentChanged("내용")
        viewModel.onTopicChanged(PostTopic.FREEDOM)
        viewModel.onTitleChanged("제목")
        val slot = slot<suspend (WithPeaceError) -> Unit>()
        coEvery {
            registerPostUseCase(
                post = any(),
                onError = capture(slot),
            )
        } returns flow { slot.captured.invoke(WithPeaceError.GeneralError()) }
        // when
        viewModel.onRegisterPostCompleted()
        // then
        viewModel.uiEvent.test {
            val actual = awaitItem()
            assertThat(actual).isEqualTo(RegisterPostUiEvent.PostFail(WithPeaceError.GeneralError()))
        }
    }
}
