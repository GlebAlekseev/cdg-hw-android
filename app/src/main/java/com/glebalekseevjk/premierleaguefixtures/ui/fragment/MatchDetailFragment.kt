package com.glebalekseevjk.premierleaguefixtures.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glebalekseevjk.premierleaguefixtures.databinding.FragmentMatchDetailBinding
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.ui.activity.MainActivity

class MatchDetailFragment : Fragment() {
    private var _binding: FragmentMatchDetailBinding? = null
    private val binding: FragmentMatchDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentMatchDetailBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            parseParams()
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
        (activity as MainActivity).supportActionBar?.title = "PLF Match 4 Round 1"
        binding.match = MatchInfo(
            4,
            1,
            "2021-08-14 14:00:00Z",
            "Stamford Bridge",
            "Chelsea",
            "Crystal Palace",
            null,
            3,
            0
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(MATCH_NUMBER)) throw RuntimeException("Param match number is absent")
        val matchNumber = args.getString(MATCH_NUMBER).toString()
    }

    companion object {
        const val MATCH_NUMBER = "match_number"
    }
}