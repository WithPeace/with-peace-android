package com.withpeace.withpeace.feature.policyconsent.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.withpeace.withpeace.feature.policyconsent.PolicyConsentRoute

const val POLICY_CONSENT_ROUTE = "policy_consent_route"

fun NavController.navigateToPolicyConsent(navOptions: NavOptions? = null) =
    navigate(POLICY_CONSENT_ROUTE, navOptions)

fun NavGraphBuilder.policyConsentGraph(
    onShowSnackBar: (String) -> Unit,
) {
    composable(POLICY_CONSENT_ROUTE) {

        PolicyConsentRoute(
            onShowSnackBar = onShowSnackBar,
        )
    }
}
