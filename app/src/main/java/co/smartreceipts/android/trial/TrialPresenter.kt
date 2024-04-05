package co.smartreceipts.android.trial

import co.smartreceipts.analytics.log.Logger
import co.smartreceipts.android.purchases.PurchaseEventsListener
import co.smartreceipts.android.purchases.model.InAppPurchase
import co.smartreceipts.android.purchases.source.PurchaseSource
import co.smartreceipts.android.purchases.subscriptionFormattedPrice
import co.smartreceipts.android.widget.viper.BaseViperPresenter
import co.smartreceipts.core.di.scopes.FragmentScope
import co.smartreceipts.core.identity.IdentityManager
import javax.inject.Inject

@FragmentScope
class TrialPresenter @Inject constructor(
    view: TrialView,
    interactor: TrialInteractor,
    private val identityManager: IdentityManager,
) :
    BaseViperPresenter<TrialView, TrialInteractor>(view, interactor),
    PurchaseEventsListener {

    override fun subscribe() {
        interactor.addSubscriptionListener(this)

        compositeDisposable.add(interactor.getTrialPurchase().map { it.subscriptionFormattedPrice }
            .doOnSuccess { Logger.debug(this, "[trial] Trial price $it") }
            .subscribe(view.presentPurchasePrice()) { throwable ->
                Logger.warn(
                    this,
                    "[trial] Failed to get trial purchase",
                    throwable
                )
            }
        )

        compositeDisposable.add(
            view.submitButtonClicks
                .subscribe {
                    Logger.debug(this, "[trial] Submit clicked, isLoggedIn = ${identityManager.isLoggedIn}")
                    when {
                        identityManager.isLoggedIn -> {
                            view.presentLoading()
                            interactor.startPurchase()
                        }

                        else -> view.navigateToLogin()
                    }
                }
        )
    }

    override fun unsubscribe() {
        interactor.removeSubscriptionListener(this)
        super.unsubscribe()
    }

    override fun onPurchaseSuccess(inAppPurchase: InAppPurchase, purchaseSource: PurchaseSource) {
        view.presentSuccessSubscription()
    }

    override fun onPurchaseFailed(purchaseSource: PurchaseSource) {
        view.presentFailedSubscription()
    }

    override fun onPurchasePending() {
        /* no-op */
    }
}
