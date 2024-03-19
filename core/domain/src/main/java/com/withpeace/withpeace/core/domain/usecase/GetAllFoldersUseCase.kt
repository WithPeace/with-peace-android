package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.image.ImageFolder
import com.withpeace.withpeace.core.domain.repository.ImageRepository
import javax.inject.Inject

class GetAllFoldersUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
) {
    suspend operator fun invoke(): List<ImageFolder> {
        return imageRepository.getFolders()
    }
}
