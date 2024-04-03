package co.smartreceipts.android.paywall.data

interface PaywallStorage {

    fun getLastShownDate(): Long

    fun setLastShownDate()
}
