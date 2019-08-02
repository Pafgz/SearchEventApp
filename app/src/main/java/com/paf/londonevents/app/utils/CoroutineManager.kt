package com.paf.londonevents.app.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

/**
 * This encapsulates the supervisorJob + coroutineContext dance so that
 * fragments can launch coroutines that come back on the main thread.
 */
class CoroutineManager: CoroutineScope {

    val supervisorJob = SupervisorJob()
    override val coroutineContext = Dispatchers.Main + supervisorJob
}

interface ManagesCoroutines: CoroutineScope {
    
    var coroutineManager: CoroutineManager

    override val coroutineContext: CoroutineContext
        get() = coroutineManager.coroutineContext

    fun cancelRunningCoroutines() {
        coroutineManager.supervisorJob.cancelChildren()
    }
}