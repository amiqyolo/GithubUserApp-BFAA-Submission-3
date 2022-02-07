package com.android.githubuserapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.githubuserapp.utility.Constants.KEY_AVATAR
import com.android.githubuserapp.utility.Constants.KEY_LOGIN
import com.android.githubuserapp.utility.Constants.TABLE_NAME
import kotlinx.parcelize.Parcelize

@Entity(tableName = TABLE_NAME)
@Parcelize
data class FavoriteUser(
    @PrimaryKey
    @ColumnInfo(name = KEY_LOGIN)
    val login: String,

    @ColumnInfo(name = KEY_AVATAR)
    val avatarUrl: String
) : Parcelable