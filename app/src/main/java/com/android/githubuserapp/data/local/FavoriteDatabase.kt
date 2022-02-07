package com.android.githubuserapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.githubuserapp.data.model.FavoriteUser

@Database(
    entities = [FavoriteUser::class],
    version = 1,
    exportSchema = false
)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

}