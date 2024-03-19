package com.withpeace.withpeace.core.domain.model.image

import androidx.paging.PagingConfig
import androidx.paging.PagingSource

data class ImagePagingInfo(
    val pageSize: Int,
    val enablePlaceholders: Boolean = true,
    val pagingSource: PagingSource<Int, String>,
) {
    val pagingConfig = PagingConfig(
        pageSize = pageSize,
        enablePlaceholders = enablePlaceholders,
    )
}
