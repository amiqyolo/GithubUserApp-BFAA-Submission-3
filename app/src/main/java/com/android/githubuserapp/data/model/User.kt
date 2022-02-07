package com.android.githubuserapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val login: String,
    val avatarUrl: String,
    val followers: Int,
    val following: Int,
    val name: String?,
    val company: String?,
    val location: String?,
    val publicRepos: Int
) : Parcelable {
    constructor(avatarUrl: String, login: String) : this(
        login,
        avatarUrl,
        0,
        0,
        null,
        null,
        null,
        0
    )
}
