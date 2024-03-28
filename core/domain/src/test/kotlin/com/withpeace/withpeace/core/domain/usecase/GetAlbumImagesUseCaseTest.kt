package com.withpeace.withpeace.core.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.model.image.ImagePagingInfo
import com.withpeace.withpeace.core.domain.paging.ImagePagingSource
import com.withpeace.withpeace.core.domain.repository.ImageRepository
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetAlbumImagesUseCaseTest {

    private lateinit var getAlbumImagesUseCase: GetAlbumImagesUseCase
    private val imageRepository = mockk<ImageRepository>()

    @Test
    fun `폴더에 따라 이미지들을 불러올 수 있다`() = runTest() {
        // given
        getAlbumImagesUseCase = GetAlbumImagesUseCase(imageRepository)
        // when && then
        val actual = getAlbumImagesUseCase("test")
        assertThat(actual).isEqualTo(
            ImagePagingInfo(
                30,
                true,
                ImagePagingSource(imageRepository, "test"),
            ),
        )
    }
}
