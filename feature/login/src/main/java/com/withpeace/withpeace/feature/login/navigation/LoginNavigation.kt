package com.withpeace.withpeace.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.login.LoginRoute

const val LOGIN_ROUTE = "loginRoute"

fun NavController.navigateLogin(navOptions: NavOptions? = null) {
    navigate(LOGIN_ROUTE, navOptions)
}

fun NavGraphBuilder.loginNavGraph(
    onShowSnackBar: (message: String) -> Unit,
    onSignUpNeeded: () -> Unit,
    onLoginSuccess: () -> Unit,
) {
    composable(route = LOGIN_ROUTE) {
        LoginRoute(
            onShowSnackBar = onShowSnackBar,
            onSignUpNeeded = onSignUpNeeded,
            onLoginSuccess = onLoginSuccess,
        )
    }
}