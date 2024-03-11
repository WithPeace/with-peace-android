package com.withpeace.withpeace.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.withpeace.withpeace.core.domain.model.ImageFolder
import com.withpeace.withpeace.core.domain.model.LimitedImages
import com.withpeace.withpeace.core.domain.usecase.GetAllFoldersUseCase
import com.withpeace.withpeace.core.domain.usecase.GetLocalImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val getLocalImagesUseCase: GetLocalImagesUseCase,
) : ViewModel() {

    private val _allFolders = MutableStateFlow<List<ImageFolder>>(emptyList())
    val allFolders = _allFolders.asStateFlow()

    private val _selectedFolder = MutableStateFlow<ImageFolder?>(null)
    val selectedFolder = _selectedFolder.asStateFlow()

    val images = selectedFolder.map { imageFolder ->
        if (imageFolder == null) PagingData.empty()
        else getLocalImagesUseCase(imageFolder.folderName)
            .cachedIn(viewModelScope) // Paging 정보를 화면 회전에도 날라가지 않게 하기 위함
            .firstOrNull()
            ?: PagingData.empty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PagingData.empty())

    private val _selectedImages = MutableStateFlow(LimitedImages(emptyList(), 5))
    val selectedImages = _selectedImages.asStateFlow()

    init {
        viewModelScope.launch {
            _allFolders.update { getAllFoldersUseCase() }
        }
    }

    fun onSelectFolder(imageFolder: ImageFolder?) {
        _selectedFolder.value = imageFolder
    }

    fun onSelectImage(uriString: String) {
        if (selectedImages.value.contains(uriString)) {
            _selectedImages.update { it.deleteImage(uriString) }
        } else if (selectedImages.value.canAddImage()) {
            _selectedImages.update { it.addImage(uriString) }
        }
    }
}
