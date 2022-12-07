package com.example.premierleaguefixtures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.premierleaguefixtures.databinding.FragmentWhiteBinding

class WhiteFragment : Fragment() {
    private var _binding: FragmentWhiteBinding? = null
    private val binding: FragmentWhiteBinding
        get() = _binding ?: throw RuntimeException("FragmentWhiteBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWhiteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextFragmentBtn.setOnClickListener {
            launchFragment(BlackFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // parentFragmentManager - вернет FragmentManager для работы с фрагментами, связанными с активностью текущего фрагмента
    // addToBackStack - добавление транзакции в back stack
    private fun launchFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .add(R.id.main_fcv, fragment)
            .addToBackStack(null)
            .commit()
    }
}