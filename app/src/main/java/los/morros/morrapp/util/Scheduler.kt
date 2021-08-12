package los.morros.morrapp.util

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object Scheduler {
    private val threads = mutableMapOf<String, ExecutorService>()
    private fun getThread(threadName: String) =
        threads[threadName] ?: Executors.newCachedThreadPool().also { threads[threadName] = it }

    fun async(threadName: String = "default", block: () -> Unit) = getThread(threadName).execute(block)
}