package com.example.test.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.test.R
import com.example.test.ui.fragment.HomeFragment
import com.example.test.ui.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Created by bruce on 2016/11/1.
 * HomeActivity 主界面
 */
class HomeActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var menuItem: MenuItem? = null
    private var bottomNavigationView: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        bottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView)
        bottomNavigationView!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> viewPager!!.currentItem = 0
                R.id.item_profile -> viewPager!!.currentItem = 1
            }
            false
        }
        viewPager!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (menuItem != null) {
                    menuItem!!.isChecked = false
                } else {
                    bottomNavigationView!!.menu.getItem(0).isChecked = false
                }
                menuItem = bottomNavigationView!!.menu.getItem(position)
                menuItem?.setChecked(true)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        setupViewPager(viewPager)
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment())
        adapter.addFragment(ProfileFragment())
        viewPager!!.adapter = adapter
    }
}