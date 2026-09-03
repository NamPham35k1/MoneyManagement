package com.screenguard.privacy.screenfilter.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.amazic.library.Utils.RemoteConfigHelper
import com.amazic.library.ads.admob.Admob
import com.amazic.library.ads.admob.AdmobApi
import com.amazic.library.ads.app_open_ads.AppOpenManager
import com.amazic.library.ads.banner_ads.BannerBuilder
import com.amazic.library.ads.banner_ads.BannerManager
import com.amazic.library.ads.callback.InterCallback
import com.amazic.library.ads.callback.RewardedCallback
import com.amazic.library.ads.collapse_banner_ads.CollapseBannerBuilder
import com.amazic.library.ads.collapse_banner_ads.CollapseBannerManager
import com.amazic.library.ads.inter_ads.InterManager
import com.amazic.library.ads.native_ads.NativeBuilder
import com.amazic.library.ads.native_ads.NativeManager
import com.amazic.library.ads.reward_ads.RewardManager
import com.google.android.play.core.install.model.InstallStatus
import com.screenguard.privacy.screenfilter.custome_view.LoadingView
import com.screenguard.privacy.screenfilter.extention.hideNavigation
import com.screenguard.privacy.screenfilter.extention.tap
import com.screenguard.privacy.screenfilter.utils.ViewModelFactory
import com.screenguard.privacy.screenfilter.utils.Constants
import com.screenguard.privacy.screenfilter.utils.SystemUtil
import com.screenguard.privacy.screenfilter.R
import com.screenguard.privacy.screenfilter.share_pref.SharePrefApp
import com.screenguard.privacy.screenfilter.ui.defaut_ui.no_internet.NetworkReceiver
import com.screenguard.privacy.screenfilter.ui.defaut_ui.no_internet.NoInternetActivity
import com.screenguard.privacy.screenfilter.ui.defaut_ui.splash.SplashActivity
import com.screenguard.privacy.screenfilter.ui.defaut_ui.welcome_back.WelcomeBackActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity<VM : ViewModel, T : ViewBinding> : AppCompatActivity() {

    abstract fun createViewModel(): Class<VM>
    abstract fun getViewDataBinding(): T
    abstract fun initView()
    abstract fun bindViewModel()
    open fun onBackPressCustom() {}
    lateinit var viewModel: VM
    lateinit var binding: T

    private val TAG = "BaseActivity"
    private var currentApiVersion = 0
    private var networkReceiver: NetworkReceiver? = null

    private lateinit var loadingDialog: Dialog

    @Inject
    lateinit var sharePrefApp: SharePrefApp

    override fun onCreate(savedInstanceState: Bundle?) {
        SystemUtil.setLocale(this)
        hideNavigationBar()

        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelFactory.Factory)[createViewModel()]

          networkReceiver = NetworkReceiver {
              if (!it) {
                  showActivityCustom(NoInternetActivity::class.java, null)
              } else {
                  if (this is NoInternetActivity && intent?.extras?.getString(Constants.ScreenKey.SCREEN) != Constants.ScreenKey.SPLASH_ACTIVITY) {
                      finish()
                  } else if (this is NoInternetActivity && intent?.extras?.getString(Constants.ScreenKey.SCREEN) == Constants.ScreenKey.SPLASH_ACTIVITY) {
                      val myIntent = Intent(this, SplashActivity::class.java)
                      myIntent.flags =
                          Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                      startActivity(myIntent)
                  }
              }
          }
          if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
              registerReceiver(
                  networkReceiver,
                  IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"),
                  RECEIVER_EXPORTED
              )
          } else {
              registerReceiver(networkReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
          }

        onBackPressedDispatcher.addCallback(this) {
            handleBackLogic()
        }
        initView()
        bindViewModel()
        baseView()

    }
    private fun handleBackLogic() {
        onBackPressCustom()

        val icBack = binding.root.findViewById<View>(R.id.icBack)
        if (icBack != null) {
            icBack.performClick()
        } else {
            onBackPressedSystem()
        }
    }

    open fun showLoading() {
        if (::loadingDialog.isInitialized.not())
            loadingDialog = LoadingView.Companion.createDialogLoading(this)
        if (loadingDialog.isShowing.not())
            loadingDialog.show()
    }

    open fun hideLoading() {
        if (::loadingDialog.isInitialized && loadingDialog.isShowing)
            loadingDialog.dismiss()
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
        SplashActivity.Companion.installStateUpdatedListener?.let { SplashActivity.Companion.appUpdateManager?.unregisterListener(it) }
    }

    override fun onResume() {
        super.onResume()
        SplashActivity.Companion.installStateUpdatedListener?.let { SplashActivity.Companion.appUpdateManager?.registerListener(it) }
        SplashActivity.Companion.appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo ->

            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                SplashActivity.Companion.appUpdateManager?.completeUpdate()
            }
        }
        lifecycleScope.launch {
            delay(250)
            if (Admob.getInstance().checkCondition(this@BaseActivity, Constants.RemoteKeys.resume_wb) &&
                this@BaseActivity !is SplashActivity &&
                this@BaseActivity !is WelcomeBackActivity
            ) {
                AppOpenManager.getInstance().enableAppResumeWithActivity(javaClass)
            }
        }
    }

    fun showActivity(activity: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, activity)
        intent.putExtras(bundle ?: Bundle())
        startActivity(intent)
    }

    fun showActivityCustom(activity: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, activity)
        intent.putExtras(bundle ?: Bundle())
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    private fun hideNavigationBar() {
        enableEdgeToEdge()
        window.hideNavigation()
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this@BaseActivity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun baseView() {
        val icBack = binding.root.findViewById<View>(R.id.icBack)
        icBack?.tap { finish() }
//        val onBackPressedCallback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                onBackPressCustom()
//                icBack?.performClick()
//            }
//        }
//        onBackPressedDispatcher.addCallback(this@BaseActivity, onBackPressedCallback)

        //rating, first open
//        if (this is MainActivity) {
//            sharePrefApp.increaseCountRatingAppear()
//            if (sharePrefApp.isFirstOpenApp) sharePrefApp.isFirstOpenApp = false
//        }

    }
    open fun onBackPressedSystem() {
       finish()
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let {
            SystemUtil.setLocale(it)
        })
    }

}