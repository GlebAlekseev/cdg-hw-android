package com.glebalekseevjk.premierleaguefixtures.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.databinding.FragmentListMatchesBinding
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.ui.activity.MainActivity
import com.glebalekseevjk.premierleaguefixtures.ui.rv.MatchListAdapter

class ListMatchesFragment : Fragment() {
    private var _binding: FragmentListMatchesBinding? = null
    private val binding: FragmentListMatchesBinding
        get() = _binding ?: throw RuntimeException("FragmentListMatchesBinding is null")

    private lateinit var matchListAdapter: MatchListAdapter

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
        matchListAdapter.submitList(
            listOf(
                MatchInfo(
                    4,
                    1,
                    "2022-12-18 14:00:00Z",
                    "Stamford Bridge",
                    "Chelsea",
                    "Crystal Palace",
                    null,
                    3,
                    0
                ),
                MatchInfo(
                    5,
                    1,
                    "2021-08-14 14:00:00Z",
                    "Stamford Bridge",
                    "Chelsea",
                    "Crystal Palace",
                    null,
                    3,
                    0
                ),
                MatchInfo(
                    6,
                    1,
                    "2021-08-14 14:00:00Z",
                    "Stamford Bridge",
                    "Chelsea",
                    "Crystal Palace",
                    null,
                    3,
                    0
                ),
                MatchInfo(
                    7,
                    1,
                    "2021-08-14 14:00:00Z",
                    "Stamford Bridge",
                    "Chelsea",
                    "Crystal Palace",
                    null,
                    3,
                    0
                ),
                MatchInfo(
                    8,
                    1,
                    "2022-08-14 14:00:00Z",
                    "Stamford Bridge",
                    "Chelsea",
                    "Crystal Palace",
                    null,
                    3,
                    0
                ),
                MatchInfo(
                    9,
                    1,
                    "2022-08-14 14:00:00Z",
                    "Stamford Bridge",
                    "Chelsea",
                    "Crystal Palace",
                    null,
                    3,
                    0
                ),
                MatchInfo(
                    10,
                    1,
                    "2021-08-14 14:00:00Z",
                    "Stamford Bridge",
                    "Chelsea",
                    "Crystal Palace",
                    null,
                    3,
                    0
                ),
                MatchInfo(
                    11,
                    1,
                    "2021-08-18 14:00:00Z",
                    "Stamford Bridge",
                    "Chelsea",
                    "Crystal Palace",
                    null,
                    3,
                    0
                ),
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                        if (matchListAdapter.viewType == MatchListAdapter.VIEW_TYPE_LIST) {
                            matchListAdapter.viewType = MatchListAdapter.VIEW_TYPE_GRID
                            binding.matchListRv.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL,false)

                        }else{
                            matchListAdapter.viewType = MatchListAdapter.VIEW_TYPE_LIST
                            binding.matchListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                        }
                        matchListAdapter.notifyDataSetChanged()
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}