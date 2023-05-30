package com.codegeniuses.estetikin.ui.modul

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codegeniuses.estetikin.databinding.FragmentModulBinding

class ModulFragment : Fragment() {

    private lateinit var binding: FragmentModulBinding
    private val modulViewModel: ModulViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModulBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}