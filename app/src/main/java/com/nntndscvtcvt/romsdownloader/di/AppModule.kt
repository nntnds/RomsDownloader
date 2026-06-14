package com.nntndscvtcvt.romsdownloader.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.nntndscvtcvt.romsdownloader.data.local.AppDatabase
import com.nntndscvtcvt.romsdownloader.data.repository.CookieRepositoryImpl
import com.nntndscvtcvt.romsdownloader.data.repository.DownloadFileRepositoryImpl
import com.nntndscvtcvt.romsdownloader.data.repository.GameFavoriteRepositoryImpl
import com.nntndscvtcvt.romsdownloader.data.repository.GameInfoRepositoryImpl
import com.nntndscvtcvt.romsdownloader.data.repository.GameRepositoryImpl
import com.nntndscvtcvt.romsdownloader.data.repository.SearchGameRepositoryImpl
import com.nntndscvtcvt.romsdownloader.domain.repository.CookieRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.DownloadFileRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.GameFavoriteRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.GameInfoRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.GameRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.SearchGameRepository
import com.nntndscvtcvt.romsdownloader.presentation.download.DownloadViewModel
import com.nntndscvtcvt.romsdownloader.presentation.favorite.FavoriteViewModel
import com.nntndscvtcvt.romsdownloader.presentation.game_info.GameInfoViewModel
import com.nntndscvtcvt.romsdownloader.presentation.home.HomeViewModel
import com.nntndscvtcvt.romsdownloader.presentation.login.LoginViewModel
import com.nntndscvtcvt.romsdownloader.presentation.search_result.SearchResultViewModel
import com.nntndscvtcvt.romsdownloader.presentation.settings.SettingsViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

private val Context.datastore by preferencesDataStore(name = "app_prefs")

val appModule = module {
    single { FirebaseFirestore.getInstance() }
    single { androidContext().datastore }
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = AppDatabase::class.java,
            name = "roms_database.db"
        ).fallbackToDestructiveMigration(true).build()
    }

    single { get<AppDatabase>().gameDao() }
    single { get<AppDatabase>().favoriteDao() }
    single { get<AppDatabase>().downloadDao() }

    single<GameRepository> { GameRepositoryImpl(get(), get()) }
    single<SearchGameRepository> { SearchGameRepositoryImpl(get()) }
    single<GameInfoRepository> { GameInfoRepositoryImpl(get()) }
    single<GameFavoriteRepository> { GameFavoriteRepositoryImpl(get()) }
    single<CookieRepository> { CookieRepositoryImpl(get()) }
    single<DownloadFileRepository> { DownloadFileRepositoryImpl(get(), androidApplication(), get()) }

    viewModelOf(::HomeViewModel)
    viewModelOf(::GameInfoViewModel)
    viewModelOf(::FavoriteViewModel)
    viewModelOf(::DownloadViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::SearchResultViewModel)
}