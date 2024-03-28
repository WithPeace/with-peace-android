package com.withpeace.withpeace.feature.registerpost

import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.designsystem.R
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.KeyboardAware
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceCompleteButton
import com.withpeace.withpeace.core.domain.model.image.LimitedImages
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.model.post.RegisterPost
import com.withpeace.withpeace.core.permission.ImagePermissionHelper
import com.withpeace.withpeace.core.ui.PostTopicUiState
import com.withpeace.withpeace.feature.registerpost.R.drawable
import com.withpeace.withpeace.feature.registerpost.R.string
import kotlinx.coroutines.launch

@Composable
fun RegisterPostRoute(
    viewModel: RegisterPostViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    onClickedBackButton: () -> Unit,
    onCompleteRegisterPost: () -> Unit,
    onNavigateToGallery: (imageLimit: Int, imageCount: Int) -> Unit,
) {
    val context = LocalContext.current

    val postUiState = viewModel.postUiState.collectAsStateWithLifecycle().value
    val showBottomSheet = viewModel.showBottomSheet.collectAsStateWithLifecycle().value

    KeyboardAware {
    RegisterPostScreen(
        registerPostUiState = postUiState,
        onClickBackButton = onClickedBackButton,
        onTitleChanged = viewModel::onTitleChanged,
        onContentChanged = viewModel::onContentChanged,
        onTopicChanged = viewModel::onTopicChanged,
        onCompleteRegisterPost = viewModel::onRegisterPostCompleted,
        onShowBottomSheetChanged = viewModel::onShowBottomSheetChanged,
        showBottomSheet = showBottomSheet,
        onImageUrlDeleted = viewModel::onImageUrlDeleted,
        onNavigateToGallery = onNavigateToGallery,
    )
    }
    LaunchedEffect(null) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                RegisterPostUiEvent.ContentBlank -> onShowSnackBar(context.getString(string.content_blank_error_message))
                RegisterPostUiEvent.PostSuccess -> {
                    onCompleteRegisterPost()
                    onShowSnackBar("게시글 등록 가능")
                }
                is RegisterPostUiEvent.PostFail -> {
                    when(val error = event.error){
                        is WithPeaceError.GeneralError -> onShowSnackBar("${error.code}${error.message}")
                        is WithPeaceError.UnAuthorized -> onShowSnackBar("인가 되지 않은 게정이에요")
                    }
                }

                RegisterPostUiEvent.TitleBlank -> onShowSnackBar(context.getString(string.title_blank_error_message))
                RegisterPostUiEvent.TopicBlank -> viewModel.onShowBottomSheetChanged(true)
            }
        }
    }
}

