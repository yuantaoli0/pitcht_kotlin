package com.example.test.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rczs.gis.base.BaseFragment
import java.util.*

/**
 * Created by bruce on 2016/11/1.
 * ViewPagerAdapter
 */
class ViewPagerAdapter(manager: FragmentManager?) : FragmentPagerAdapter(manager!!) {
    private val mFragmentList: MutableList<Fragment> = ArrayList<Fragment>()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    private val count: Int = mFragmentList.size
    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }
}