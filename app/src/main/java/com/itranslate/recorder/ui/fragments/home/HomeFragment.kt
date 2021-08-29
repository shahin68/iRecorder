package com.itranslate.recorder.ui.fragments.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.databinding.FragmentHomeBinding
import com.itranslate.recorder.general.Constants.GENERIC_FILE_NAME
import com.itranslate.recorder.general.extensions.onClick
import com.itranslate.recorder.ui.fragments.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareClickListeners()

    }

    /**
     * Function to prepare all click listeners
     */
    private fun prepareClickListeners() {
        binding.btnHomeMic.onClick {
            lifecycleScope.launch {
                viewModel.insertRecord(
                    Record(recordName = GENERIC_FILE_NAME)
                )
                Toast.makeText(requireContext(), "Item added", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnHomeShowRecordings.onClick {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToRecordingsFragment()
            )
        }
    }

}