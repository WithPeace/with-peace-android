package com.withpeace.withpeace.core.data.repository

import com.withpeace.withpeace.core.domain.model.image.ImageFolder
import com.withpeace.withpeace.core.domain.repository.ImageRepository
import com.withpeace.withpeace.core.imagestorage.ImageDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultImageRepository @Inject constructor(
    private val imageDataSource: ImageDataSource,
) : ImageRepository {
    override suspend fun getFolders(): List<ImageFolder> =
        withContext(Dispatchers.IO) {
            imageDataSource.getFolders().map {
                ImageFolder(
                    it.folderName,
                    it.representativeImageUri.toString(),
                    it.count,
                )
            }
        }

    override suspend fun getImages(page: Int, loadSize: Int, folderName: String?): List<String> =
        withContext(Dispatchers.IO) {
            imageDataSource.getImages(
                page = page,
                loadSize = loadSize,
                folder = folderName,
            ).map { it.toString() }
        }
}
