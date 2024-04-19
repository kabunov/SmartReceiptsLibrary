package co.smartreceipts.android.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Named;

import co.smartreceipts.android.onboarding.data.OnboardingPreferencesStorage;
import co.smartreceipts.android.persistence.database.tables.ordering.OrderingPreferencesManager;
import co.smartreceipts.android.rating.data.AppRatingPreferencesStorage;
import co.smartreceipts.android.settings.UserPreferenceManager;
import co.smartreceipts.android.trial.data.TrialPreferencesStorage;
import co.smartreceipts.core.di.scopes.ApplicationScope;
import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferencesModule {

    @Provides
    @ApplicationScope
    public static SharedPreferences providesDefaultPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @ApplicationScope
    @Named(UserPreferenceManager.PREFERENCES_FILE_NAME)
    public static SharedPreferences providesCoreSettingsPreferences(Context context) {
        return context.getSharedPreferences(UserPreferenceManager.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @ApplicationScope
    @Named(OrderingPreferencesManager.ORDERING_PREFERENCES)
    public static SharedPreferences providesOrderingPreferences(Context context) {
        return context.getSharedPreferences(OrderingPreferencesManager.ORDERING_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Provides
    @ApplicationScope
    @Named(AppRatingPreferencesStorage.RATING_PREFERENCES)
    public static SharedPreferences providesAppRatingPreferences(Context context) {
        return context.getSharedPreferences(AppRatingPreferencesStorage.RATING_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Provides
    @ApplicationScope
    @Named(TrialPreferencesStorage.TRIAL_PREFERENCES)
    public static SharedPreferences providesTrialPreferences(Context context) {
        return context.getSharedPreferences(TrialPreferencesStorage.TRIAL_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Provides
    @ApplicationScope
    @Named(OnboardingPreferencesStorage.ONBOARDING_PREFERENCES)
    public static SharedPreferences providesOnboardingPreferences(Context context) {
        return context.getSharedPreferences(OnboardingPreferencesStorage.ONBOARDING_PREFERENCES, Context.MODE_PRIVATE);
    }
}
