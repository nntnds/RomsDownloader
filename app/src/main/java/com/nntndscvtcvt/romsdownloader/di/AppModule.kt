package com.nntndscvtcvt.romsdownloader.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.nntndscvtcvt.romsdownloader.data.local.AppDatabase
import com.nntndscvtcvt.romsdownloader.data.repository.CookieRepositoryImpl
import com.nntndscvtcvt.romsdownloader.data.repository.GameFavoriteRepositoryImpl
import com.nntndscvtcvt.romsdownloader.data.repository.GameRepositoryImpl
import com.nntndscvtcvt.romsdownloader.data.repository.SettingsRepositoryImpl
import com.nntndscvtcvt.romsdownloader.domain.repository.CookieRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.GameFavoriteRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.GameRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.SettingsRepository
import com.nntndscvtcvt.romsdownloader.presentation.favorite.FavoriteViewModel
import com.nntndscvtcvt.romsdownloader.presentation.game_info.GameInfoViewModel
import com.nntndscvtcvt.romsdownloader.presentation.home.HomeViewModel
import com.nntndscvtcvt.romsdownloader.presentation.login.LoginViewModel
import com.nntndscvtcvt.romsdownloader.presentation.search_result.SearchResultViewModel
import com.nntndscvtcvt.romsdownloader.presentation.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val Context.datastore by preferencesDataStore(name = "app_prefs")

val dataModule = module {
    // Firebase
    single { FirebaseFirestore.getInstance() }

    // DataStore
    single { androidContext().datastore }

    // Room
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = AppDatabase::class.java,
            name = "roms_database.db"
        ).build()
    }

    // Dao
    single { get<AppDatabase>().gameDao() }
    single { get<AppDatabase>().favoriteDao() }
}

// Repositories
val repositoryModule = module {
    singleOf(::GameRepositoryImpl) { bind<GameRepository>() }
    singleOf(::GameFavoriteRepositoryImpl) { bind<GameFavoriteRepository>() }
    singleOf(::CookieRepositoryImpl) { bind<CookieRepository>() }
    singleOf(::SettingsRepositoryImpl) { bind<SettingsRepository>() }
}

// ViewModel
val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::GameInfoViewModel)
    viewModelOf(::FavoriteViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::SearchResultViewModel)
}

val appModules = listOf(
    dataModule,
    repositoryModule,
    viewModelModule
)