package co.smartreceipts.android.trial.data

interface TrialStorage {

    fun getLastShownDate(): Long

    fun setLastShownDate()
}
