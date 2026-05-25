package com.outfitbase.util

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class FileLoggingTree(filesDir: File) : Timber.Tree() {

    private val logDir = File(filesDir, "logs").also { it.mkdirs() }
    private val logFile = File(logDir, "outfitbase.log")
    private val backupFile = File(logDir, "outfitbase.log.bak")
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        .withZone(ZoneId.systemDefault())

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        scope.launch {
            rotateIfNeeded()
            val timestamp = formatter.format(Instant.now())
            val level = priorityToLabel(priority)
            val line = buildString {
                append(timestamp)
                append(' ')
                append(level)
                append('/')
                append(tag ?: "NoTag")
                append(": ")
                append(message)
                if (t != null) {
                    append('\n')
                    append(t.stackTraceToString())
                }
                append('\n')
            }
            try {
                FileWriter(logFile, true).use { it.write(line) }
            } catch (_: Exception) {
                // Cannot log the logging failure — avoid infinite loop
            }
        }
    }

    private fun rotateIfNeeded() {
        if (logFile.exists() && logFile.length() > MAX_FILE_SIZE) {
            backupFile.delete()
            logFile.renameTo(backupFile)
        }
    }

    private fun priorityToLabel(priority: Int): String = when (priority) {
        Log.VERBOSE -> "V"
        Log.DEBUG -> "D"
        Log.INFO -> "I"
        Log.WARN -> "W"
        Log.ERROR -> "E"
        Log.ASSERT -> "A"
        else -> "?"
    }

    companion object {
        private const val MAX_FILE_SIZE = 2L * 1024 * 1024 // 2 MB
    }
}
