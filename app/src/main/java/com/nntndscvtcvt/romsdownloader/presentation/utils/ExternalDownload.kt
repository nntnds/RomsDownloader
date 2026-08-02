package com.nntndscvtcvt.romsdownloader.presentation.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun launchExternalDownload(url: String, context: Context): Boolean {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(url.toUri(), "application/octet-stream")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    return try {
        context.startActivity(Intent.createChooser(intent, "Download with..."))
        true
    } catch(e: ActivityNotFoundException) {
        false
    }
}