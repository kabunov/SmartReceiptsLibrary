package co.smartreceipts.android.trial

import co.smartreceipts.android.activities.LoginSourceDestination
import co.smartreceipts.android.activities.NavigationHandler
import co.smartreceipts.android.activities.SmartReceiptsActivity
import co.smartreceipts.core.di.scopes.FragmentScope
import javax.inject.Inject

@FragmentScope
class TrialRouter @Inject constructor(private val navigationHandler: NavigationHandler<SmartReceiptsActivity>) {

    fun navigateBack(): Boolean {
        return navigationHandler.navigateBack()
    }

    fun navigateToLoginScreen() {
        navigationHandler.navigateToLoginScreen(LoginSourceDestination.TRIAL)
    }
}