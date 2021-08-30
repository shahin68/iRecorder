package com.itranslate.recorder.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Base Fragment class representing default properties of a fragment
 *
 * View binding field [binding] is being lazy inflated
 * Using [LazyThreadSafetyMode.PUBLICATION] binding can be initialized on multiple threads
 * But, When a value is set by any thread, other threads will use the previously instantiated value
 *
 * Because we know the ViewBinding types, which is held in [T] type parameter, we don't need to use
 * [DataBindingUtil]
 */
abstract class BaseFragment<T: ViewBinding>(bindingInflater: (LayoutInflater) -> T): Fragment() {

    protected val binding by lazy(LazyThreadSafetyMode.PUBLICATION) {
        bindingInflater(layoutInflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

}