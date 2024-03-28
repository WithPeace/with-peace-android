package com.withpeace.withpeace.feature.postlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.postlist.PostListRoute

const val POST_LIST_ROUTE = "post_list_rotue"

fun NavController.navigateToPostList(navOptions: NavOptions? = null) =
    navigate(POST_LIST_ROUTE, navOptions)

fun NavGraphBuilder.postListGraph(
    onShowSnackBar: (String) -> Unit,
    navigateToPostDetail: (postId: Long) -> Unit,
) {
    composable(POST_LIST_ROUTE) {
        PostListRoute(
            onShowSnackBar = onShowSnackBar,
            navigateToDetail = navigateToPostDetail,
        )
    }
}
