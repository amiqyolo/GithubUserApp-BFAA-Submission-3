package com.android.githubuserapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.githubuserapp.R
import com.android.githubuserapp.adapter.GithubAdapter
import com.android.githubuserapp.data.model.FavoriteUser
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.databinding.ActivityFavoriteBinding
import com.android.githubuserapp.helper.MappingHelper.modelFavToUser
import com.android.githubuserapp.utility.Constants.EXTRA_USER
import com.android.githubuserapp.viewmodel.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteAdapter: GithubAdapter
    private val favoriteViewModel by viewModel<FavoriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        observe()
    }

    private fun observe() {
        observeAllFavUser()
    }

    private fun observeAllFavUser() {
        favoriteViewModel.getAllFavUser().observe(this, { handleAllFavUser(it) })
    }

    private fun handleAllFavUser(listFav: List<FavoriteUser>) {
        if (listFav.isNotEmpty()) {
            val result = modelFavToUser(listFav)
            favoriteAdapter.setListData(result)
            showEmptyFavUser(false)
            showRecyclerView(true)
        } else {
            showRecyclerView(false)
            showEmptyFavUser(true)
        }
    }

    private fun setupRecyclerView() {
        favoriteAdapter = GithubAdapter()

        binding.recyclerviewFavUser.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewFavUser.setHasFixedSize(true)
        binding.recyclerviewFavUser.adapter = favoriteAdapter

        favoriteAdapter.setOnItemClickCallBack(object : GithubAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: User) {
                startActivity(Intent(this@FavoriteActivity, DetailActivity::class.java).apply {
                    putExtra(EXTRA_USER, data)
                })
            }
        })
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.favorites)
    }

    private fun showEmptyFavUser(isEmpty: Boolean) {
        with(binding) {
            favoriteLayoutIllustrations.root.visibility = if (isEmpty) {
                favoriteLayoutIllustrations.apply {
                    imgStateIllustration.setImageResource(R.drawable.ic_undraw_empty)
                    tvTitleIllustration.text =
                        resources.getString(R.string.title_favorite_empty)
                    tvMessageIllustration.text =
                        resources.getString(R.string.message_favorite_empty)
                }
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun showRecyclerView(isRecycler: Boolean) {
        binding.recyclerviewFavUser.visibility = if (isRecycler) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_menu_settings -> {
                startActivity(Intent(this, SettingActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}