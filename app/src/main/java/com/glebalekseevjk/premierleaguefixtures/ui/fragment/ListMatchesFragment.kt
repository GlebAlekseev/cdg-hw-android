package com.glebalekseevjk.premierleaguefixtures.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.databinding.FragmentListMatchesBinding
import com.glebalekseevjk.premierleaguefixtures.ui.activity.MainActivity
import com.glebalekseevjk.premierleaguefixtures.ui.rv.MatchListAdapter
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.ListMatchesViewModel
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.ListMatchesViewModel.Companion.LayoutManagerViewType.VIEW_TYPE_GRID
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.ListMatchesViewModel.Companion.LayoutManagerViewType.VIEW_TYPE_LIST
import kotlinx.coroutines.launch

class ListMatchesFragment : Fragment() {
    private var _binding: FragmentListMatchesBinding? = null
    private val binding: FragmentListMatchesBinding
        get() = _binding ?: throw RuntimeException("FragmentListMatchesBinding is null")

    private lateinit var matchListAdapter: MatchListAdapter
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
        (activity as MainActivity).supportActionBar?.title = resources.getString(R.string.app_name)
        setupRecyclerView()
        setupMenu()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObservers(){
        lifecycleScope.launch {
            listMatchesViewModel.observeMatchList().collect{
                matchListAdapter.submitList(it)
            }
        }
        lifecycleScope.launch{
            listMatchesViewModel.currentRecyclerLayoutManager.collect{
                when(listMatchesViewModel.currentRecyclerLayoutManager.value) {
                    VIEW_TYPE_LIST -> {
                        binding.matchListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        matchListAdapter.viewType = MatchListAdapter.VIEW_TYPE_LIST
                    }
                    VIEW_TYPE_GRID -> {
                        binding.matchListRv.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL,false)
                        matchListAdapter.viewType = MatchListAdapter.VIEW_TYPE_GRID
                    }
                }
                matchListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupRecyclerView() {
        matchListAdapter = MatchListAdapter()
        binding.matchListRv.adapter = matchListAdapter
        matchListAdapter.openMatchDetailClickListener = { matchNumber ->
            val bundle = bundleOf(
                MatchDetailFragment.MATCH_NUMBER to matchNumber
            )
            findNavController().navigate(R.id.action_listMatchesFragment_to_matchDetailFragment, bundle)
        }
        binding.matchListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setupMenu(){
        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.match_list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_change_view_type ->{
                        when(listMatchesViewModel.currentRecyclerLayoutManager.value){
                            VIEW_TYPE_LIST ->{
                                listMatchesViewModel.currentRecyclerLayoutManager.value = VIEW_TYPE_GRID
                            }
                            VIEW_TYPE_GRID ->{
                                listMatchesViewModel.currentRecyclerLayoutManager.value = VIEW_TYPE_LIST
                            }
                        }
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}