package com.glebalekseevjk.premierleaguefixtures.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.glebalekseevjk.premierleaguefixtures.databinding.FragmentMatchDetailBinding
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.ui.activity.MainActivity
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.MatchDetailViewModel
import kotlinx.coroutines.launch

class MatchDetailFragment : Fragment() {
    private var _binding: FragmentMatchDetailBinding? = null
    private val binding: FragmentMatchDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentMatchDetailBinding is null")

    private val matchDetailViewModel: MatchDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            parseParams()
        }
    }

    private fun observeCurrentMatchInfo(){
        lifecycleScope.launch{
            matchDetailViewModel.observeCurrentMatchList().collect{
                val match = it.firstOrNull() ?: throw RuntimeException("Match with current number is not exist")
                binding.match = match
                (activity as MainActivity).supportActionBar?.title = "PLF Match ${match.matchNumber} Round ${match.roundNumber}"
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.match = MatchInfo.MOCK
        observeCurrentMatchInfo()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseParams() {
        val args = requireArguments()
        println(args.toString())
        if (!args.containsKey(MATCH_NUMBER)) throw RuntimeException("Param match number is absent")
        matchDetailViewModel.currentMatchNumber.value = args.getInt(MATCH_NUMBER)
    }

    companion object {
        const val MATCH_NUMBER = "match_number"
    }
}