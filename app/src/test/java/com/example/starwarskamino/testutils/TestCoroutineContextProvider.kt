package com.example.starwarskamino.testutils

import com.example.starwarskamino.general.CoroutineContextProvider
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * This will change all dispatchers to Unconfined, which executes initial continuation
 * of the coroutine in the current call-frame and lets the coroutine resume in whatever
 * thread that is used by the corresponding suspending function,
 * without mandating any specific threading policy.
 */
class TestCoroutineContextProvider : CoroutineContextProvider() {
    override val Main: CoroutineContext = Dispatchers.Unconfined
    override val IO: CoroutineContext = Dispatchers.Unconfined
}