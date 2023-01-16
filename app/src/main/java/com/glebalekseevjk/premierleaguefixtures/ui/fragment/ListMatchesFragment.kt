package com.glebalekseevjk.premierleaguefixtures.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.appComponent
import com.glebalekseevjk.premierleaguefixtures.databinding.FragmentListMatchesBinding
import com.glebalekseevjk.premierleaguefixtures.ui.rv.FooterAdapter
import com.glebalekseevjk.premierleaguefixtures.ui.rv.MatchListAdapter
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.ListMatchesViewModel
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.intent.ListMatchesIntent
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.LayoutManagerState
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

    private lateinit var matchListAdapter: MatchListAdapter
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
        initDataBinding()
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

    private fun initDataBinding() {
        binding.listMatchesViewModel = listMatchesViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupRecyclerView() {
        matchListAdapter = MatchListAdapter().apply {
            viewType = when (listMatchesViewModel.layoutManagerState.value) {
                LayoutManagerState.ViewTypeGrid -> MatchListAdapter.VIEW_TYPE_GRID
                LayoutManagerState.ViewTypeList -> MatchListAdapter.VIEW_TYPE_LIST
            }
        }
        val footerAdapter = FooterAdapter({ matchListAdapter.retry() }) {
            lifecycleScope.launch {
                listMatchesViewModel.userIntent.send(ListMatchesIntent.EnableCacheMode)
            }
        }
        binding.matchListRv.adapter = matchListAdapter.withLoadStateFooter(footerAdapter)
        with(binding.matchListRv.layoutManager as GridLayoutManager) {
            spanCount = 2
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == matchListAdapter.itemCount && footerAdapter.itemCount > 0) 2 else
                        when (matchListAdapter.getItemViewType(position)) {
                            MatchListAdapter.VIEW_TYPE_GRID -> 1
                            else -> 2
                        }
                }
            }
        }
        matchListAdapter.openMatchDetailClickListener = { matchNumber ->
            navigateToMatchDetailFragment(matchNumber)
        }
        matchListAdapter.addLoadStateListener {
            lifecycleScope.launch {
                when (it.refresh) {
                    is LoadState.NotLoading -> {
                        listMatchesViewModel.userIntent.send(ListMatchesIntent.SetIdleState)
                    }
                    is LoadState.Loading -> {
                    }
                    is LoadState.Error -> {
                        if (matchListAdapter.itemCount == 0) {
                            listMatchesViewModel.userIntent.send(ListMatchesIntent.SetNotFoundState)
                        } else {
                            listMatchesViewModel.userIntent.send(ListMatchesIntent.SetIdleState)
                        }
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
            listMatchesViewModel.pagingListMatches.collect {
                matchListAdapter.submitData(it)
            }
        }
        lifecycleScope.launch {
            listMatchesViewModel.layoutManagerState.collect {
                when (it) {
                    LayoutManagerState.ViewTypeGrid -> {
                        binding.matchListRv.post {
                            matchListAdapter.viewType =
                                MatchListAdapter.VIEW_TYPE_GRID
                            refreshVisibleRecyclerViewItems(
                                matchListAdapter,
                                binding.matchListRv
                            )
                        }
                    }
                    LayoutManagerState.ViewTypeList -> {
                        binding.matchListRv.post {
                            matchListAdapter.viewType =
                                MatchListAdapter.VIEW_TYPE_LIST
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

    private fun refreshVisibleRecyclerViewItems(
        adapter: MatchListAdapter,
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

    private fun initListeners() {
        binding.matchInfoListUpSrl.setOnRefreshListener {
            matchListAdapter.refresh()
            binding.matchInfoListUpSrl.isRefreshing = false
        }
        binding.useCacheBtn.setOnClickListener {
            lifecycleScope.launch {
                listMatchesViewModel.userIntent.send(ListMatchesIntent.EnableCacheMode)
                matchListAdapter.retry()
            }
        }
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

    private fun navigateToMatchDetailFragment(matchNumber: Int) {
        if (checkCurrentDestination()) {
            val action =
                ListMatchesFragmentDirections.actionListMatchesFragmentToMatchDetailFragment(
                    matchNumber
                )
            navController.navigate(action)
        }
    }

    private fun navigateToSearchListMatchesFragment() {
        if (checkCurrentDestination()) {
            val action =
                ListMatchesFragmentDirections.actionListMatchesFragmentToSearchListMatchesFragment()
            navController.navigate(action)
        }
    }

    private fun checkCurrentDestination(): Boolean {
        return navController.currentDestination?.id == R.id.listMatchesFragment
    }
}