package com.rczs.gis.base

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.test.R
import com.example.test.utils.showToast
import com.gyf.immersionbar.ktx.immersionBar
import com.rczs.gis.eventbus.BindEventBus
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.ParameterizedType

/**
 * @CreateDate:     2021/3/18
 * @Author:         LSX
 * @Description:
 */
@Suppress("COMPATIBILITY_WARNING", "UNCHECKED_CAST")
abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity(){
    lateinit var viewModel: VM
    private val REQUIRED_PERMISSION_LIST = arrayOf<String>(
        Manifest.permission.VIBRATE,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.WAKE_LOCK,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )
    protected var missingPermission = ArrayList<String>()
    protected val REQUEST_PERMISSION_CODE = 12345
    protected var TAG: String = this::class.java.name

    protected abstract fun setResLayout(): Int

    override fun getResources(): Resources {
        val res = super.getResources()
        if (res != null) { //非默认值
            val configuration = res.configuration
            if (configuration != null && configuration.fontScale != 1.0f) {
                configuration.fontScale = 1.0f
                res.updateConfiguration(configuration, res.displayMetrics);
            }
        }
        return res
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setResLayout())
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        // 若使用BindEventBus注解，则绑定EventBus
        if (javaClass.isAnnotationPresent(BindEventBus::class.java)) {
            EventBus.getDefault().register(this)
        }
        //沉浸式处理
        immersionBar {
            //底部导航栏颜色
            navigationBarColor(R.color.black)
            //状态栏字体是深色，不写默认为亮色
            statusBarDarkFont(true)
        }
        createViewModel()
        //注册 UI事件
        registerDefUIChange()
        initView()
        initData()
        liveDataObserve()
        initOnClick()
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
        viewModel.toastStr.observe(this) {
            showToast(it)
        }
        viewModel.isFinishAct.observe(this) {
            if (it) {
                finish()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        // 若使用BindEventBus注解，则解绑EventBus
        if (javaClass.isAnnotationPresent(BindEventBus::class.java)) {
            EventBus.getDefault().unregister(this)
        }
    }

    protected abstract fun initView()

    protected abstract fun initData()

    /**
     *  处理Activity中liveData数据处理
     *  viewModel.XXX.observe(this) {  //doAnything }
     */
    open fun liveDataObserve() {}
    open fun initOnClick() {}


    /**
     * Checks if there is any missing permissions, and
     * requests runtime permission if needed.
     */
    public fun checkAndRequestPermissions(): Boolean {
        // Check for permissions
        for (eachPermission in REQUIRED_PERMISSION_LIST) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    eachPermission
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                missingPermission.add(eachPermission)
            }
        }
        // Request for missing permissions
        if (missingPermission.isEmpty()) {

//            startSDKRegistration()
            return true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showToast("Need to grant the permissions!")
            ActivityCompat.requestPermissions(
                this,
                missingPermission.toTypedArray(),
                REQUEST_PERMISSION_CODE
            )
            return false
        }
        return false
    }
}