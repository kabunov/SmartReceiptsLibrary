package co.smartreceipts.android.paywall

import co.smartreceipts.android.purchases.PurchaseEventsListener
import co.smartreceipts.android.purchases.PurchaseManager
import co.smartreceipts.android.purchases.model.InAppPurchase
import co.smartreceipts.android.purchases.source.PurchaseSource
import co.smartreceipts.android.utils.rx.RxSchedulers
import co.smartreceipts.core.di.scopes.FragmentScope
import co.smartreceipts.core.identity.IdentityManager
import com.android.billingclient.api.ProductDetails
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

@FragmentScope
class PaywallInteractor @Inject constructor(
    private val identityManager: IdentityManager,
    private val purchaseManager: PurchaseManager,
    @Named(RxSchedulers.MAIN)
    private val observeOnScheduler: Scheduler
) {
    fun addSubscriptionListener(listener: PurchaseEventsListener) {
        purchaseManager.addEventListener(listener)
    }

    fun removeSubscriptionListener(listener: PurchaseEventsListener) {
        purchaseManager.removeEventListener(listener)
    }

    //TODO check do we need to ask for login before purchase
    val isUserLoggedIn: Boolean
        get() = identityManager.isLoggedIn

    fun getTrialPurchase(): Single<ProductDetails> {
        return purchaseManager.allAvailablePurchaseSkus
            .map { set ->
                set.first { skuDetails ->
                    val inAppPurchase = InAppPurchase.from(skuDetails.productId)
                    inAppPurchase == InAppPurchase.StandardSubscriptionTrialPlan
                }
            }
            .observeOn(observeOnScheduler)
    }

    fun startPurchase() {
        purchaseManager.initiatePurchase(InAppPurchase.StandardSubscriptionTrialPlan, PurchaseSource.Paywall)
    }
}
