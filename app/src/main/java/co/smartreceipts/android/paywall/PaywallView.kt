package co.smartreceipts.android.paywall

import io.reactivex.Observable
import io.reactivex.functions.Consumer

interface PaywallView {

    fun presentPurchasePrice(): Consumer<in String?>

    val submitButtonClicks: Observable<Unit>

    fun presentLoading()

    fun presentSuccessSubscription()

    fun presentFailedSubscription()
}
