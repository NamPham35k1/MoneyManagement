package com.screenguard.privacy.screenfilter.ui.defaut_ui.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.amazic.library.Utils.EventTrackingHelper
import com.amazic.library.ads.app_open_ads.AppOpenManager
import com.screenguard.privacy.screenfilter.ads.AdsHelper
import com.screenguard.privacy.screenfilter.ads.NativeType
import com.screenguard.privacy.screenfilter.base.BaseActivity
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.ActivityPermissionBinding
import com.screenguard.privacy.screenfilter.dialog.DialogPer
import com.screenguard.privacy.screenfilter.extention.isTiRaMiSu
import com.screenguard.privacy.screenfilter.extention.openAppPermissionSettings
import com.screenguard.privacy.screenfilter.extention.tap
import com.screenguard.privacy.screenfilter.ui.feature.home.HomeActivity
import com.screenguard.privacy.screenfilter.utils.Constants
import com.screenguard.privacy.screenfilter.utils.PermissionManager
import android.provider.Settings


class PermissionActivity : BaseActivity<CommonVM, ActivityPermissionBinding>() {

    private lateinit var perStorageLauncher: ActivityResultLauncher<Array<String>>
    private var PERMISSION = ""

    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java

    private lateinit var notificationLauncher: ActivityResultLauncher<String>
    override fun getViewDataBinding(): ActivityPermissionBinding =
        ActivityPermissionBinding.inflate(layoutInflater)

    override fun initView() {
        EventTrackingHelper.logEvent(this, Constants.TrackingKeys.permission_open)
        AdsHelper.loadNative(
            this,
            this,
            frAds = binding.frAds,
            remoteKeyMain = Constants.RemoteKeys.native_per,
            remoteKeySecondary = Constants.RemoteKeys.native_per,
            type = NativeType.NATIVE_LARGE_ABOVE
        )

        binding.swOverlay.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!canDrawOverlay()) {
                    requestOverlayPermission()
                }
            }
        }
        notificationLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            updatePermissionSwitchStates()

            if (!isGranted && !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showPermissionSettingsDialog()
            }
        }
        binding.sWNotification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                requestNotificationPermission()
            }
        }
        updatePermissionSwitchStates()



        binding.tvContinue.tap {
            EventTrackingHelper.logEvent(this, Constants.TrackingKeys.permission_continue_click)
            showActivity(HomeActivity::class.java, null)
            finishAffinity()
        }


    }



    private fun showPermissionSettingsDialog() {
        val dialog = DialogPer {
            openAppPermissionSettings()
        }
        dialog.show(supportFragmentManager, "dialog_permission_settings")
    }

    override fun onResume() {
        super.onResume()
        updatePermissionSwitchStates()
        AppOpenManager.getInstance().enableAppResumeWithActivity(PermissionActivity::class.java)

    }


    private fun updatePermissionSwitchStates() {

        val isOverlay = Settings.canDrawOverlays(this)

        val isNotification =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED
            } else true

        binding.swOverlay.isChecked = isOverlay
        binding.swOverlay.isEnabled = !isOverlay

        binding.sWNotification.isChecked = isNotification
        binding.sWNotification.isEnabled = !isNotification
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivity(intent)
    }
    private fun canDrawOverlay(): Boolean {
        return Settings.canDrawOverlays(this)
    }
    override fun bindViewModel() {}

    override fun onBackPressCustom() {
        finishAffinity()
    }
}