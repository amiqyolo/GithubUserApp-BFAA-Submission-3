package com.android.githubuserapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.githubuserapp.R
import com.android.githubuserapp.adapter.GithubAdapter
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.databinding.FragmentFollowersBinding
import com.android.githubuserapp.ui.activity.DetailActivity
import com.android.githubuserapp.utility.Constants.EXTRA_USER
import com.android.githubuserapp.utility.MainState
import com.android.githubuserapp.viewmodel.FollowersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FollowersFragment : Fragment() {

    private lateinit var followersAdapter: GithubAdapter
    private var _fragmentFollowersBinding: FragmentFollowersBinding? = null
    private val binding get() = _fragmentFollowersBinding
    private val followersViewModel by viewModel<FollowersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentFollowersBinding = FragmentFollowersBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observe()

        return binding?.root
    }

    private fun observe() {
        observeQueryLogin()
        observeFollowersData()
        observeState()
    }

    private fun observeState() {
        followersViewModel.state.observe(viewLifecycleOwner, { handleState(it) })
    }

    private fun handleState(state: MainState) {
        when (state) {
            is MainState.Loading -> showLoading(state.isLoading)
            is MainState.ServerState -> showErrorIllustration(state.isError, state.message)
            else -> return
        }
    }

    private fun observeQueryLogin() {
        arguments?.getParcelable<User>(EXTRA_USER).let { user ->
            if (user != null) {
                followersViewModel.setFollowersData(user.login)
            }
        }
    }

    private fun observeFollowersData() {
        followersViewModel.listFollowersData.observe(
            viewLifecycleOwner,
            { handleFollowersData(it) })
    }

    private fun handleFollowersData(users: ArrayList<User>?) {
        if (users != null && users.isNotEmpty()) {
            followersAdapter.setListData(users)
            showEmptyIllustration(false)
            showRecyclerView(true)
        } else {
            showRecyclerView(false)
            showEmptyIllustration(true)
        }
    }

    private fun setupRecyclerView() {
        followersAdapter = GithubAdapter()

        binding?.recyclerviewFollowers?.layoutManager = LinearLayoutManager(requireContext())
        binding?.recyclerviewFollowers?.setHasFixedSize(true)
        binding?.recyclerviewFollowers?.adapter = followersAdapter

        followersAdapter.setOnItemClickCallBack(object :
            GithubAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: User) {
                startActivity(Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra(EXTRA_USER, data)
                })
            }
        })
    }

    override fun onResume() {
        super.onResume()
        followersViewModel.listFollowersData.observe(this, {
            if (it != null) {
                followersAdapter.setListData(it)
            }
        })
    }

    private fun showEmptyIllustration(isEmpty: Boolean) {
        binding?.let {
            with(it.followersLayoutIllustrations) {
                root.visibility = if (isEmpty) {
                    imgStateIllustration.setImageResource(R.drawable.ic_undraw_empty)
                    tvTitleIllustration.text =
                        resources.getString(R.string.title_followers_empty)
                    tvMessageIllustration.text =
                        resources.getString(R.string.message_followers_empty)
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

    private fun showErrorIllustration(isError: Boolean, message: String?) {
        binding?.let {
            with(it.followersLayoutIllustrations) {
                root.visibility = if (isError) {
                    imgStateIllustration.setImageResource(R.drawable.ic_undraw_server_status)
                    tvTitleIllustration.text =
                        resources.getString(R.string.title_state_server)
                    tvMessageIllustration.text =
                        resources.getString(R.string.message_state_server, message)
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

    private fun showRecyclerView(isRecycler: Boolean) {
        binding?.recyclerviewFollowers?.visibility = if (isRecycler) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.followersProgressBar?.root?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentFollowersBinding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) =
            FollowersFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_USER, user)
                }
            }
    }

}