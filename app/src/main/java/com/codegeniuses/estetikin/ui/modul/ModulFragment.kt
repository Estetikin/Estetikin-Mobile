package com.codegeniuses.estetikin.ui.modul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.FragmentModulBinding
import com.codegeniuses.estetikin.ui.MainActivity

class ModulFragment : Fragment() {
    private var _binding: FragmentModulBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModulBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setActionBarTitle(getString(R.string.title_module))
    }
}
