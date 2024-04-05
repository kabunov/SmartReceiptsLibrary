package co.smartreceipts.android.apis.okhttp

import android.os.Build
import co.smartreceipts.android.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .header("User-Agent", "SmartReceipts/${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE}) ${Build.BRAND} ${Build.MODEL} sdk:${Build.VERSION.SDK_INT}") //SmartReceipts/1.2.3 Samsung Galaxy 5 Android 10
                .build()
        )
    }
}