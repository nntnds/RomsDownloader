package com.nntndscvtcvt.romsdownloader.domain.repository

import com.nntndscvtcvt.romsdownloader.domain.model.Game
import kotlinx.coroutines.flow.Flow

interface SearchGameRepository {
    fun searchGame(query: String): Flow<List<Game>>
}