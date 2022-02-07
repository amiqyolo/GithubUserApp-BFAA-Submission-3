@file:Suppress("unused")

package com.android.githubuserapp

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import com.android.githubuserapp.data.local.FavoriteDao
import com.android.githubuserapp.data.local.FavoriteDatabase
import com.android.githubuserapp.networking.ApiConfig
import com.android.githubuserapp.repository.*
import com.android.githubuserapp.utility.Constants
import com.android.githubuserapp.viewmodel.*
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class GithubAppDelegate : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@GithubAppDelegate)
            modules(
                listOf(
                    retrofitModule,
                    repositoryModule,
                    viewModelModule,
                    databaseModule,
                    datastoreModule
                )
            )
        }
    }

    private val retrofitModule = module {
        single { ApiConfig.getApiService() }
    }

    private val repositoryModule = module {
        factory { MainRepository(get(), get()) }
        factory { DetailRepository(get(), get()) }
        factory { FollowersRepository(get()) }
        factory { FollowingRepository(get()) }
        factory { FavoriteRepository(get()) }
        factory { SettingRepository(androidContext()) }
    }

    private val viewModelModule = module {
        viewModel { MainViewModel(get()) }
        viewModel { DetailViewModel(get()) }
        viewModel { FollowersViewModel(get()) }
        viewModel { FollowingViewModel(get()) }
        viewModel { FavoriteViewModel(get()) }
        viewModel { SettingViewModel(get()) }
    }

    private val databaseModule = module {

        fun provideDatabase(application: Application): FavoriteDatabase {
            return Room.databaseBuilder(
                application,
                FavoriteDatabase::class.java,
                Constants.DATABASE_NAME
            ).build()
        }

        fun provideFavoriteDao(database: FavoriteDatabase): FavoriteDao {
            return database.favoriteDao()
        }

        single { provideDatabase(androidApplication()) }
        single { provideFavoriteDao(get()) }
    }

    private val datastoreModule = module {
        single { SettingRepository(androidContext()) }
    }

}