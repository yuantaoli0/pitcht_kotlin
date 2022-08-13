package com.example.test.ui

import android.content.Intent
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.test.R
import com.example.test.ui.fragment.HomeFragment
import com.example.test.ui.fragment.ProfileFragment
import com.example.test.utils.AnimationUtils
import com.example.test.viewmodel.HomeVm
import com.example.test.widget.semicircularmenu.SemiCircularRadialMenu
import com.example.test.widget.semicircularmenu.SemiCircularRadialMenuItem
import com.feng.kotlin.AppContext
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rczs.gis.base.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by bruce on 2016/11/1.
 * HomeActivity 主界面
 */
class HomeActivity : BaseActivity<HomeVm>() {
    private var viewPager: ViewPager? = null
    private var menuItem: MenuItem? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private var audio:SemiCircularRadialMenuItem ?=null
    private var video:SemiCircularRadialMenuItem ?=null

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment())
        adapter.addFragment(ProfileFragment())
        viewPager!!.adapter = adapter
    }

    override fun setResLayout(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
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

        //圆形菜单
        val menuWidth: Int = getResources().getDisplayMetrics().widthPixels/5*2
        val params = LinearLayout.LayoutParams(
            menuWidth,LinearLayout.LayoutParams.WRAP_CONTENT
        )
        scrm.layoutParams = params

        audio = SemiCircularRadialMenuItem("audio", getResources().getDrawable(R.mipmap.ic_audio), "audio")
        audio?.iconDimen = menuWidth/4
        video = SemiCircularRadialMenuItem("video", getResources().getDrawable(R.mipmap.ic_video), "video")
        video?.iconDimen = menuWidth/4
        scrm.addMenuItem(audio?.getMenuID(), audio)
        scrm.addMenuItem(video?.getMenuID(), video)
        scrm.setMenuVisible(true)
    }

    override fun initData() {
        viewModel.getUserInfo(AppContext.token)
    }

    override fun liveDataObserve() {
        viewModel.userEntry.observe(this){
            AppContext.user?.name = it.user?.name
            AppContext.user?.username = it.user?.username
            AppContext.user?.email = it.user?.email
            ProfileFragment.refresh()
        }

        viewModel.registerEntry.observe(this){
            ProfileFragment.refresh()
        }
    }

    override fun initOnClick() {
        super.initOnClick()
        fab.setOnClickListener {
            //圆形菜单
            if (ll_container.visibility == View.VISIBLE) {
                AnimationUtils.fadeOut(ll_container)
                fab.visibility = View.GONE
                fab.setImageDrawable(applicationContext.getDrawable(R.mipmap.ic_tri))
                AnimationUtils.fadeIn(fab,500)

            } else {
                AnimationUtils.fadeIn(ll_container)
                fab.visibility = View.GONE
                fab.setImageDrawable(applicationContext.getDrawable(R.mipmap.ic_pause))
                AnimationUtils.fadeIn(fab,500)
            }
        }

        audio?.setOnSemiCircularRadialMenuPressed {
            startActivity(Intent(applicationContext, AudioActivity::class.java))
            fab.visibility = View.GONE
            fab.setImageDrawable(applicationContext.getDrawable(R.mipmap.ic_tri))
            AnimationUtils.fadeIn(fab,500)
            AnimationUtils.fadeOut(ll_container)
        }

        video?.setOnSemiCircularRadialMenuPressed {
            startActivity(Intent(applicationContext, VideoRecyclerviewActivity::class.java))
            fab.visibility = View.GONE
            fab.setImageDrawable(applicationContext.getDrawable(R.mipmap.ic_tri))
            AnimationUtils.fadeIn(fab,500)
            AnimationUtils.fadeOut(ll_container)
        }
    }

    fun logout(){
        finish()
        startActivity(Intent(this,MainActivity::class.java))
    }
}