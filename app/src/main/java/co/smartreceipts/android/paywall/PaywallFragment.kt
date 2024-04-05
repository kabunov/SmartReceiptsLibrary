package co.smartreceipts.android.paywall

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import co.smartreceipts.analytics.log.Logger
import co.smartreceipts.android.R
import co.smartreceipts.android.activities.NavigationHandler
import co.smartreceipts.android.activities.SmartReceiptsActivity
import co.smartreceipts.android.databinding.PaywallFragmentBinding
import com.jakewharton.rxbinding3.view.clicks
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import javax.inject.Inject

class PaywallFragment : Fragment(), PaywallView {

    @Inject
    lateinit var presenter: PaywallPresenter

    @Inject
    lateinit var navigationHandler: NavigationHandler<SmartReceiptsActivity>

    private var _binding: PaywallFragmentBinding? = null
    private val binding get() = _binding!!

    override val submitButtonClicks: Observable<Unit> get() = binding.buttonSubmit.clicks()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = PaywallFragmentBinding.inflate(inflater, container, false)

        binding.textSubscriptionPrice.paintFlags = binding.textSubscriptionPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        binding.buttonClose.setOnClickListener {
            navigationHandler.navigateBack()
        }

        binding.success.buttonContinue.setOnClickListener {  navigationHandler.navigateBack() }
        binding.success.buttonClose.setOnClickListener {  navigationHandler.navigateBack() }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        this.presenter.subscribe()
    }

    override fun onStop() {
        this.presenter.unsubscribe()
        super.onStop()
    }

    override fun presentPurchasePrice(): Consumer<in String?> {
        return Consumer<String?> { price: String? ->
            Logger.debug(this, "[trial] purchasePrice $price")
            if (price != null) {
                binding.textSubscriptionPrice.text = price
            }
        }
    }

    override fun presentLoading() {
        binding.progress.isVisible = true
    }

    override fun presentSuccessSubscription() {
        Logger.debug(this, "presentSuccessSubscription")
        binding.progress.isVisible = false
        binding.success.root.isVisible = true
    }

    override fun presentFailedSubscription() {
        binding.progress.isVisible = false
        Toast.makeText(requireContext(), R.string.purchase_failed, Toast.LENGTH_LONG).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = PaywallFragment()
    }

}