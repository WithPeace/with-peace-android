package com.withpeace.withpeace.core.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.model.image.ImageFolder
import com.withpeace.withpeace.core.domain.repository.ImageRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetAllFoldersUseCaseTest {
    private lateinit var getAllFoldersUseCase: GetAllFoldersUseCase
    private val imageRepository: ImageRepository = mockk()

    @Test
    fun `모든 폴더를 가져올 수 있다`() = runTest {
        // given
        val testFolders = List(10) {
            ImageFolder(
                folderName = "Landon Bradley",
                representativeImageUri = "cum",
                imageCount = 4322,
            )
        }
        coEvery { imageRepository.getFolders() } returns testFolders
        getAllFoldersUseCase = GetAllFoldersUseCase(imageRepository)
        // when
        val actual = getAllFoldersUseCase()
        // then
        assertThat(actual).isEqualTo(testFolders)
    }
}
