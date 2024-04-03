package co.smartreceipts.android.paywall

import co.smartreceipts.android.paywall.data.PaywallStorage
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PaywallManager @Inject constructor(
    private val paywallStorage: PaywallStorage,
) {
    fun isPaywallCanBeShown(): Boolean {
        val getLastShownDate = paywallStorage.getLastShownDate()
        if (getLastShownDate < 0) {
            return true
        }

        val daysToMillis = TimeUnit.DAYS.toMillis(1)
        val diffInDays = (System.currentTimeMillis() - getLastShownDate) / daysToMillis
        return diffInDays >= DAYS_UNTIL_PROMPT
    }

    fun setPaywallShown() {
        paywallStorage.setLastShownDate()
    }

    private companion object {
        const val DAYS_UNTIL_PROMPT = 7
    }
}