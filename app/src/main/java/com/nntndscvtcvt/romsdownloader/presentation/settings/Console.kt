package com.nntndscvtcvt.romsdownloader.presentation.settings

import androidx.annotation.DrawableRes
import com.nntndscvtcvt.romsdownloader.R

data class Console(
    val consoleName: String,
    @DrawableRes val icon: Int,
    val size: String,
    val gamesCount: String
)

val consoles = listOf(
    Console(
        consoleName = "PSP",
        icon = R.drawable.psp_ic,
        size = "2,3 MB",
        gamesCount = "1357 games"
    ),
    Console(
        consoleName = "PS2",
        icon = R.drawable.ps2_ic,
        size = "5,6 MB",
        gamesCount = "2894 games"
    )
)
