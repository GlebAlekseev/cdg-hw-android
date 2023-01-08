package com.glebalekseevjk.premierleaguefixtures.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.databinding.FragmentListMatchesBinding
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.ui.rv.MatchListAdapter
import com.glebalekseevjk.premierleaguefixtures.ui.rv.PaginationMatchListAdapter
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.ListMatchesViewModel
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.ListMatchesState.Companion.LayoutManagerViewType.VIEW_TYPE_GRID
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.ListMatchesState.Companion.LayoutManagerViewType.VIEW_TYPE_LIST
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ListMatchesFragment : Fragment() {
    private var _binding: FragmentListMatchesBinding? = null
    private val binding: FragmentListMatchesBinding
        get() = _binding ?: throw RuntimeException("FragmentListMatchesBinding is null")

    private lateinit var matchListAdapter: PaginationMatchListAdapter
    private val listMatchesViewModel: ListMatchesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        observeSubmitListAdapter()
        setupRecyclerView()
        CoroutineScope(Dispatchers.Main).launch {
            listMatchesViewModel.loadNextPage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        matchListAdapter = PaginationMatchListAdapter()
        binding.matchListRv.adapter = matchListAdapter
        matchListAdapter.openMatchDetailClickListener = { matchNumber ->
            navigateToMatchDetailFragment(matchNumber)
        }
        val paginationScrollListener = object :
            PaginationScrollListener(binding.matchListRv.layoutManager as GridLayoutManager) {
            override suspend fun loadMoreItems() {
                println("--------------- loadMoreItems")
                listMatchesViewModel.loadNextPage()
            }

            override fun isLastPage(): Boolean {
                return listMatchesViewModel.currentState.isLastPage
            }

            override fun isLoading(): Boolean {
                return listMatchesViewModel.currentState.isLoading
            }
        }
        binding.matchListRv.addOnScrollListener(paginationScrollListener)
        matchListAdapter.isLoadingAddedListener = { listMatchesViewModel.currentState.isLoading }
    }

    private fun navigateToMatchDetailFragment(matchNumber: Int) {
        val action =
            ListMatchesFragmentDirections.actionListMatchesFragmentToMatchDetailFragment(matchNumber)
        findNavController().navigate(action)
    }

    private fun observeSubmitListAdapter() {
        lifecycleScope.launch {
            listMatchesViewModel.observeState(viewLifecycleOwner) {
                matchListAdapter.submitList(it.listMatches)
                when (it.layoutManagerViewType) {
                    VIEW_TYPE_GRID -> {
                        matchListAdapter.viewType = MatchListAdapter.VIEW_TYPE_GRID
                        (binding.matchListRv.layoutManager as GridLayoutManager).spanCount = 2
                    }
                    VIEW_TYPE_LIST -> {
                        matchListAdapter.viewType = MatchListAdapter.VIEW_TYPE_LIST
                        (binding.matchListRv.layoutManager as GridLayoutManager).spanCount = 1
                    }
                }

            }
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.match_list_menu)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_change_view_type -> {
                    listMatchesViewModel.updateState {
                        it.copy(
                            layoutManagerViewType = when (it.layoutManagerViewType) {
                                VIEW_TYPE_GRID -> {
                                    menuItem.icon = AppCompatResources.getDrawable(
                                        requireContext(),
                                        R.drawable.ic_columns_24
                                    )
                                    VIEW_TYPE_LIST
                                }
                                VIEW_TYPE_LIST -> {
                                    menuItem.icon = AppCompatResources.getDrawable(
                                        requireContext(),
                                        R.drawable.ic_rows_24
                                    )
                                    VIEW_TYPE_GRID
                                }
                            }
                        )
                    }
                    true
                }
                else -> false
            }
        }
    }
}