package com.android.githubuserapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.githubuserapp.R
import com.android.githubuserapp.adapter.GithubAdapter
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.databinding.ActivityMainBinding
import com.android.githubuserapp.utility.Constants.EXTRA_USER
import com.android.githubuserapp.utility.MainState
import com.android.githubuserapp.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainAdapter: GithubAdapter
    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        observe()
    }

    private fun observe() {
        observeDarkMode()
        observeSearchUser()
        observeQueryUser()
        observeState()
    }

    private fun observeDarkMode() {
        mainViewModel.getThemeSettings().observe(this, { handleDarkMode(it) })
    }

    private fun handleDarkMode(isDarkActive: Boolean) {
        if (isDarkActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun observeState() {
        mainViewModel.state.observe(this, { handleState(it) })
    }

    private fun handleState(state: MainState) {
        when (state) {
            is MainState.Loading -> showLoading(state.isLoading)
            is MainState.ServerState -> showErrorIllustration(state.isError, state.message)
            else -> return
        }
    }

    private fun observeQueryUser() {
        binding.itemSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    mainViewModel.setSearchUser(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun observeSearchUser() {
        mainViewModel.listSearchData.observe(this, { handleSearchUser(it) })
    }

    private fun handleSearchUser(users: ArrayList<User>) {
        binding.recyclerviewSearchUser.adapter.let { user ->
            if (user is GithubAdapter) {
                if (users.isNotEmpty()) {
                    user.setListData(users)
                    showEmptyIllustration(false)
                    showRecyclerView(true)
                } else {
                    showRecyclerView(false)
                    showEmptyIllustration(true)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        mainAdapter = GithubAdapter()

        binding.recyclerviewSearchUser.layoutManager = LinearLayoutManager(applicationContext)
        val itemDecoration =
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        binding.recyclerviewSearchUser.addItemDecoration(itemDecoration)
        binding.recyclerviewSearchUser.setHasFixedSize(true)
        binding.recyclerviewSearchUser.recycledViewPool.clear()
        binding.recyclerviewSearchUser.adapter = mainAdapter

        mainAdapter.setOnItemClickCallBack(object :
            GithubAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: User) {
                startActivity(Intent(applicationContext, DetailActivity::class.java).apply {
                    putExtra(EXTRA_USER, data)
                })
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        this.supportActionBar?.apply {
            title = resources.getString(R.string.app_name)
        }
    }

    private fun showEmptyIllustration(isEmpty: Boolean) {
        with(binding) {
            mainLayoutIllustrations.root.visibility = if (isEmpty) {
                mainLayoutIllustrations.apply {
                    imgStateIllustration.setImageResource(R.drawable.ic_undraw_empty)
                    tvTitleIllustration.text =
                        resources.getString(R.string.title_state_empty)
                    tvMessageIllustration.text =
                        resources.getString(R.string.message_state_empty)
                }
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun showErrorIllustration(isError: Boolean, message: String?) {
        with(binding) {
            recyclerviewSearchUser.visibility = View.GONE
            mainLayoutIllustrations.root.visibility = if (isError) {
                mainLayoutIllustrations.apply {
                    imgStateIllustration.setImageResource(R.drawable.ic_undraw_server_status)
                    tvTitleIllustration.text =
                        resources.getString(R.string.title_state_server)
                    tvMessageIllustration.text =
                        resources.getString(R.string.message_state_server, message)
                }
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun showRecyclerView(isRecycler: Boolean) {
        binding.recyclerviewSearchUser.visibility = if (isRecycler) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.mainProgressBar.root.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_menu_fav -> {
                startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
            }
            R.id.action_menu_settings -> {
                startActivity(Intent(this, SettingActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}



