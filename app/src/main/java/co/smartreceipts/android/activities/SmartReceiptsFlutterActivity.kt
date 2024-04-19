package co.smartreceipts.android.activities

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class SmartReceiptsFlutterActivity : FlutterActivity() {
    private var methodChannel: MethodChannel? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, ONBOARDING_CHANNEL)
            .apply {
                setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
                    when (call.method) {
                        MESSAGE_DONE -> {
                            result.success(null)
                            finish()
                        }

                        else -> {
                            result.notImplemented()
                        }
                    }
                }
            }
    }

    companion object {

        private const val ONBOARDING_CHANNEL = "onboarding"
        private const val MESSAGE_DONE = "done"
    }
}
