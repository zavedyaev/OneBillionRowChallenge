package com.zavediaev.obrc.attempt3

import com.zavediaev.obrc.attempt2.StorageWithStringIntMaps
import com.zavediaev.obrc.attempt2.processLines
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.concurrent.Executors

/**
 * Multi-thread implementation using coroutines.
 * Reading the file synchronously and sending chunks of rows to processing coroutines.
 * Optimizations against [com.zavediaev.obrc.attempt2.calc]:
 * 1. parallel data processing
 *
 * Takes ~43 seconds
 */
fun calc(path: String) {
    val threadPool = Executors.newFixedThreadPool(12)
    runBlocking(threadPool.asCoroutineDispatcher()) {
        val linesChannel = Channel<List<String>>(1000)

        val file = File(path)

        async {
            file.useLines { lines ->
                lines.chunked(1000).forEach { chunk ->
                    linesChannel.send(chunk)
                }
                linesChannel.close()
            }
        }.start()

        val processCoroutinesNumber = 12
        val deferred = (0 until processCoroutinesNumber).map { coroutineIndex ->
            val coroutine = async {
                var lines = linesChannel.receiveCatching().getOrNull()
                val storage = StorageWithStringIntMaps()
                while (lines != null) {
                    processLines(lines.iterator(), storage)
                    lines = linesChannel.receiveCatching().getOrNull()
                }
                storage
            }
            coroutine.start()
            coroutine
        }

        val storage = deferred.map { it.await() }.reduce { acc, storageWithInt -> acc + storageWithInt }
        storage.printResults()
    }
    threadPool.shutdown()
}
