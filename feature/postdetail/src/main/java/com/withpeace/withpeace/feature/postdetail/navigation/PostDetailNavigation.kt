package com.withpeace.withpeace.feature.postdetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.withpeace.withpeace.feature.postdetail.PostDetailRoute

const val POST_DETAIL_ROUTE = "post_detail_route"
const val POST_DETAIL_ID_ARGUMENT = "post_id_argument"
const val POST_DETAIL_ROUTE_WITH_ARGUMENT =
    "$POST_DETAIL_ROUTE/{$POST_DETAIL_ID_ARGUMENT}/"

fun NavController.navigateToPostDetail(
    postId: Int,
    navOptions: NavOptions? = null,
) = navigate(route = "$POST_DETAIL_ROUTE/$postId", navOptions)

fun NavGraphBuilder.postDetailNavGraph(
    onShowSnackBar: (String) -> Unit,
) {
    composable(
        route = POST_DETAIL_ROUTE_WITH_ARGUMENT,
        arguments = listOf(
            navArgument(POST_DETAIL_ID_ARGUMENT) { type = NavType.LongType },
        ),
    ) {
        PostDetailRoute(onShowSnackBar = onShowSnackBar)
    }
}
