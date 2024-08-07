package com.withpeace.withpeace.feature.mypage.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.mypage.MyPageRoute
import com.withpeace.withpeace.feature.mypage.MyPageViewModel

const val MY_PAGE_ROUTE = "myPageRoute"
const val MY_PAGE_CHANGED_NICKNAME_ARGUMENT = "myPageChangedNicknameArgument"
const val MY_PAGE_CHANGED_IMAGE_ARGUMENT = "myPageChangedImageArgument"

fun NavController.navigateMyPage(navOptions: NavOptions? = null) {
    navigate(MY_PAGE_ROUTE, navOptions)
}

fun NavGraphBuilder.myPageNavGraph(
    onShowSnackBar: (message: String) -> Unit,
    onEditProfile: (nickname: String, profileImageUrl: String) -> Unit,
    onLogoutSuccess: () -> Unit,
    onWithdrawSuccess: () -> Unit,
    onAuthExpired: () -> Unit,
    onDibsOfPolicyClick: () -> Unit,
) {
    composable(
        route = MY_PAGE_ROUTE,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        val nickname = it.savedStateHandle.get<String>(MY_PAGE_CHANGED_NICKNAME_ARGUMENT)
        val profile = it.savedStateHandle.get<String>(MY_PAGE_CHANGED_IMAGE_ARGUMENT)
        val viewModel: MyPageViewModel = hiltViewModel()
        viewModel.updateProfile(nickname, profile)
        MyPageRoute(
            onShowSnackBar = onShowSnackBar,
            onEditProfile = onEditProfile,
            onLogoutSuccess = onLogoutSuccess,
            onWithdrawSuccess = onWithdrawSuccess,
            viewModel = viewModel,
            onAuthExpired = onAuthExpired,
            onDibsOfPolicyClick = onDibsOfPolicyClick,
        )
    }
}