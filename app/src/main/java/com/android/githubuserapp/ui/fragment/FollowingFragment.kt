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
import com.android.githubuserapp.databinding.FragmentFollowingBinding
import com.android.githubuserapp.ui.activity.DetailActivity
import com.android.githubuserapp.utility.Constants.EXTRA_USER
import com.android.githubuserapp.utility.MainState
import com.android.githubuserapp.viewmodel.FollowingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FollowingFragment : Fragment() {

    private lateinit var followingAdapter: GithubAdapter
    private var _fragmentFollowingBinding: FragmentFollowingBinding? = null
    private val binding get() = _fragmentFollowingBinding
    private val followingViewModel by viewModel<FollowingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentFollowingBinding = FragmentFollowingBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observe()

        return binding?.root
    }

    private fun observe() {
        observeQueryLogin()
        observeFollowingData()
        observeState()
    }

    private fun observeState() {
        followingViewModel.state.observe(viewLifecycleOwner, { handleState(it) })
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
                followingViewModel.setFollowingData(user.login)
            }
        }
    }

    private fun observeFollowingData() {
        followingViewModel.listFollowingData.observe(
            viewLifecycleOwner,
            { handleFollowingData(it) })
    }

    private fun handleFollowingData(users: ArrayList<User>?) {
        if (users != null && users.isNotEmpty()) {
            followingAdapter.setListData(users)
            showEmptyIllustration(false)
            showRecyclerView(true)
        } else {
            showRecyclerView(false)
            showEmptyIllustration(true)
        }
    }

    private fun setupRecyclerView() {
        followingAdapter = GithubAdapter()

        binding?.recyclerviewFollowing?.layoutManager = LinearLayoutManager(requireContext())
        binding?.recyclerviewFollowing?.setHasFixedSize(true)
        binding?.recyclerviewFollowing?.adapter = followingAdapter

        followingAdapter.setOnItemClickCallBack(object :
            GithubAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: User) {
                startActivity(Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra(EXTRA_USER, data)
                })
            }
        })
    }

    private fun showEmptyIllustration(isEmpty: Boolean) {
        binding?.let {
            with(it.followingLayoutIllustrations) {
                root.visibility = if (isEmpty) {
                    imgStateIllustration.setImageResource(R.drawable.ic_undraw_empty)
                    tvTitleIllustration.text =
                        resources.getString(R.string.title_following_empty)
                    tvMessageIllustration.text =
                        resources.getString(R.string.message_following_empty)
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

    private fun showErrorIllustration(isError: Boolean, message: String?) {
        binding?.let {
            with(it.followingLayoutIllustrations) {
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
        binding?.recyclerviewFollowing?.visibility = if (isRecycler) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.followingProgressBar?.root?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        followingViewModel.listFollowingData.observe(this, {
            if (it != null) {
                followingAdapter.setListData(it)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentFollowingBinding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_USER, user)
                }
            }
    }
}