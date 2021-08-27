package com.itranslate.recorder.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.itranslate.recorder.databinding.FragmentHomeBinding
import com.itranslate.recorder.ui.fragments.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}