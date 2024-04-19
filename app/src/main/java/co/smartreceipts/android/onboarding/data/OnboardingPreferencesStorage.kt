package co.smartreceipts.android.onboarding.data

import android.content.SharedPreferences
import co.smartreceipts.core.di.scopes.ApplicationScope
import com.google.common.base.Preconditions
import dagger.Lazy
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

@ApplicationScope
class OnboardingPreferencesStorage @Inject constructor(@Named(ONBOARDING_PREFERENCES) sharedPreferences: Lazy<SharedPreferences>) :
    OnboardingStorage {

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

        const val ONBOARDING_PREFERENCES = "Smart Receipts onboarding"

        private const val LAST_SHOWN_DATE = "LAST_SHOWN_DATE"
    }
}
