package com.withpeace.withpeace.feature.postlist.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.postlist.PostListRoute
import com.withpeace.withpeace.feature.postlist.PostListViewModel

const val POST_LIST_ROUTE = "post_list_route"
const val POST_LIST_DELETED_POST_ID_ARGUMENT = "post_list_deleted_post_id"

fun NavController.navigateToPostList(navOptions: NavOptions? = null) =
    navigate(POST_LIST_ROUTE, navOptions)

fun NavGraphBuilder.postListGraph(
    onShowSnackBar: (String) -> Unit,
    navigateToPostDetail: (postId: Long) -> Unit,
    onAuthExpired: () -> Unit,
) {
    composable(
        route = POST_LIST_ROUTE,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        val deletedId = it.savedStateHandle.get<Long>(POST_LIST_DELETED_POST_ID_ARGUMENT)
        val viewModel: PostListViewModel = hiltViewModel()
        deletedId?.let { id ->
            viewModel.updateDeletedId(id)
        }
        PostListRoute(
            viewModel = viewModel,
            onShowSnackBar = onShowSnackBar,
            navigateToDetail = navigateToPostDetail,
            onAuthExpired = onAuthExpired,
        )
    }
}
