package com.withpeace.withpeace.core.analytics

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "StubAnalyticsHelper"
@Singleton
internal class StubAnalyticsHelper @Inject constructor() : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        Log.d(TAG, "Received analytics event: $event")
    }
}