package co.smartreceipts.android.trial.di

import co.smartreceipts.android.trial.TrialFragment
import co.smartreceipts.android.trial.TrialView
import dagger.Binds
import dagger.Module

@Module
abstract class TrialModule {

    @Binds
    internal abstract fun provideTrialView(fragment: TrialFragment): TrialView
}
