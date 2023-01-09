package com.glebalekseevjk.premierleaguefixtures.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.glebalekseevjk.premierleaguefixtures.databinding.FragmentMatchDetailBinding
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.ui.activity.MainActivity
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.ListMatchesViewModel
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.MatchDetailViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MatchDetailFragment : Fragment() {
    private var _binding: FragmentMatchDetailBinding? = null
    private val binding: FragmentMatchDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentMatchDetailBinding is null")
    private val navController: NavController by lazy { findNavController() }
    private val args: MatchDetailFragmentArgs by navArgs()
    private val matchDetailViewModel: MatchDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) parseParams()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        binding.matchDetailViewModel = matchDetailViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        lifecycleScope.launch{
            delay(100)
            startPostponedEnterTransition()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseParams() {
        matchDetailViewModel.setCurrentMatchInfo(args.matchNumber)
    }

    private fun initToolBar(){
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }
}