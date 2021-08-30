package com.itranslate.recorder.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding

/**
 * Base Activity class representing default properties of an activity
 *
 * View binding field [binding] is being lazy inflated
 * Using [LazyThreadSafetyMode.PUBLICATION] binding can be initialized on multiple threads
 * But, When a value is set by any thread, other threads will use the previously instantiated value
 *
 * Because we know the ViewBinding types, which is held in [T] type parameter, we don't need to use
 * [DataBindingUtil]
 */
abstract class BaseActivity<T: ViewBinding>(bindingInflater: (LayoutInflater) -> T): AppCompatActivity() {

    protected val binding by lazy(LazyThreadSafetyMode.PUBLICATION) {
        bindingInflater.invoke(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}