package com.glebalekseevjk.premierleaguefixtures.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
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
import com.glebalekseevjk.premierleaguefixtures.databinding.FragmentSearchListMatchesBinding
import com.glebalekseevjk.premierleaguefixtures.ui.custom.CustomSearchView
import com.glebalekseevjk.premierleaguefixtures.ui.rv.FooterAdapter
import com.glebalekseevjk.premierleaguefixtures.ui.rv.MatchListAdapter
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.ListMatchesViewModel
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.SearchListMatchesViewModel
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.intent.ListMatchesIntent
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.intent.SearchListMatchesIntent
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.LayoutManagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class SearchListMatchesFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentSearchListMatchesBinding? = null
    private val binding: FragmentSearchListMatchesBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchListMatchesBinding is null")
    private lateinit var searchListMatchesViewModel: SearchListMatchesViewModel
    private lateinit var matchListAdapter: MatchListAdapter
    private val navController: NavController by lazy { findNavController() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.createSearchListMatchesFragmentSubcomponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        searchListMatchesViewModel =
            ViewModelProvider(this, viewModelFactory)[SearchListMatchesViewModel::class.java]
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchListMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        initDataBinding()
        setupRecyclerView()
        setupToolbar(savedInstanceState)
        observeViewModel()
        startAction(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initDataBinding() {
        binding.searchListMatchesViewModel = searchListMatchesViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupRecyclerView() {
        matchListAdapter = MatchListAdapter().apply {
            viewType = when (searchListMatchesViewModel.layoutManagerState.value) {
                LayoutManagerState.ViewTypeGrid -> MatchListAdapter.VIEW_TYPE_GRID
                LayoutManagerState.ViewTypeList -> MatchListAdapter.VIEW_TYPE_LIST
            }
        }
        binding.matchListRv.adapter = matchListAdapter
        with(binding.matchListRv.layoutManager as GridLayoutManager) {
            spanCount = 2
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (matchListAdapter.getItemViewType(position)) {
                        MatchListAdapter.VIEW_TYPE_GRID -> 1
                        else -> 2
                    }

                }
            }
        }
        matchListAdapter.openMatchDetailClickListener = { matchNumber ->
            navigateToMatchDetailFragment(matchNumber)
        }
        matchListAdapter.addLoadStateListener { combinedLoadState ->
            lifecycleScope.launch {
                when (combinedLoadState.refresh) {
                    is LoadState.NotLoading -> {
                        if (matchListAdapter.itemCount == 0) {
                            searchListMatchesViewModel.userIntent
                                .send(SearchListMatchesIntent.SetNotFoundState)
                        } else {
                            searchListMatchesViewModel.userIntent
                                .send(SearchListMatchesIntent.SetIdleState)
                        }
                    }

                    is LoadState.Loading -> {
                        searchListMatchesViewModel.userIntent
                            .send(SearchListMatchesIntent.SetLoading)
                    }

                    is LoadState.Error -> {
                        if (matchListAdapter.itemCount == 0) {
                            searchListMatchesViewModel.userIntent
                                .send(SearchListMatchesIntent.SetNotFoundState)
                        } else {
                            searchListMatchesViewModel.userIntent
                                .send(SearchListMatchesIntent.SetIdleState)
                        }
                    }
                }
            }
        }
    }

    private fun navigateToMatchDetailFragment(matchNumber: Int) {
        if (checkCurrentDestination()) {
            val action =
                SearchListMatchesFragmentDirections.actionSearchListMatchesFragmentToMatchDetailFragment(
                    matchNumber
                )
            navController.navigate(action)
        }
    }

    private fun checkCurrentDestination(): Boolean {
        return navController.currentDestination?.id == R.id.searchListMatchesFragment
    }

    private fun setupToolbar(savedInstanceState: Bundle?) {
        val toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.match_search_list_menu)
        val searchViewMenuItem = toolbar.menu.findItem(R.id.menu_search)
        with(searchViewMenuItem.actionView as CustomSearchView) {
            setOnSearchViewCollapsedListener {
                navController.popBackStack()
            }
            setOnQueryTextFocusChangeListener { view, hasFocus ->
                if (savedInstanceState == null) {
                    if (hasFocus) {
                        openKeyBoard(view.findFocus())
                    }
                }
            }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let {
                        if (it.isNotEmpty() && it != searchListMatchesViewModel.teamName.value) {
                            lifecycleScope.launch {
                                searchListMatchesViewModel.userIntent.send(
                                    SearchListMatchesIntent.SetTeamName(
                                        newText
                                    )
                                )
                                matchListAdapter.refresh()
                            }
                        }
                    }
                    return true
                }
            })
        }
        searchViewMenuItem.expandActionView()
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_change_view_type -> {
                    lifecycleScope.launch {
                        searchListMatchesViewModel.userIntent
                            .send(SearchListMatchesIntent.ToggleLayoutManagerState { drawable ->
                            menuItem.icon = AppCompatResources.getDrawable(
                                requireContext(),
                                drawable
                            )
                        })
                    }
                    true
                }

                R.id.menu_search -> {
                    true
                }
                else -> false
            }
        }
    }

    private fun openKeyBoard(view: View) {
        val inputMethodManager: InputMethodManager? =
            ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
        inputMethodManager?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            searchListMatchesViewModel.pagingListMatches.collect {
                matchListAdapter.submitData(it)
            }
        }
        lifecycleScope.launch {
            searchListMatchesViewModel.layoutManagerState.collect { layoutManagerState ->
                when (layoutManagerState) {
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
}