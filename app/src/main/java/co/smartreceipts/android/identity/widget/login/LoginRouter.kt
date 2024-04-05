package co.smartreceipts.android.identity.widget.login

import co.smartreceipts.android.activities.NavigationHandler
import co.smartreceipts.android.activities.SmartReceiptsActivity
import co.smartreceipts.core.di.scopes.FragmentScope
import javax.inject.Inject

@FragmentScope
class LoginRouter @Inject constructor(private val navigationHandler: NavigationHandler<SmartReceiptsActivity>) {

    fun navigateBack(): Boolean {
        return navigationHandler.navigateBack()
    }

    fun navigateToOcrConfigurationFragment() {
        navigationHandler.navigateToOcrConfigurationFragment()
    }

    fun navigateToSubscriptionsActivity() {
        navigationHandler.navigateToSubscriptionsActivity()
    }

    fun navigateToTrialScreen() {
        navigationHandler.navigateToTrialScreen()
    }
}
