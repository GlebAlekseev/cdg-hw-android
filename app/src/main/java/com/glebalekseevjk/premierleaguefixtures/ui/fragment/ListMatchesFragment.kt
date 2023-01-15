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
import com.glebalekseevjk.premierleaguefixtures.domain.entity.ErrorType
import com.glebalekseevjk.premierleaguefixtures.ui.listener.PaginationScrollListener
import com.glebalekseevjk.premierleaguefixtures.ui.rv.PaginationMatchListAdapter
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.ListMatchesViewModel
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.intent.ListMatchesIntent
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.LayoutManagerState
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.PaginationListMatchesState
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
        setupRecyclerView()
        setupToolbar()
        observeViewModel()
        initListeners()
        startAction(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startAction(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            lifecycleScope.launch {
                delay(100)
                startPostponedEnterTransition()
            }
        } else {
            startPostponedEnterTransition()
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.match_list_menu)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_change_view_type -> {
                    lifecycleScope.launch {
                        listMatchesViewModel.userIntent.send(ListMatchesIntent.ToggleLayoutManagerState { drawable ->
                            menuItem.icon = AppCompatResources.getDrawable(
                                requireContext(),
                                drawable
                            )
                        })
                    }
                    true
                }
                R.id.menu_search -> {
                    navigateToSearchListMatchesFragment()
                    true
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            listMatchesViewModel.layoutManagerState.collect {
                when (it) {
                    LayoutManagerState.ViewTypeGrid -> {
                        binding.matchListRv.post {
                            (binding.matchListRv.layoutManager as GridLayoutManager).spanCount =
                                if (listMatchesViewModel.paginationListMatchesState.value.isEmpty) 1 else 2
                            matchListAdapter.viewType =
                                PaginationMatchListAdapter.VIEW_TYPE_GRID
                            refreshVisibleRecyclerViewItems(
                                matchListAdapter,
                                binding.matchListRv
                            )
                        }
                    }
                    LayoutManagerState.ViewTypeList -> {
                        binding.matchListRv.post {
                            (binding.matchListRv.layoutManager as GridLayoutManager).spanCount = 1
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
        lifecycleScope.launch {
            listMatchesViewModel.paginationListMatchesState.collect {
                when (it) {
                    is PaginationListMatchesState.Loading, is PaginationListMatchesState.NeedLoading, is PaginationListMatchesState.Finish -> {
                        matchListAdapter.submitList(it.getPaginationMatchList())
                    }
                    is PaginationListMatchesState.Start -> {}
                    is PaginationListMatchesState.Empty -> {}
                }
            }
        }
    }

    private fun setupRecyclerView() {
        matchListAdapter = PaginationMatchListAdapter().apply {
            viewType = when (listMatchesViewModel.layoutManagerState.value) {
                LayoutManagerState.ViewTypeGrid -> PaginationMatchListAdapter.VIEW_TYPE_GRID
                LayoutManagerState.ViewTypeList -> PaginationMatchListAdapter.VIEW_TYPE_LIST
            }
        }
        (binding.matchListRv.layoutManager as GridLayoutManager).apply {
            spanCount = when (listMatchesViewModel.layoutManagerState.value) {
                LayoutManagerState.ViewTypeGrid -> 2
                LayoutManagerState.ViewTypeList -> 1
            }
        }
        binding.matchListRv.adapter = matchListAdapter
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

            override fun isFinish(): Boolean {
                return listMatchesViewModel.paginationListMatchesState.value.isFinish
            }

            override fun isNeedLoading(): Boolean {
                return listMatchesViewModel.paginationListMatchesState.value.isNeedLoading
            }

            override fun isLoading(): Boolean {
                return listMatchesViewModel.paginationListMatchesState.value.isLoading
            }
        }
        binding.matchListRv.addOnScrollListener(paginationScrollListener)
        matchListAdapter.isLoadingAddedListener =
            { listMatchesViewModel.paginationListMatchesState.value.isNeedLoading || listMatchesViewModel.paginationListMatchesState.value.isLoading }
    }

    private fun navigateToMatchDetailFragment(matchNumber: Int) {
        val action =
            ListMatchesFragmentDirections.actionListMatchesFragmentToMatchDetailFragment(matchNumber)
        navController.navigate(action)
    }

    private fun navigateToSearchListMatchesFragment() {
        if(navController.currentDestination?.id == R.id.listMatchesFragment){
            val action =
                ListMatchesFragmentDirections.actionListMatchesFragmentToSearchListMatchesFragment()
            navController.navigate(action)
        }
    }

    private fun initListeners() {
        binding.matchInfoListUpSrl.setOnRefreshListener {
            lifecycleScope.launch {
                listMatchesViewModel.userIntent.send(ListMatchesIntent.ResetPaginationListHolder {
                    loadNextPage()
                    binding.matchInfoListUpSrl.isRefreshing = false
                })
            }
        }
    }

    private fun loadNextPage() {
        lifecycleScope.launch {
            listMatchesViewModel.userIntent.send(ListMatchesIntent.LoadNextPage {
                when (it) {
                    ErrorType.Unknown -> {
                        Log.d("ListMatchesFragment", getString(R.string.unknown_error_text))
                        Toast.makeText(
                            context,
                            getString(R.string.unknown_error_text),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
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