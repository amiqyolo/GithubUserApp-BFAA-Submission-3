package com.android.githubuserapp.ui.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.android.githubuserapp.R
import com.android.githubuserapp.adapter.SectionPagerAdapter
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.databinding.ActivityDetailBinding
import com.android.githubuserapp.utility.Constants.EXTRA_USER
import com.android.githubuserapp.utility.MainState
import com.android.githubuserapp.viewmodel.DetailViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var user: User
    private val detailViewModel by viewModel<DetailViewModel>()
    private var isFavorite = false

    @StringRes
    private val tabsTitle = intArrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        setupToolbar()
        setupNavigation()
        observe()
    }

    private fun observe() {
        observeDetailUser()
        observeCheckLogin()
        observeCheckFavorite()
        observeState()
    }

    private fun observeState() {
        detailViewModel.state.observe(this, { handleState(it) })
    }

    private fun handleState(state: MainState) {
        when (state) {
            is MainState.Loading -> showLoading(state.isLoading)
            is MainState.Favorites -> setItemFavorite(state.isFavorites)
            else -> return
        }
    }

    private fun setItemFavorite(favorites: Boolean) {
        val icon = if (favorites) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        binding.fabFavorite.setImageResource(icon)
        isFavorite = !favorites
        binding.fabFavorite.setOnClickListener {
            isFavorite = if (isFavorite) {
                detailViewModel.addToFavorite(user)
                Toast.makeText(this, "Success Add to Favorite List!", Toast.LENGTH_SHORT)
                    .show()
                false
            } else {
                detailViewModel.deleteFromFavorite(user)
                Toast.makeText(this, "Success Remove from Favorite List!", Toast.LENGTH_SHORT)
                    .show()
                true
            }
        }
    }

    private fun observeCheckFavorite() {
        detailViewModel.checkFavoriteUser(user.login)
    }

    private fun observeCheckLogin() {
        detailViewModel.setDetailData(user.login)
    }

    private fun observeDetailUser() {
        detailViewModel.listDetailData.observe(this, { handleDetailUser(it) })
    }

    private fun handleDetailUser(user: User) {
        binding.apply {
            Glide.with(this@DetailActivity).load(user.avatarUrl).apply(
                RequestOptions().override(96, 96)
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
                }).into(ivDetailAvatarUrl)

            tvDetailLogin.text = user.login
            tvDetailPublicRepos.text = user.publicRepos.toString()

            if (user.name == null) {
                tvDetailName.text = getString(R.string.detail_no_name)
            } else {
                tvDetailName.text = user.name
            }

            if (user.company == null) {
                tvDetailCompany.text = getString(R.string.detail_no_company)
            } else {
                tvDetailCompany.text = user.company
            }

            if (user.location == null) {
                tvDetailLocation.text = getString(R.string.detail_no_location)
            } else {
                tvDetailLocation.text = user.location
            }

            tabs.getTabAt(0)?.text = getString(R.string.info_follower) + user.followers
            tabs.getTabAt(1)?.text = getString(R.string.info_following) + user.following
        }
    }

    private fun setupNavigation() {
        with(binding) {
            viewPager2.adapter = SectionPagerAdapter(this@DetailActivity, user)
            TabLayoutMediator(tabs, viewPager2) { tab, position ->
                tab.text = resources.getString(tabsTitle[position])
            }.attach()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        this.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(
                resources.getIdentifier(
                    "R.drawable.ic_arrow_back",
                    "drawable",
                    packageName
                )
            )
            title = resources.getString(R.string.detail_title)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.detailProgressBar.root.visibility = if (isLoading) View.VISIBLE else View.GONE
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