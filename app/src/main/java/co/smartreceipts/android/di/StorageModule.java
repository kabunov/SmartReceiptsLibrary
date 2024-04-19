package co.smartreceipts.android.di;


import co.smartreceipts.android.onboarding.data.OnboardingStorage;
import co.smartreceipts.android.rating.data.AppRatingPreferencesStorage;
import co.smartreceipts.android.rating.data.AppRatingStorage;
import co.smartreceipts.android.onboarding.data.OnboardingPreferencesStorage;
import co.smartreceipts.android.trial.data.TrialPreferencesStorage;
import co.smartreceipts.android.trial.data.TrialStorage;
import co.smartreceipts.android.widget.tooltip.report.backup.data.BackupReminderPreferencesStorage;
import co.smartreceipts.android.widget.tooltip.report.backup.data.BackupReminderTooltipStorage;
import co.smartreceipts.android.widget.tooltip.report.generate.data.GenerateInfoTooltipPreferencesStorage;
import co.smartreceipts.android.widget.tooltip.report.generate.data.GenerateInfoTooltipStorage;
import co.smartreceipts.core.di.scopes.ApplicationScope;
import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

    @Provides
    @ApplicationScope
    public static GenerateInfoTooltipStorage provideGenerateInfoTooltipStorage(GenerateInfoTooltipPreferencesStorage storage) {
        return storage;
    }

    @Provides
    @ApplicationScope
    public static BackupReminderTooltipStorage provideBackupReminderTooltipStorage(BackupReminderPreferencesStorage storage) {
        return storage;
    }

    @Provides
    @ApplicationScope
    public static AppRatingStorage provideAppRatingStorage(AppRatingPreferencesStorage storage) {
        return storage;
    }

    @Provides
    @ApplicationScope
    public static TrialStorage provideTrialStorage(TrialPreferencesStorage storage) {
        return storage;
    }

    @Provides
    @ApplicationScope
    public static OnboardingStorage provideOnboardingStorage(OnboardingPreferencesStorage storage) {
        return storage;
    }

}
