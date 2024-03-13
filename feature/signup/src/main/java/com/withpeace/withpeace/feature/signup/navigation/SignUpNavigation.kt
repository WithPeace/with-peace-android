package com.withpeace.withpeace.feature.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.signup.SignUpRoute

const val SIGN_UP_ROUTE = "signUpRoute"

fun NavController.navigateSignUp(navOptions: NavOptions? = null) {
    navigate(SIGN_UP_ROUTE, navOptions)
}

fun NavGraphBuilder.signUpNavGraph(
    onShowSnackBar: (message: String) -> Unit,
) {
    composable(route = SIGN_UP_ROUTE) {
        SignUpRoute(onShowSnackBar = onShowSnackBar)
    }
}