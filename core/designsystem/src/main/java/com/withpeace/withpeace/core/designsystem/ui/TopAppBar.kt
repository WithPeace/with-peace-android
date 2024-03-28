package com.withpeace.withpeace.core.designsystem.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.R
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithPeaceBackButtonTopAppBar(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit) = {},
    windowInsets: WindowInsets = WindowInsets(0,0,0,0)
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .padding(start = 20.dp, bottom = 16.dp, top = 16.dp, end = 28.dp)
                    .clickable {
                        onClickBackButton()
                    }
                    .padding(4.dp),
                painter = painterResource(id = R.drawable.ic_backarrow_left),
                contentDescription = "BackArrowLeft",
            )
        },
        windowInsets = windowInsets,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = WithpeaceTheme.colors.SystemWhite),
    )
}

@Preview
@Composable
private fun BackButtonTopAppBarPreview() {
    WithpeaceTheme {
        WithPeaceBackButtonTopAppBar(
            onClickBackButton = {},
            title = {
                Text(text = "글 쓰기")
            },
        )
    }
}

