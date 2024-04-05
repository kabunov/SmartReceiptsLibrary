package co.smartreceipts.android.trial

import co.smartreceipts.android.trial.data.TrialStorage
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TrialManager @Inject constructor(
    private val trialStorage: TrialStorage,
) {
    fun isTrialCanBeShown(): Boolean {
        val getLastShownDate = trialStorage.getLastShownDate()
        if (getLastShownDate < 0) {
            return true
        }

        val daysToMillis = TimeUnit.DAYS.toMillis(1)
        val diffInDays = (System.currentTimeMillis() - getLastShownDate) / daysToMillis
        return diffInDays >= DAYS_UNTIL_PROMPT
    }

    fun setTrialShown() {
        trialStorage.setLastShownDate()
    }

    private companion object {
        const val DAYS_UNTIL_PROMPT = 7
    }
}