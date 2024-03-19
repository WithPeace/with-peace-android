package com.withpeace.withpeace.feature.gallery

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.designsystem.R
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceCompleteButton
import com.withpeace.withpeace.core.domain.model.image.ImageFolder
import com.withpeace.withpeace.core.domain.model.image.LimitedImages
import com.withpeace.withpeace.feature.gallery.R.drawable
import com.withpeace.withpeace.feature.gallery.R.string
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import java.text.NumberFormat
import java.util.Locale

@Composable
fun GalleryRoute(
    viewModel: GalleryViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
    onCompleteRegisterImages: (List<String>) -> Unit,
) {
    val noMoreImageMessage = stringResource(id = string.no_more_image_select)
    val allFolders = viewModel.allFolders.collectAsStateWithLifecycle().value
    val pagingImages = viewModel.images.collectAsLazyPagingItems()
    val selectedImageList = viewModel.selectedImages.collectAsStateWithLifecycle().value
    val selectedFolder = viewModel.selectedFolder.collectAsStateWithLifecycle().value

    BackHandler { // 스마트폰 뒤로가기 제어
        if (selectedFolder == null) {
            onClickBackButton()
        } else {
            viewModel.onSelectFolder(null)
        }
    }
    GalleryScreen(
        allFolders = allFolders,
        onSelectImage = viewModel::onSelectImage,
        onClickBackButton = onClickBackButton,
        onCompleteRegisterImages = onCompleteRegisterImages,
        onSelectFolder = viewModel::onSelectFolder,
        pagingImages = pagingImages,
        selectedImageList = selectedImageList,
        selectedFolder = selectedFolder,
    )

    LaunchedEffect(key1 = null) {
        viewModel.sideEffect.collectLatest {
            when (it) {
                GallerySideEffect.SelectImageFail -> onShowSnackBar(noMoreImageMessage)
            }
        }
    }
}

@Composable
fun GalleryScreen(
    onClickBackButton: () -> Unit = {},
    onCompleteRegisterImages: (List<String>) -> Unit = {},
    allFolders: List<ImageFolder>,
    onSelectFolder: (ImageFolder?) -> Unit = {},
    onSelectImage: (String) -> Unit = {},
    pagingImages: LazyPagingItems<String>,
    selectedImageList: LimitedImages,
    selectedFolder: ImageFolder?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WithpeaceTheme.colors.SystemWhite),
    ) {
        WithPeaceBackButtonTopAppBar(
            onClickBackButton = {
                if (selectedFolder == null) {
                    onClickBackButton()
                } else {
                    onSelectFolder(null)
                }
            },
            title = {
                if (selectedFolder == null) {
                    Text(
                        text = stringResource(string.gallery_folder_topbar_title),
                        style = WithpeaceTheme.typography.title1,
                    )
                } else {
                    Text(
                        text = stringResource(
                            string.selected_images_count,
                            selectedImageList.currentCount,
                            selectedImageList.maxCount,
                        ),
                        style = WithpeaceTheme.typography.title1,
                    )
                }
            },
            actions = {
                if (selectedFolder != null) {
                    WithPeaceCompleteButton(
                        modifier = Modifier.padding(end = 23.dp),
                        onClick = { onCompleteRegisterImages(selectedImageList.urls) },
                        enabled = selectedImageList.urls.isNotEmpty(),
                    )
                }
            },
        )
        if (selectedFolder == null) {
            FolderList(
                allFolders = allFolders,
                onSelectFolder = onSelectFolder,
            )
        } else {
            ImageList(
                pagingImages = pagingImages,
                selectedImageList = selectedImageList,
                onSelectImage = onSelectImage,
            )
        }
    }
}

@Composable
fun FolderList(
    modifier: Modifier = Modifier,
    allFolders: List<ImageFolder>,
    onSelectFolder: (ImageFolder) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = allFolders,
        ) { folder ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clickable {
                        onSelectFolder(folder)
                    },
            ) {
                GlideImage(
                    imageModel = { Uri.parse(folder.representativeImageUri) },
                    imageOptions = ImageOptions(),
                    previewPlaceholder = R.drawable.ic_backarrow_right,
                )
                Column(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    WithpeaceTheme.colors.SystemBlack.copy(alpha = 0.5f),
                                ),
                            ),
                        )
                        .padding(bottom = 8.dp, start = 8.dp)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .aspectRatio(2.2f),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Text(
                        text = folder.folderName,
                        style = WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SystemWhite,
                    )
                    Text(
                        text = NumberFormat.getNumberInstance(Locale.US).format(folder.imageCount),
                        style = WithpeaceTheme.typography.caption,
                        color = WithpeaceTheme.colors.SystemWhite,
                    )
                }
            }
        }
    }
}

@Composable
fun ImageList(
    modifier: Modifier = Modifier,
    pagingImages: LazyPagingItems<String>,
    selectedImageList: LimitedImages,
    onSelectImage: (String) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        columns = GridCells.Fixed(3),
    ) {
        items(
            pagingImages.itemCount,
        ) { index ->
            val uriString = pagingImages[index] ?: throw IllegalStateException("uri가 존재하지 않음")

            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clickable {
                        onSelectImage(uriString)
                    },
            ) {
                GlideImage(
                    modifier = Modifier.align(Alignment.Center),
                    imageModel = { Uri.parse(uriString) },
                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    previewPlaceholder = R.drawable.ic_backarrow_right,
                )
                if (selectedImageList.contains(uriString)) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(WithpeaceTheme.colors.SystemGray3.copy(alpha = 0.5f)),
                    ) {
                        Image(
                            modifier = Modifier.align(Alignment.Center),
                            painter = painterResource(id = drawable.ic_picture_select),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GalleryScreenPreview() {
    WithpeaceTheme {
        GalleryScreen(
            onClickBackButton = {},
            onCompleteRegisterImages = {},
            allFolders = List(10) {
                ImageFolder(
                    folderName = "Phillip McClain",
                    representativeImageUri = "per",
                    imageCount = 7380,
                )
            },
            pagingImages = flowOf(
                PagingData.from(
                    List(10) { "" },
                    sourceLoadStates =
                    LoadStates(
                        refresh = LoadState.NotLoading(false),
                        append = LoadState.NotLoading(false),
                        prepend = LoadState.NotLoading(false),
                    ),
                ),
            ).collectAsLazyPagingItems(),
            selectedImageList = LimitedImages(listOf("", "")),
            selectedFolder = ImageFolder(
                folderName = "Phillip McClain",
                representativeImageUri = "per",
                imageCount = 7380,
            ),
        )
    }
}

