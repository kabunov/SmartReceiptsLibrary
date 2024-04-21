package co.smartreceipts.android.subscriptions

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import co.smartreceipts.analytics.Analytics
import co.smartreceipts.analytics.events.Events
import co.smartreceipts.analytics.log.Logger
import co.smartreceipts.android.R
import co.smartreceipts.android.databinding.ActivitySubscriptionsBinding
import com.jakewharton.rxbinding3.view.clicks
import dagger.android.AndroidInjection
import io.reactivex.Observable
import javax.inject.Inject

class SubscriptionsActivity : AppCompatActivity(), SubscriptionsView {

    companion object {
        const val RESULT_NEED_LOGIN = 45321
        const val RESULT_OK = 45322
    }

    @Inject
    lateinit var presenter: SubscriptionsPresenter

    @Inject
    lateinit var analytics: Analytics

    override val standardSubscriptionClicks: Observable<Unit>
        get() = binding.cardStandard.clicks().filter { !binding.yourPlanStandard.isVisible }
    override val premiumSubscriptionClicks: Observable<Unit>
        get() = binding.cardPremium.clicks().filter { !binding.yourPlanPremium.isVisible }
    override val trialSubscriptionClicks: Observable<Unit>
        get() = binding.trialButtonContainer.clicks()
            .mergeWith(binding.trialTitle.clicks())
            .filter { binding.trialButtonTitle.isVisible }
    override val cancelSubscriptionInfoClicks: Observable<Unit> get() = binding.cancelSubscriptionInfo.clicks()

    private var _binding: ActivitySubscriptionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        _binding = ActivitySubscriptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardStandard.isVisible = false
        binding.cardPremium.isVisible = false

        binding.success.buttonContinue.setOnClickListener { finish() }
        binding.success.buttonClose.setOnClickListener { finish() }

        if (savedInstanceState == null) {
            analytics.record(Events.Subscriptions.SubscriptionShown)
        }
    }

    override fun onResume() {
        super.onResume()

        setSupportActionBar(binding.toolbar.toolbar)

        supportActionBar?.apply {
            setTitle(R.string.menu_main_subscriptions)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            analytics.record(Events.Subscriptions.SubscriptionClose)

            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribe()
    }

    override fun onStop() {
        presenter.unsubscribe()
        super.onStop()
    }

    override fun presentStandardPlan(price: String?) {

        binding.cardStandard.isVisible = true

        if (price == null) { // this plan is current
            binding.priceStandardContainer.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.subscription_current_plan_bg)

            binding.priceStandard.isVisible = false
            binding.perMonthStandard.isVisible = false
            binding.yourPlanStandard.isVisible = true
        } else {
            binding.priceStandardContainer.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.violet)

            binding.priceStandard.text = price

            binding.priceStandard.isVisible = true
            binding.perMonthStandard.isVisible = true
            binding.yourPlanStandard.isVisible = false
        }
    }

    override fun presentPremiumPlan(price: String?) {

        binding.cardPremium.isVisible = true

        if (price == null) { // this plan is current
            binding.pricePremiumContainer.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.subscription_current_plan_bg)

            binding.pricePremium.isVisible = false
            binding.perMonthPremium.isVisible = false
            binding.yourPlanPremium.isVisible = true
        } else {
            binding.pricePremiumContainer.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.violet)

            binding.pricePremium.text = price

            binding.pricePremium.isVisible = true
            binding.perMonthPremium.isVisible = true
            binding.yourPlanPremium.isVisible = false
        }
    }

    override fun presentTrialPlan(price: String?) {
        if (price == null) { // this plan is current
            binding.trialButtonContainer.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.subscription_current_plan_bg)

            binding.trialButtonTitle.isVisible = false
            binding.trialButtonSubtitle.isVisible = false
            binding.yourPlanTrial.isVisible = true
        } else {
            binding.trialButtonContainer.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.subscription_current_plan_trial_bg)

            binding.trialButtonTitle.isVisible = true
            binding.trialButtonSubtitle.isVisible = true
            binding.yourPlanTrial.isVisible = false

            binding.trialButtonSubtitle.text = getString(R.string.subscriptions_trial_button_subtitle, price)
        }
    }

    override fun redirectToPlayStoreSubscriptions() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/account/subscriptions")
            )
        )
    }

    override fun presentCancelInfo(isVisible: Boolean) {
        binding.cancelSubscriptionInfo.isVisible = isVisible
    }

    override fun presentSuccessSubscription() {
        Logger.debug(this, "presentSuccessSubscription")
        binding.progress.isVisible = false
        supportActionBar?.hide()
        binding.success.root.isVisible = true
    }

    override fun presentFailedSubscription() {
        binding.progress.isVisible = false
        Toast.makeText(this, R.string.purchase_failed, Toast.LENGTH_LONG).show()
    }

    override fun presentLoading() {
        binding.progress.isVisible = true
    }

    override fun navigateToLogin() {
        setResult(RESULT_NEED_LOGIN)
        finish()
    }

    override fun navigateBack() {
        setResult(RESULT_OK)
        finish()
    }
}