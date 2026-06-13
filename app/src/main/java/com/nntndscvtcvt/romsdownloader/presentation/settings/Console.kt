package com.nntndscvtcvt.romsdownloader.presentation.settings

import androidx.annotation.DrawableRes
import com.nntndscvtcvt.romsdownloader.R

data class Console(
    val consoleName: String,
    val platform: String,
    @DrawableRes val icon: Int,
    val size: String,
)

val consoles = listOf(
    Console(
        consoleName = "PSP",
        platform = "Sony PSP",
        icon = R.drawable.psp_ic,
        size = "2,1 MB"
    ),
    Console(
        consoleName = "PS2",
        platform = "Sony Playstation 2",
        icon = R.drawable.ps2_ic,
        size = "5,4 MB",
    )
)
