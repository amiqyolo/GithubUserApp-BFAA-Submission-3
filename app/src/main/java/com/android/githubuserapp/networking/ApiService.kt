package com.android.githubuserapp.networking

import com.android.githubuserapp.data.response.DetailResponse
import com.android.githubuserapp.data.response.FollowersFollowingResponse
import com.android.githubuserapp.data.response.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun getSearchUser(@Query("q") query: String)
            : Response<SearchResponse>

    @GET("users/{login}")
    suspend fun getDetailUser(@Path("login") login: String)
            : Response<DetailResponse>

    @GET("users/{login}/followers")
    suspend fun getFollowersUser(@Path("login") login: String)
            : Response<List<FollowersFollowingResponse>>

    @GET("users/{login}/following")
    suspend fun getFollowingUser(@Path("login") login: String)
            : Response<List<FollowersFollowingResponse>>
}