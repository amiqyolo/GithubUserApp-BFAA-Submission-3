package com.android.githubuserapp.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.githubuserapp.R
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.databinding.ItemRowGithubUserBinding
import com.android.githubuserapp.helper.DiffCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions

class GithubAdapter : RecyclerView.Adapter<GithubAdapter.GithubViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallBack
    private val listUsers = ArrayList<User>()

    fun setListData(listUsers: List<User>) {
        val diffCallback = DiffCallback(this.listUsers, listUsers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listUsers.clear()
        this.listUsers.addAll(listUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallback = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: User)
    }

    inner class GithubViewHolder(private val binding: ItemRowGithubUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        internal fun User.bind() {
            binding.apply {
                Glide.with(itemView.context).load(avatarUrl).apply(
                    RequestOptions().override(56, 56)
                ).error(R.drawable.ic_launcher_foreground)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    }).into(imgItemAvatar)

                tvItemLogin.text = login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubViewHolder {
        return GithubViewHolder(
            ItemRowGithubUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GithubViewHolder, position: Int) {
        with(holder) {
            val user: User = listUsers[position]
            user.bind()
            itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }
        }
    }

    override fun getItemCount(): Int = listUsers.size

}