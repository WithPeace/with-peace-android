package com.withpeace.withpeace.core.imagestorage

import android.net.Uri

interface ImageDataSource {
    suspend fun getImages(
        page: Int,
        loadSize: Int,
        folder: String?,
    ):List<Uri>

    suspend fun getFolders(): List<ImageFolderEntity>
}
