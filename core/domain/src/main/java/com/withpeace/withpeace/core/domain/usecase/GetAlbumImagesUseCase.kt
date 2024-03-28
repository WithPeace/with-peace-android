package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.image.ImagePagingInfo
import com.withpeace.withpeace.core.domain.paging.ImagePagingSource
import com.withpeace.withpeace.core.domain.repository.ImageRepository
import javax.inject.Inject

class GetAlbumImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
) {
    operator fun invoke(selectedFolderName: String): ImagePagingInfo = ImagePagingInfo(
        pageSize = PAGE_SIZE,
        pagingSource = ImagePagingSource(
            imageRepository,
            selectedFolderName,
            ),
    )

    companion object {
        private const val PAGE_SIZE = 30
    }
}
