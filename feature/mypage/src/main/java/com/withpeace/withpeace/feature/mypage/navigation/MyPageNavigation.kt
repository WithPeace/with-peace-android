package com.withpeace.withpeace.feature.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.mypage.MyPageRoute

const val MY_PAGE_ROUTE = "myPageRoute"

fun NavController.navigateMyPage(navOptions: NavOptions? = null) {
    navigate(MY_PAGE_ROUTE, navOptions)
}

fun NavGraphBuilder.myPageNavGraph(
    onShowSnackBar: (message: String) -> Unit,
    onEditProfile: () -> Unit,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
) {
    composable(route = MY_PAGE_ROUTE) {
        MyPageRoute(
            onShowSnackBar = onShowSnackBar,
            onEditProfile = onEditProfile,
            onLogoutClick = onLogoutClick,
            onWithdrawClick = onWithdrawClick,
        )
    }
}