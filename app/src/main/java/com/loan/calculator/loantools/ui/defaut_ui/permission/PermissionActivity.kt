package com.loan.calculator.loantools.ui.defaut_ui.permission

import android.Manifest
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.amazic.library.Utils.EventTrackingHelper
import com.amazic.library.ads.app_open_ads.AppOpenManager
import com.loan.calculator.loantools.ads.AdsHelper
import com.loan.calculator.loantools.ads.NativeType
import com.loan.calculator.loantools.base.BaseActivity
import com.loan.calculator.loantools.base.CommonVM
import com.loan.calculator.loantools.databinding.ActivityPermissionBinding
import com.loan.calculator.loantools.dialog.DialogPer
import com.loan.calculator.loantools.extention.isTiRaMiSu
import com.loan.calculator.loantools.extention.openAppPermissionSettings
import com.loan.calculator.loantools.extention.tap
import com.loan.calculator.loantools.ui.feature.home.HomeActivity
import com.loan.calculator.loantools.utils.Constants
import com.loan.calculator.loantools.utils.PermissionManager

class PermissionActivity : BaseActivity<CommonVM, ActivityPermissionBinding>() {

    private lateinit var perStorageLauncher: ActivityResultLauncher<Array<String>>
    private var PERMISSION = ""

    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java

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

        PERMISSION = PermissionManager.PERMISSION_IN_APP[0]
        perStorageLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isGranted = permissions[PERMISSION] ?: false
            binding.swStorage.isChecked = isGranted
            if (isGranted) {
                binding.swStorage.isEnabled = false
            } else {
                if (!PermissionManager.shouldShowRequestPermissionRationale(this, PERMISSION)) {
                    showPermissionSettingsDialog()
                }
            }
        }

        updatePermissionSwitchStates()

        binding.swStorage.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                handlePermissionRequest(PERMISSION, perStorageLauncher)
                EventTrackingHelper.logEventWithAParam(this, Constants.TrackingKeys.permission_allow_click, "per_name", "READ_EXTERNAL_STORAGE")
                AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity::class.java)

            }
        }

        binding.tvContinue.tap {
            EventTrackingHelper.logEvent(this, Constants.TrackingKeys.permission_continue_click)
            showActivity(HomeActivity::class.java, null)
            finishAffinity()
        }


    }

    private fun handlePermissionRequest(permission: String, launcher: ActivityResultLauncher<Array<String>>) {
        when {
            PermissionManager.hasPermission(permission, this) -> {
            }

            PermissionManager.shouldShowRequestPermissionRationale(this, permission) -> {
                showPermissionSettingsDialog()
            }

            else -> {
                PermissionManager.requestAPermission(this, permission, launcher)
            }
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
        val isStorage =
            if (isTiRaMiSu()) {
                PermissionManager.hasPermission(Manifest.permission.READ_MEDIA_VIDEO, this)
            } else {
                PermissionManager.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)
            }
        binding.swStorage.isChecked = isStorage
        binding.swStorage.isEnabled = !isStorage
    }

    override fun bindViewModel() {}

    override fun onBackPressCustom() {
        finishAffinity()
    }
}