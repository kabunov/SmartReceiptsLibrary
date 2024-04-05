package co.smartreceipts.android.trial

import io.reactivex.Observable
import io.reactivex.functions.Consumer

interface TrialView {

    fun presentPurchasePrice(): Consumer<in String?>

    val submitButtonClicks: Observable<Unit>

    fun navigateToLogin()

    fun presentLoading()

    fun presentSuccessSubscription()

    fun presentFailedSubscription()
}
