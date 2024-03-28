package com.withpeace.withpeace.core.domain.model.image

data class LimitedImages(
    val urls: List<String>,
    val maxCount: Int = DEFAULT_MAX_COUNT,
    val alreadyExistCount: Int = DEFAULT_ALREADY_EXIST_COUNT, // 이미지를 선택하고 다시 갤러리에 온 상황 대비
) {

    val additionalCount: Int
        get() = maxCount - urls.size - alreadyExistCount

    val currentCount = urls.size + alreadyExistCount

    init {
        check(currentCount <= maxCount) { "이미지의 최대 개수는 ${maxCount}장입니다" }
    }

    fun contains(url: String)=urls.contains(url)

    fun canAddImage() = additionalCount > 0

    fun addImage(url: String) = copy(urls = urls + listOf(url))

    fun addImages(inputUrls: List<String>) = copy(urls = urls + inputUrls)

    fun deleteImage(url: String) = copy(urls = urls.filter { it != url })

    fun deleteImage(deletedIndex: Int) =
        copy(urls = urls.filterIndexed { index, s -> index != deletedIndex })

    companion object {
        const val DEFAULT_MAX_COUNT = 5
        const val DEFAULT_ALREADY_EXIST_COUNT = 0
    }
}
