package co.smartreceipts.android.subscriptions

import co.smartreceipts.analytics.Analytics
import co.smartreceipts.analytics.events.DataPoint
import co.smartreceipts.analytics.events.DefaultDataPointEvent
import co.smartreceipts.analytics.events.Events
import co.smartreceipts.analytics.log.Logger
import co.smartreceipts.android.purchases.PurchaseEventsListener
import co.smartreceipts.android.purchases.model.InAppPurchase
import co.smartreceipts.android.purchases.source.PurchaseSource
import co.smartreceipts.android.purchases.subscriptionFormattedPrice
import co.smartreceipts.android.widget.viper.BaseViperPresenter
import co.smartreceipts.core.di.scopes.ActivityScope
import co.smartreceipts.core.identity.IdentityManager
import javax.inject.Inject

@ActivityScope
class SubscriptionsPresenter @Inject constructor(
    view: SubscriptionsView,
    interactor: SubscriptionsInteractor,
    private val identityManager: IdentityManager,
    private val analytics: Analytics,
) : BaseViperPresenter<SubscriptionsView, SubscriptionsInteractor>(view, interactor),
    PurchaseEventsListener {

    override fun subscribe() {

        interactor.addSubscriptionListener(this)

        compositeDisposable.add(
            view.cancelSubscriptionInfoClicks
                .subscribe { view.redirectToPlayStoreSubscriptions() }
        )

        compositeDisposable.add(
            view.standardSubscriptionClicks
                .subscribe {
                    analytics.record(
                        DefaultDataPointEvent(Events.Subscriptions.SubscriptionTapped).addDataPoint(
                            DataPoint("productId", InAppPurchase.StandardSubscriptionPlan.sku)
                        )
                    )

                    when {
                        identityManager.isLoggedIn -> {
                            view.presentLoading()
                            interactor.purchaseStandardPlan()
                        }

                        else -> view.navigateToLogin()
                    }
                }
        )

        compositeDisposable.add(
            view.premiumSubscriptionClicks
                .subscribe {
                    analytics.record(
                        DefaultDataPointEvent(Events.Subscriptions.SubscriptionTapped).addDataPoint(
                            DataPoint("productId", InAppPurchase.PremiumSubscriptionPlan.sku)
                        )
                    )

                    when {
                        identityManager.isLoggedIn -> {
                            view.presentLoading()
                            interactor.purchasePremiumPlan()
                        }

                        else -> view.navigateToLogin()
                    }
                }
        )

        compositeDisposable.add(
            interactor.getPlansWithOwnership()
                .subscribe({ plans ->
                    var userOwnsPlan = false
                    for (plan in plans) {
                        if (plan.key.productId == InAppPurchase.StandardSubscriptionPlan.sku) {
                            val isOwned = plan.value
                            view.presentStandardPlan(if (isOwned) null else plan.key.subscriptionFormattedPrice?.formatPrice())
                            userOwnsPlan = userOwnsPlan || isOwned
                        } else if (plan.key.productId == InAppPurchase.PremiumSubscriptionPlan.sku) {
                            val isOwned = plan.value
                            view.presentPremiumPlan(if (isOwned) null else plan.key.subscriptionFormattedPrice?.formatPrice())
                            userOwnsPlan = userOwnsPlan || isOwned
                        }
                    }

                    view.presentCancelInfo(userOwnsPlan)
                }, { t -> Logger.error(this, t) })
        )
    }

    override fun unsubscribe() {
        interactor.removeSubscriptionListener(this)
        super.unsubscribe()
    }

    override fun onPurchaseSuccess(inAppPurchase: InAppPurchase, purchaseSource: PurchaseSource) {
        analytics.record(
            DefaultDataPointEvent(Events.Subscriptions.SubscriptionPurchaseSuccess).addDataPoint(
                DataPoint("productId", inAppPurchase.sku)
            )
        )
        view.presentSuccessSubscription()
    }

    override fun onPurchaseFailed(purchaseSource: PurchaseSource) {
        analytics.record(Events.Subscriptions.SubscriptionPurchaseFailed)
        view.presentFailedSubscription()
    }

    override fun onPurchasePending() {
        /* no-op */
    }

    private fun String.formatPrice(): String {
        return this.replace(".00", "").replace(",00", "")
    }
}