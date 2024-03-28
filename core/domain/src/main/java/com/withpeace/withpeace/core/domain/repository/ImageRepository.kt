package com.withpeace.withpeace.core.domain.repository

import com.withpeace.withpeace.core.domain.model.image.ImageFolder

interface ImageRepository {

    suspend fun getFolders(): List<ImageFolder>

    suspend fun getImages(
        page: Int,
        loadSize: Int,
        folderName: String?,
    ): List<String>
}