@Composable
fun RegisterPostScreen(
    registerPostUiState: RegisterPost,
    onClickBackButton: () -> Unit = {},
    onTitleChanged: (String) -> Unit = {},
    onContentChanged: (String) -> Unit = {},
    onTopicChanged: (PostTopic) -> Unit = {},
    onCompleteRegisterPost: () -> Unit,
    onImageUrlDeleted: (Int) -> Unit,
    onShowBottomSheetChanged: (Boolean) -> Unit = {},
    showBottomSheet: Boolean,
    onNavigateToGallery: (imageLimit: Int, imageCount: Int) -> Unit = { _, _ -> },
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WithpeaceTheme.colors.SystemWhite),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
        ) {
            RegisterPostTopAppBar(
                onClickBackButton = onClickBackButton,
                onCompleteRegisterPost = onCompleteRegisterPost,
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.fillMaxHeight()) {
                    RegisterPostTopic(
                        topic = registerPostUiState.topic,
                        onTopicChanged = onTopicChanged,
                        onShowBottomSheetChanged = onShowBottomSheetChanged,
                        showBottomSheet = showBottomSheet,
                    )
                    RegisterPostTitle(
                        title = registerPostUiState.title, onTitleChanged = onTitleChanged,
                    )
                    RegisterPostContent(
                        modifier = Modifier.fillMaxHeight(),
                        content = registerPostUiState.content,
                        onContentChanged = onContentChanged,
                        scrollByKeyboardHeight = {
                            scrollState.animateScrollBy(it, spring(dampingRatio = 5f))
                        },
                    )
                }
                PostImageList(
                    imageUrls = registerPostUiState.images.urls,
                    onImageUrlDeleted = onImageUrlDeleted,
                )
            }
        }
        Column {
            RegisterPostCamera(
                onNavigateToGallery = {
                    onNavigateToGallery(
                        registerPostUiState.images.maxCount,
                        registerPostUiState.images.currentCount,
                    )
                },
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostImageList(
    modifier: Modifier = Modifier,
    imageUrls: List<String>,
    onImageUrlDeleted: (Int) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(start = WithpeaceTheme.padding.BasicHorizontalPadding),
    ) {
        itemsIndexed(
            items = imageUrls,
        ) { index, imageUrl ->
            Box(
                modifier = Modifier
                    .size(122.dp)
                    .animateItemPlacement(),
            ) {
                GlideImage(
                    modifier = Modifier
                        .size(110.dp)
                        .padding(top = 12.dp),
                    imageModel = { imageUrl },
                    previewPlaceholder = drawable.ic_camera,
                )
                Image(
                    modifier = Modifier
                        .clickable {
                            onImageUrlDeleted(index)
                        }
                        .align(Alignment.TopEnd),
                    painter = painterResource(id = drawable.btn_picture_delete),
                    contentDescription = "ImageDelete",
                )
            }
        }
    }
}

@Composable
fun RegisterPostTopAppBar(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
    onCompleteRegisterPost: () -> Unit,
) {
    Column {
        WithPeaceBackButtonTopAppBar(
            modifier = modifier.padding(end = 24.dp),
            onClickBackButton = onClickBackButton,
            title = {
                Text(
                    text = stringResource(string.register_post_topbar_title),
                    style = WithpeaceTheme.typography.title1,
                )
            },
            actions = {
                WithPeaceCompleteButton(
                    onClick = onCompleteRegisterPost,
                    enabled = true,
                )
            },
        )
        Divider(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPostTopic(
    modifier: Modifier = Modifier,
    topic: PostTopic?,
    onTopicChanged: (PostTopic) -> Unit,
    onShowBottomSheetChanged: (Boolean) -> Unit,
    showBottomSheet: Boolean,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    Column(
        modifier = modifier.clickable { onShowBottomSheetChanged(true) },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (topic == null) {
                   stringResource(id = string.topic_hint)
                } else {
                    stringResource(
                        id = PostTopicUiState.create(
                            topic,
                        ).textResId,
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                style = WithpeaceTheme.typography.body,
            )
            Icon(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.ic_backarrow_right),
                contentDescription = "TopicArrow",
            )
        }
        Divider(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            dragHandle = {},
            containerColor = WithpeaceTheme.colors.SystemWhite,
            sheetState = bottomSheetState,
            onDismissRequest = { onShowBottomSheetChanged(false) },
            windowInsets = WindowInsets(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
            ),
        ) {
            TopicBottomSheetContent(
                currentTopic = topic,
                onClickTopic = {
                    onTopicChanged(it)
                    onShowBottomSheetChanged(false)
                },
            )
        }
    }
}

@Composable
fun TopicBottomSheetContent(
    currentTopic: PostTopic?,
    onClickTopic: (PostTopic) -> Unit,
) {
    Column {
        Text(
            modifier = Modifier.padding(start = 24.dp, top = 24.dp),
            text = stringResource(string.topic_hint),
            style = WithpeaceTheme.typography.title1,
        )
        LazyVerticalGrid(
            contentPadding = PaddingValues(top = 40.dp, bottom = 40.dp, start = 24.dp, end = 24.dp),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(17.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            items(
                items = PostTopicUiState.entries,
            ) { topicUiState ->
                val color = if (currentTopic == topicUiState.topic) {
                    WithpeaceTheme.colors.MainPink
                } else {
                    WithpeaceTheme.colors.SystemGray2
                }
                Column(
                    modifier = Modifier.size(93.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                onClickTopic(topicUiState.topic)
                            }
                            .padding(vertical = 12.dp, horizontal = 24.dp)
                            .weight(1f),
                        painter = painterResource(topicUiState.iconResId),
                        contentDescription = topicUiState.iconResId.toString(),
                        tint = color,
                    )
                    Text(
                        text = stringResource(id = topicUiState.textResId),
                        style = WithpeaceTheme.typography.caption,
                        color = color,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPostTitle(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChanged: (String) -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier.padding(
            horizontal = WithpeaceTheme.padding.BasicHorizontalPadding,
        ),
    ) {
        BasicTextField(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            value = title,
            onValueChange = onTitleChanged,
            enabled = true,
            textStyle = WithpeaceTheme.typography.title1,
            singleLine = true,
            maxLines = 2,
            minLines = 1,
        ) {
            TextFieldDefaults.DecorationBox(
                value = title,
                innerTextField = it,
                enabled = true,
                singleLine = false,
                visualTransformation = VisualTransformation.None,
                placeholder = {
                    Text(
                        text = stringResource(string.title_hint),
                        style = WithpeaceTheme.typography.title2,
                        color = WithpeaceTheme.colors.SystemGray2,
                    )
                },
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.dp),
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                ),
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPostContent(
    modifier: Modifier = Modifier,
    content: String,
    onContentChanged: (String) -> Unit,
    scrollByKeyboardHeight: suspend (Float) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(key1 = keyboardHeight, key2 = content.lines().size) {
        if (content.lines().size >= SCROLL_THRESHOLD_LINE) {
            coroutineScope.launch { scrollByKeyboardHeight(keyboardHeight.toFloat()) }
        }
    }  // 키보드기 올라가거나, Content의 라인이 변할때마다 내용이 키보드 영역에 가려지지 않도록 키보드 영역만큼 스크롤해줌.

    val interactionSource = remember { MutableInteractionSource() }
        BasicTextField(
            modifier = modifier
                .padding(
                    vertical = 16.dp,
                    horizontal = WithpeaceTheme.padding.BasicHorizontalPadding,
                )
                .fillMaxSize(),
            value = content,
            onValueChange = onContentChanged,
            enabled = true,
            textStyle = WithpeaceTheme.typography.body,
            minLines = 10,
        ) {
            TextFieldDefaults.DecorationBox(
                value = content,
                innerTextField = it,
                enabled = true,
                singleLine = false,
                visualTransformation = VisualTransformation.None,
                placeholder = {
                    Text(
                        text = stringResource(string.content_hint),
                        style = WithpeaceTheme.typography.body,
                        color = WithpeaceTheme.colors.SystemGray2,
                    )
                },
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.dp),
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                ),
            )
        }
}

@Composable
fun RegisterPostCamera(
    modifier: Modifier = Modifier,
    onNavigateToGallery: () -> Unit,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val imagePermissionHelper = remember { ImagePermissionHelper(context) }
    val launcher = imagePermissionHelper.getImageLauncher(
        onPermissionGranted = onNavigateToGallery,
        onPermissionDenied = { showDialog = true },
    )

    if (showDialog) {
        imagePermissionHelper.ImagePermissionDialog { showDialog = false }
    }

    Column(
        modifier = Modifier.padding(
            horizontal = WithpeaceTheme.padding.BasicHorizontalPadding,
        ),
    ) {
        Divider(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .height(1.dp),
            color = WithpeaceTheme.colors.SystemGray3,
        )
        Row(
            modifier = modifier.padding(
                bottom = 16.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .clickable {
                        imagePermissionHelper.onCheckSelfImagePermission(
                            onPermissionGranted = onNavigateToGallery,
                            onPermissionDenied = {
                                imagePermissionHelper.requestPermissionDialog(launcher)
                            },
                        )
                    }
                    .padding(end = 8.dp),
                painter = painterResource(id = drawable.ic_camera),
                contentDescription = "CameraIcon",
            )
            Text(text = stringResource(string.picture), style = WithpeaceTheme.typography.caption)
        }
    }
}

private const val SCROLL_THRESHOLD_LINE = 5

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun RegisterPostScreenPreview() {
    WithpeaceTheme {
        RegisterPostScreen(
            registerPostUiState = RegisterPost("", "", PostTopic.INFORMATION, LimitedImages(listOf("", ""))),
            onCompleteRegisterPost = {},
            onImageUrlDeleted = {},
            onShowBottomSheetChanged = {},
            showBottomSheet = false,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TopicBottomSheetContentPreview() {
    WithpeaceTheme {
        TopicBottomSheetContent(currentTopic = null) {

        }
    }
}
