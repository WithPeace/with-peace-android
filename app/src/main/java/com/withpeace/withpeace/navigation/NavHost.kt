package com.withpeace.withpeace.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.withpeace.withpeace.feature.gallery.navigation.galleryNavGraph
import com.withpeace.withpeace.feature.gallery.navigation.navigateToGallery
import com.withpeace.withpeace.feature.home.navigation.homeNavGraph
import com.withpeace.withpeace.feature.login.navigation.LOGIN_ROUTE
import com.withpeace.withpeace.feature.login.navigation.loginNavGraph
import com.withpeace.withpeace.feature.mypage.navigation.myPageNavGraph
import com.withpeace.withpeace.feature.postdetail.navigation.postDetailGraph
import com.withpeace.withpeace.feature.postlist.navigation.postListGraph
import com.withpeace.withpeace.feature.registerpost.navigation.IMAGE_LIST_ARGUMENT
import com.withpeace.withpeace.feature.registerpost.navigation.registerPostNavGraph

@Composable
fun WithpeaceNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = LOGIN_ROUTE,
    onShowSnackBar: (message: String) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        loginNavGraph(onShowSnackBar = onShowSnackBar)
        registerPostNavGraph(
            onShowSnackBar = onShowSnackBar,
            onCompleteRegisterPost = {},
            onClickBackButton = navController::popBackStack,
            onNavigateToGallery = { imageLimit, imageCount ->
                navController.navigateToGallery(
                    imageLimit = imageLimit,
                    currentImageCount = imageCount,
                )
            },
        )
        galleryNavGraph(
            onClickBackButton = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IMAGE_LIST_ARGUMENT, emptyList<String>(),
                )
                navController.popBackStack()
            },
            onCompleteRegisterImages = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    IMAGE_LIST_ARGUMENT, it,
                )
                navController.popBackStack()
            },
            onShowSnackBar = onShowSnackBar,
        )
        homeNavGraph(onShowSnackBar)
        postListGraph(onShowSnackBar)
        postDetailGraph(
            onShowSnackBar = onShowSnackBar,
            onClickBackButton = navController::popBackStack,
        )
        myPageNavGraph(onShowSnackBar)
    }
}
