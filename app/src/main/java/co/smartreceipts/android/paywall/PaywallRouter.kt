package co.smartreceipts.android.paywall

import co.smartreceipts.android.activities.LoginSourceDestination
import co.smartreceipts.android.activities.NavigationHandler
import co.smartreceipts.android.activities.SmartReceiptsActivity
import co.smartreceipts.core.di.scopes.FragmentScope
import javax.inject.Inject

@FragmentScope
class PaywallRouter @Inject constructor(private val navigationHandler: NavigationHandler<SmartReceiptsActivity>) {

    fun navigateBack(): Boolean {
        return this.navigationHandler.navigateBack()
    }

    fun navigateToLoginScreen() {
        navigationHandler.navigateToLoginScreen(LoginSourceDestination.PAYWALL)
    }
}