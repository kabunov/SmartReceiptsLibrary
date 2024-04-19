package co.smartreceipts.android.onboarding

import co.smartreceipts.android.onboarding.data.OnboardingStorage
import javax.inject.Inject

class OnboardingManager @Inject constructor(
    private val onboardingStorage: OnboardingStorage,
) {
    fun isOnboardingCanBeShown(): Boolean {
        val getLastShownDate = onboardingStorage.getLastShownDate()
        return getLastShownDate < 0
    }

    fun setOnboardingShown() {
        onboardingStorage.setLastShownDate()
    }
}