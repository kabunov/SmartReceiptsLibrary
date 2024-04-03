package co.smartreceipts.android.paywall.di

import co.smartreceipts.android.paywall.PaywallFragment
import co.smartreceipts.android.paywall.PaywallView
import dagger.Binds
import dagger.Module

@Module
abstract class PaywallModule {

    @Binds
    internal abstract fun providePaywallView(fragment: PaywallFragment): PaywallView
}
