package com.glebalekseevjk.premierleaguefixtures.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.appComponent
import com.glebalekseevjk.premierleaguefixtures.databinding.FragmentMatchDetailBinding
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.MatchDetailViewModel
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.MatchDetailState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MatchDetailFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentMatchDetailBinding? = null
    private val binding: FragmentMatchDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentMatchDetailBinding is null")
    private lateinit var matchDetailViewModel: MatchDetailViewModel
    private val navController: NavController by lazy { findNavController() }
    private val args: MatchDetailFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.createMatchDetailFragmentSubcomponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        matchDetailViewModel =
            ViewModelProvider(this, viewModelFactory)[MatchDetailViewModel::class.java]
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
        initDataBinding()
        initToolBar()
        observeViewModel()
        startAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseParams() {
        matchDetailViewModel.setCurrentMatchInfo(args.matchNumber)
    }

    private fun initDataBinding() {
        binding.matchDetailViewModel = matchDetailViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initToolBar() {
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            matchDetailViewModel.state.collect { matchDetailState ->
                when (matchDetailState) {
                    is MatchDetailState.Error<*> -> {
                        Toast.makeText(
                            requireContext(),
                            getString(matchDetailState.errorMessage as? Int ?: R.string.error_text),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun startAction() {
        lifecycleScope.launch {
            delay(100)
            startPostponedEnterTransition()
        }
    }
}