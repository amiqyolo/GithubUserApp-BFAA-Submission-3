package com.android.githubuserapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.android.githubuserapp.data.model.User

class DiffCallback(private val mOldFavData: List<User>, private val mNewFavData: List<User>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = mOldFavData.size

    override fun getNewListSize(): Int = mNewFavData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        mOldFavData[oldItemPosition].login == mNewFavData[newItemPosition].login

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldFavData[oldItemPosition]
        val newEmployee = mNewFavData[newItemPosition]
        return oldEmployee.login == newEmployee.login && oldEmployee.avatarUrl == newEmployee.avatarUrl
    }

}