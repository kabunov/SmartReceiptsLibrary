package co.smartreceipts.android.trial.data

import android.content.SharedPreferences
import co.smartreceipts.core.di.scopes.ApplicationScope
import com.google.common.base.Preconditions
import dagger.Lazy
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

@ApplicationScope
class TrialPreferencesStorage @Inject constructor(@Named(TRIAL_PREFERENCES) sharedPreferences: Lazy<SharedPreferences>) :
    TrialStorage {

    private val sharedPreferences: Lazy<SharedPreferences>

    init {
        this.sharedPreferences = Preconditions.checkNotNull(sharedPreferences)
    }

    override fun getLastShownDate(): Long {
        val preferences = sharedPreferences.get()
        return preferences.getLong(LAST_SHOWN_DATE, Long.MIN_VALUE)
    }

    override fun setLastShownDate() {
        Completable.fromAction {
            sharedPreferences.get().edit().apply {
                putLong(LAST_SHOWN_DATE, System.currentTimeMillis())
                apply()
            }
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    companion object {

        const val TRIAL_PREFERENCES = "Smart Receipts trial"

        private const val LAST_SHOWN_DATE = "LAST_SHOWN_DATE"
    }
}
