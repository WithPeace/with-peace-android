package com.withpeace.withpeace.feature.postlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.landscapist.glide.GlideImage
import com.withpeace.withpeace.core.designsystem.theme.PretendardFont
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.designsystem.ui.WithpeaceCard
import com.withpeace.withpeace.core.domain.model.date.Date
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.ui.R
import com.withpeace.withpeace.core.ui.toRelativeString
import java.time.LocalDateTime

@Composable
fun PostListRoute(
    viewModel: PostListViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
) {
    val postList = viewModel.postList.collectAsStateWithLifecycle().value
    val currentTopic = viewModel.currentTopic.collectAsStateWithLifecycle().value
    PostListScreen(
        currentTopic = currentTopic,
        postList,
        onTopicChanged = viewModel::onTopicChanged,
    )
}

@Composable
fun PostListScreen(
    currentTopic: PostTopic,
    postList: List<Post>,
    onTopicChanged: (PostTopic) -> Unit = {},
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(8.dp))
        TopicTabs(
            currentTopic = currentTopic,
            onClick = onTopicChanged,
            tabPosition = PostTopic.findIndex(currentTopic),
        )
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = postList,
                key = {it.postId}
            ) { post ->
                WithpeaceCard(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ConstraintLayout(
                        modifier = Modifier
                            .padding(vertical = 15.dp, horizontal = 16.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    ) {
                        val (column, image) = createRefs()
                        Column(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .constrainAs(column) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(image.start)
                                    width = Dimension.fillToConstraints
                                },
                        ) {
                            Text(
                                text = post.title,
                                fontFamily = PretendardFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                            )
                            Text(
                                modifier = Modifier.padding(vertical = 8.dp),
                                text = post.content,
                                style = WithpeaceTheme.typography.caption,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = post.createDate.toRelativeString(context),
                                fontFamily = PretendardFont,
                                fontWeight = FontWeight.Normal,
                                color = WithpeaceTheme.colors.SystemGray2,
                                fontSize = 12.sp
                            )
                        }
                        GlideImage(
                            modifier = Modifier
                                .constrainAs(image) {
                                    top.linkTo(column.top)
                                    bottom.linkTo(column.bottom)
                                    end.linkTo(parent.end)
                                    height = Dimension.fillToConstraints
                                    width = Dimension.ratio("1:1")
                                },
                            imageModel = { post.postImageUrl },
                            previewPlaceholder = R.drawable.ic_freedom,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PostListScreenPreview() {
    WithpeaceTheme {
        PostListScreen(
            currentTopic = PostTopic.FREEDOM,
            postList = List(10) {
                Post(
                    postId = 2049,
                    title = "periculis",
                    content = "pellentesq\nuehaha",
                    postTopic = PostTopic.INFORMATION,
                    createDate = Date(LocalDateTime.now()),
                    postImageUrl = "https://duckduckgo.com/?q=verterem",
                )
            },
        )
    }
}
