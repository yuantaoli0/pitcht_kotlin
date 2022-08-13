package com.rczs.gis.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.test.utils.showToast
import com.rczs.gis.eventbus.BindEventBus
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.ParameterizedType


/**
 * @Description:
 * @CreateDate:     2021/3/17
 * @Author:         LSX
 */

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {


    /**
     * fragment切换的时候的动画
     * 暂时未添加
     */
    companion object {
        var navOptionIn = navOptions {
        }
        var navOptionOut = navOptions {

        }
    }

    lateinit var viewModel: VM
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(setResLayout(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createViewModel()
        // 若使用BindEventBus注解，则绑定EventBus
        if (javaClass.isAnnotationPresent(BindEventBus::class.java)) {
            EventBus.getDefault().register(this)
        }
        initView(savedInstanceState)
        initData()
        registerDefUIChange()
        initOnClick()
        liveDataObserve()
    }

    /**
     * 初始化viewModel
     */
    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(viewModelStore, defaultViewModelProviderFactory)
                .get(tClass) as VM
        }
    }

    private fun registerDefUIChange() {
        viewModel.toastStr.observe(viewLifecycleOwner) {
            requireActivity().showToast(it)
        }
    }


    /**
     *
     * @return Int  xml资源文件
     */
    abstract fun setResLayout(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    protected abstract fun initData()

    /**
     *  Fragment中liveData数据处理
     *  viewModel.XXX.observe(viewLifecycleOwner) {  //doAnything }
     */
    open fun liveDataObserve() {}
    open fun initOnClick() {}

    override fun onDestroy() {
        super.onDestroy()
        // 若使用BindEventBus注解，则解绑EventBus
        if (javaClass.isAnnotationPresent(BindEventBus::class.java)) {
            EventBus.getDefault().unregister(this)
        }
    }

    /**
     * 切换fragment
     */
    protected fun goFragment(idRes: Int, bundle: Bundle?) {
        findNavController().navigate(idRes, bundle, navOptionIn)
    }

    /**
     * 切换fragment
     */
    protected fun goFragment(idRes: Int) {
        findNavController().navigate(idRes, null, navOptionIn)
    }

    protected fun goActivity(clazz: Class<*>) {
        requireActivity().startActivity(Intent(requireContext(), clazz))
    }

    /**
     * fragment的返回
     */
    open fun onBackPressed() {
        requireActivity().onBackPressed()
    }

}