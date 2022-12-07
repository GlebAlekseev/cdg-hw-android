package com.example.premierleaguefixtures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.premierleaguefixtures.databinding.FragmentBlackBinding

class BlackFragment : Fragment() {
    private var _binding: FragmentBlackBinding? = null
    private val binding: FragmentBlackBinding
        get() = _binding ?: throw RuntimeException("FragmentBlackBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextFragmentBtn.setOnClickListener {
            launchFragment(WhiteFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .add(R.id.main_fcv, fragment)
            .addToBackStack(null)
            .commit()
    }
}