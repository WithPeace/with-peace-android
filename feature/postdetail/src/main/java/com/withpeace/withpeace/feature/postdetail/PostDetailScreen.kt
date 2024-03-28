package com.withpeace.withpeace.feature.postdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithPeaceBackButtonTopAppBar
import com.withpeace.withpeace.core.domain.model.date.Date
import com.withpeace.withpeace.core.domain.model.post.PostContent
import com.withpeace.withpeace.core.domain.model.post.PostDetail
import com.withpeace.withpeace.core.domain.model.post.PostTitle
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.model.post.PostUser
import com.withpeace.withpeace.core.ui.PostTopicUiState
import com.withpeace.withpeace.core.ui.R
import com.withpeace.withpeace.core.ui.toRelativeString
import java.time.LocalDateTime

@Composable
fun PostDetailRoute(
    viewModel: PostDetailViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
    onClickBackButton: () -> Unit,
) {
    val postUiState = viewModel.postUiState.collectAsStateWithLifecycle().value
    PostDetailScreen(
        postUiState = postUiState,
        onClickBackButton = onClickBackButton,
    )
}

@Composable
fun PostDetailScreen(
    onClickBackButton: () -> Unit = {},
    postUiState: PostDetailUiState,
) {
    var showBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    val lazyListState = rememberLazyListState()
    Column {
        WithPeaceBackButtonTopAppBar(
            onClickBackButton = onClickBackButton,
            title = {},
            actions = {
                Icon(
                    modifier = Modifier
                        .clickable {
                            showBottomSheet = true
                        }
                        .padding(21.dp)
                        .size(24.dp),
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "option",
                )
            },
        )
        when (postUiState) {
        PostDetailUiState.Fail -> Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "네트워크 상태를 확인해주세요",
                modifier = Modifier.align(Alignment.Center),
            )
        }

        PostDetailUiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = WithpeaceTheme.colors.MainPink,
            )
        }

        is PostDetailUiState.Success -> {
            LazyColumn(state = lazyListState) {
                item {
                    HorizontalDivider(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
                        color = WithpeaceTheme.colors.SystemGray3,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PostTopic(postTopic = postUiState.postDetail.postTopic)
                    Spacer(modifier = Modifier.height(16.dp))
                    UserProfile(
                        modifier = Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
                        user = postUiState.postDetail.user,
                        createDate = postUiState.postDetail.createDate,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PostTitle(
                        modifier = Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
                        title = postUiState.postDetail.title.value,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PostContent(
                        modifier = Modifier.padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
                        content = postUiState.postDetail.content.value,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                PostImages(
                    imageUrls = postUiState.postDetail.imageUrls,
                )
            }
        }
        }
    }
}

fun LazyListScope.PostImages(
    imageUrls: List<String>,
) {
    items(
        items = imageUrls,
    ) { imageUrl ->
        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding),
            imageModel = { imageUrl },
            previewPlaceholder = R.drawable.ic_freedom,
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun PostTitle(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        modifier = modifier,
        text = title,
        style = WithpeaceTheme.typography.title2,
    )
}

@Composable
fun PostContent(
    modifier: Modifier = Modifier,
    content: String,
) {
    Text(
        modifier = modifier,
        text = content,
        style = WithpeaceTheme.typography.body,
    )
}

@Composable
fun PostTopic(
    modifier: Modifier = Modifier,
    postTopic: PostTopic,
) {
    Box(
        modifier = modifier
            .padding(horizontal = WithpeaceTheme.padding.BasicHorizontalPadding)
            .background(
                color = WithpeaceTheme.colors.MainPink,
                shape = RoundedCornerShape(20.dp),
            ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val topicUiState = PostTopicUiState.create(postTopic)
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(id = topicUiState.iconResId),
                contentDescription = topicUiState.name,
                tint = WithpeaceTheme.colors.SystemWhite,
            )
            Spacer(modifier = Modifier.width(11.dp))
            Text(
                text = stringResource(id = topicUiState.textResId),
                style = WithpeaceTheme.typography.body,
                color = WithpeaceTheme.colors.SystemWhite,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun UserProfile(
    modifier: Modifier = Modifier,
    createDate: Date,
    user: PostUser,
) {
    val context = LocalContext.current
    Row(modifier = modifier) {
        GlideImage(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            imageModel = { user.profileImageUrl },
            previewPlaceholder = R.drawable.ic_freedom,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = user.name,
                style = WithpeaceTheme.typography.body,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = createDate.toRelativeString(context),
                style = WithpeaceTheme.typography.caption,
                color = WithpeaceTheme.colors.SystemGray4,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 900, backgroundColor = 0xFFffffff)
@Composable
private fun PostDetailScreenPreview() {
    WithpeaceTheme {
        PostDetailScreen(
            postUiState = PostDetailUiState.Success(
                PostDetail(
                    user = PostUser(
                        id = 4323,
                        name = "Angeline Shaw",
                        profileImageUrl = "https://www.google.com/#q=ignota",
                    ),
                    id = 1529,
                    title = PostTitle(value = "일찌기 나는 아무것도 아니었다."),
                    content = PostContent(value = "돌아가는 팽이를 보고 싶어서, 그 팽이가 온전히 내 팽이이고 싶어서, 내 속도를 그대로 빼닮은 팽이의 회전을 여유롭게 관찰하고 싶어서, 그러니까 문방구에서 막상 팽이를 사오긴 했는데 요즘 누가 팽이 돌리나 눈치 보다 땅에다가는 못 풀고 눈으로 푸는 마음, 그 눈에서 돌아가는 팽이의 마음, 그거 시 같다."),
                    postTopic = PostTopic.FREEDOM,
                    imageUrls = listOf("", "", ""),
                    createDate = Date(date = LocalDateTime.now()),
                ),
            ),
        )
    }
}
