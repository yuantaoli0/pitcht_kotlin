package com.example.test.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by bruce on 2016/11/1.
 * BaseFragment
 */
abstract  class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)
        initData()
        initEvent()
    }

    abstract fun getLayout() : Int

    open fun initView(view:View){}

    open fun initData(){}

    open fun initEvent(){}
}

