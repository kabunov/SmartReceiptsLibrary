package co.smartreceipts.android.onboarding.data

interface OnboardingStorage {

    fun getLastShownDate(): Long

    fun setLastShownDate()
}
