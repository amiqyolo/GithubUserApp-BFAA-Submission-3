package com.android.githubuserapp.helper

import android.database.Cursor
import com.android.githubuserapp.data.model.FavoriteUser
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.data.response.DetailResponse
import com.android.githubuserapp.data.response.FollowersFollowingResponse
import com.android.githubuserapp.data.response.ItemsItem
import com.android.githubuserapp.utility.Constants.KEY_AVATAR
import com.android.githubuserapp.utility.Constants.KEY_LOGIN

object MappingHelper {

    fun responseSearchToUser(searchResponse: List<ItemsItem>): List<User> {
        val models = mutableListOf<User>()

        searchResponse.forEach {
            it.apply {
                models.add(
                    User(
                        login = this.login,
                        avatarUrl = this.avatarUrl
                    )
                )
            }
        }

        return models
    }

    fun responseDetailToUser(detailResponse: DetailResponse): User {
        return User(
            avatarUrl = detailResponse.avatarUrl,
            login = detailResponse.login,
            followers = detailResponse.followers,
            following = detailResponse.following,
            name = detailResponse.name,
            company = detailResponse.company,
            location = detailResponse.location,
            publicRepos = detailResponse.publicRepos
        )
    }

    fun responseFollowersFollowingToUser(
        followersFollowingResponse: List<FollowersFollowingResponse>
    ): List<User> {
        val models = mutableListOf<User>()

        followersFollowingResponse.forEach {
            it.apply {
                models.add(
                    User(
                        login = this.login,
                        avatarUrl = this.avatarUrl
                    )
                )
            }
        }

        return models as ArrayList<User>
    }

    fun modelFavToUser(favUser: List<FavoriteUser>): List<User> {
        val models = mutableListOf<User>()

        favUser.forEach {
            it.apply {
                models.add(
                    User(
                        login = this.login,
                        avatarUrl = this.avatarUrl
                    )
                )
            }
        }

        return models
    }

    fun cursorToFavUser(cursor: Cursor): List<FavoriteUser> {
        val models = mutableListOf<FavoriteUser>()

        cursor.apply {
            while (moveToNext()) {
                val login = getString(getColumnIndexOrThrow(KEY_LOGIN))
                val avatar = getString(getColumnIndexOrThrow(KEY_AVATAR))

                models.add(
                    FavoriteUser(
                        login,
                        avatar
                    )
                )
            }
        }

        return models
    }
}