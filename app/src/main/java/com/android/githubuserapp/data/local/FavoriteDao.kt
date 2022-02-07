package com.android.githubuserapp.data.local

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.githubuserapp.data.model.FavoriteUser

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToFavorite(favoriteUser: FavoriteUser): Long

    @Query("DELETE FROM table_fav_user WHERE login = :login")
    fun deleteFromFavorite(login: String): Int

    @Query("SELECT * FROM table_fav_user WHERE login = :login")
    fun checkFavoriteUser(login: String): Cursor

    @Query("SELECT * FROM table_fav_user")
    fun getAllFavorites(): LiveData<List<FavoriteUser>>

}