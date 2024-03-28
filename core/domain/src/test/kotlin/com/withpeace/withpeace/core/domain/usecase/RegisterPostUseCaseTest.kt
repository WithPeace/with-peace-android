package com.withpeace.withpeace.core.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.model.image.LimitedImages
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.model.post.RegisterPost
import com.withpeace.withpeace.core.domain.repository.PostRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RegisterPostUseCaseTest {
    private lateinit var registerPostUseCase: RegisterPostUseCase
    private val postRepository = mockk<PostRepository>()

    @Before
    fun setup() {
        registerPostUseCase = RegisterPostUseCase(postRepository)
    }

    @Test
    fun `게시글을 등록을 성공하면, 게시글 아이디를 반환한다`() = runTest {
        // given
        val testRegisterPost = RegisterPost(
            title = "title", content = "content", topic = PostTopic.ECONOMY,
            images = LimitedImages(
                urls = listOf(),
                maxCount = 9536,
                alreadyExistCount = 6212,
            ),
        )
        coEvery {
            postRepository.registerPost(
                testRegisterPost,
                onError = any(),
            )
        } returns flow { emit(1L) }
        // when
        val actual = registerPostUseCase(testRegisterPost, {}).first()
        assertThat(actual).isEqualTo(1L)
    }

    @Test
    fun `게시글을 등록을 실패하면, 실패 람다를 실행한다`() = runTest {
        // given
        val errorMock = mockk<suspend (WithPeaceError) -> Unit>(relaxed = true)
        val errorSlot = slot<suspend (WithPeaceError) -> Unit>()
        coEvery {
            postRepository.registerPost(
                testRegisterPost,
                onError = capture(errorSlot),
            )
        } returns flow { errorSlot.captured.invoke(WithPeaceError.GeneralError()) }
        // when
        registerPostUseCase(testRegisterPost) { errorMock.invoke(it) }.toList()
        coVerify { errorMock.invoke(WithPeaceError.GeneralError()) }
    }

    private val testRegisterPost = RegisterPost(
        title = "title", content = "content", topic = PostTopic.ECONOMY,
        images = LimitedImages(
            urls = listOf(),
            maxCount = 9536,
            alreadyExistCount = 6212,
        ),
    )
}
