package com.rczs.gis.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.entry.LoadState

/**
 * @CreateDate:     2021/3/18
 * @Author:         LSX
 * @Description:
 */
abstract class BaseViewModel : ViewModel() {
    val toastStr by lazy {
        MutableLiveData<String>()
    }
    val loadState by lazy {
        MutableLiveData<LoadState>()
    }
    val isFinishAct by lazy {
        MutableLiveData<Boolean>()
    }


}
