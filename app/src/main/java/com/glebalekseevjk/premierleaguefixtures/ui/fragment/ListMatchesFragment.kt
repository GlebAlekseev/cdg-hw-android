package com.glebalekseevjk.premierleaguefixtures.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.appComponent
import com.glebalekseevjk.premierleaguefixtures.databinding.FragmentListMatchesBinding
import com.glebalekseevjk.premierleaguefixtures.ui.rv.PaginationMatchListAdapter
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.ListMatchesViewModel
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.ListMatchesState.Companion.LayoutManagerViewType.VIEW_TYPE_GRID
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.ListMatchesState.Companion.LayoutManagerViewType.VIEW_TYPE_LIST
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class ListMatchesFragment : Fragment() {
    private var _binding: FragmentListMatchesBinding? = null
    private val binding: FragmentListMatchesBinding
        get() = _binding ?: throw RuntimeException("FragmentListMatchesBinding is null")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var listMatchesViewModel: ListMatchesViewModel

    private lateinit var matchListAdapter: PaginationMatchListAdapter
    private val navController: NavController by lazy { findNavController() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.createListMatchesFragmentSubcomponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        listMatchesViewModel =
            ViewModelProvider(this, viewModelFactory)[ListMatchesViewModel::class.java]
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        observeSubmitListAdapter()
        setupRecyclerView()
        initListeners()
        if (savedInstanceState == null) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(100)
                startPostponedEnterTransition()
            }
            loadNextPage()
        } else {
            startPostponedEnterTransition()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                R.id.menu_search -> {
                    // TODO()
                    true
                }
                else -> false
            }
        }
    }

    private fun observeSubmitListAdapter() {
        lifecycleScope.launch {
            listMatchesViewModel.observeState(viewLifecycleOwner) {
                matchListAdapter.submitList(it.listMatches)
                when (it.layoutManagerViewType) {
                    VIEW_TYPE_GRID -> {
                        if (matchListAdapter.viewType != PaginationMatchListAdapter.VIEW_TYPE_GRID) {
                            binding.matchListRv.post {
                                (binding.matchListRv.layoutManager as GridLayoutManager).spanCount =
                                    2
                                matchListAdapter.viewType =
                                    PaginationMatchListAdapter.VIEW_TYPE_GRID
                                refreshVisibleRecyclerViewItems(
                                    matchListAdapter,
                                    binding.matchListRv
                                )
                            }
                        }
                    }
                    VIEW_TYPE_LIST -> {
                        if (matchListAdapter.viewType != PaginationMatchListAdapter.VIEW_TYPE_LIST) {
                            binding.matchListRv.post {
                                (binding.matchListRv.layoutManager as GridLayoutManager).spanCount =
                                    1
                                matchListAdapter.viewType =
                                    PaginationMatchListAdapter.VIEW_TYPE_LIST
                                refreshVisibleRecyclerViewItems(
                                    matchListAdapter,
                                    binding.matchListRv
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        matchListAdapter = PaginationMatchListAdapter().apply {
            viewType = when (listMatchesViewModel.currentState.layoutManagerViewType) {
                VIEW_TYPE_GRID -> PaginationMatchListAdapter.VIEW_TYPE_GRID
                VIEW_TYPE_LIST -> PaginationMatchListAdapter.VIEW_TYPE_LIST
            }
        }
        binding.matchListRv.adapter = matchListAdapter
        (binding.matchListRv.layoutManager as GridLayoutManager).apply {
            spanCount = when (listMatchesViewModel.currentState.layoutManagerViewType) {
                VIEW_TYPE_GRID -> 2
                VIEW_TYPE_LIST -> 1
            }
        }
        binding.matchListRv.recycledViewPool.setMaxRecycledViews(
            PaginationMatchListAdapter.VIEW_TYPE_LOADING,
            PaginationMatchListAdapter.LOADING_POOL_SIZE
        )
        matchListAdapter.openMatchDetailClickListener = { matchNumber ->
            navigateToMatchDetailFragment(matchNumber)
        }
        val paginationScrollListener = object :
            PaginationScrollListener() {
            override fun loadMoreItems() {
                loadNextPage()
            }

            override fun isLastPage(): Boolean {
                return listMatchesViewModel.currentState.isLastPage
            }

            override fun isLoading(): Boolean {
                return listMatchesViewModel.currentState.isLoading
            }

            override fun isLoadingPage(): Boolean {
                return listMatchesViewModel.currentState.isLoadingPage
            }
        }
        binding.matchListRv.addOnScrollListener(paginationScrollListener)
        matchListAdapter.isLoadingAddedListener =
            { listMatchesViewModel.currentState.isLoading }
    }

    private fun navigateToMatchDetailFragment(matchNumber: Int) {
        val action =
            ListMatchesFragmentDirections.actionListMatchesFragmentToMatchDetailFragment(matchNumber)
        navController.navigate(action)
    }

    private fun initListeners() {
        binding.matchInfoListUpSrl.setOnRefreshListener {
            lifecycleScope.launch {
                listMatchesViewModel.resetPaginationListHolder {
                    loadNextPage()
                    binding.matchInfoListUpSrl.isRefreshing = false
                }
            }
        }
    }

    private fun loadNextPage() {
        listMatchesViewModel.loadNextPage {
            Log.d("ListMatchesFragment", it)
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshVisibleRecyclerViewItems(
        adapter: PaginationMatchListAdapter,
        recyclerView: RecyclerView
    ) {
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        val visibleItemCount = layoutManager.childCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        adapter.notifyItemRangeChanged(
            firstVisibleItemPosition,
            firstVisibleItemPosition + visibleItemCount
        )
    }
}