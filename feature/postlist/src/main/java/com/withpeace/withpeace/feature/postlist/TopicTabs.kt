package com.withpeace.withpeace.feature.postlist

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.ui.PostTopicUiState

@Composable
fun TopicTabs(
    currentTopic: PostTopic,
    tabPosition: Int,
    onClick: (PostTopic) -> Unit,
) {
    TabRow(
        modifier = Modifier.wrapContentSize(),
        selectedTabIndex = tabPosition,
        containerColor = WithpeaceTheme.colors.SystemWhite,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[tabPosition])
                    .padding(horizontal = 16.dp),
                color = WithpeaceTheme.colors.MainPink,
            )
        },
    ) {
        PostTopicUiState.entries.forEachIndexed { index, postTopicUiState ->
            val color = if (currentTopic == postTopicUiState.topic) WithpeaceTheme.colors.MainPink
            else WithpeaceTheme.colors.SystemGray2
            Tab(
                selected = postTopicUiState.topic == currentTopic,
                onClick = { onClick(postTopicUiState.topic) },
                text = {
                    Text(
                        text = stringResource(id = postTopicUiState.textResId),
                        color = color,
                    )
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = postTopicUiState.iconResId),
                        contentDescription = stringResource(id = postTopicUiState.textResId),
                        tint = color,
                    )
                },
            )
        }
    }
}

