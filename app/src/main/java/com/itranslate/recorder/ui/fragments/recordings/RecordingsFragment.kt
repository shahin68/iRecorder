package com.itranslate.recorder.ui.fragments.recordings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.itranslate.recorder.databinding.FragmentRecordingsBinding
import com.itranslate.recorder.general.extensions.addEnterExitSharedAxisTransition
import com.itranslate.recorder.ui.fragments.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordingsFragment :
    BaseFragment<FragmentRecordingsBinding>(FragmentRecordingsBinding::inflate) {

    private val viewModel: RecordingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addEnterExitSharedAxisTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}