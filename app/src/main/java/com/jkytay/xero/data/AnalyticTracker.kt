package com.jkytay.xero.data

import javax.inject.Inject
import kotlin.time.Duration

enum class Action {
    ButtonClick, Expand, Collapse
}

interface AnalyticTracker {
    fun trackUI(event: String, action: Action)

    fun trackError(exception: Exception? = null, message: String? = null)

    fun trackNetwork(event: String, duration: Duration)
}

internal class AnalyticTrackerImpl @Inject constructor() : AnalyticTracker {
    override fun trackUI(event: String, action: Action) {
        println("UI event: $event, action: $action")
    }

    override fun trackError(exception: Exception?, message: String?) {
        println("Error error: $exception, message: $message")
    }

    override fun trackNetwork(event: String, duration: Duration) {
        println("Network event: $event, duration: $duration")
    }
}
