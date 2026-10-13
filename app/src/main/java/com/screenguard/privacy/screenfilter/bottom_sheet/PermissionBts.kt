package com.screenguard.privacy.screenfilter.bottom_sheet

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.vifarmer.ads.lib.ads.app_open_ads.AppOpenManager
import com.screenguard.privacy.screenfilter.base.BaseBottomSheetFragment
import com.screenguard.privacy.screenfilter.dialog.DialogPer
import com.screenguard.privacy.screenfilter.extention.clickWithBounce
import com.screenguard.privacy.screenfilter.extention.gone
import com.screenguard.privacy.screenfilter.extention.grantOverLayPer
import com.screenguard.privacy.screenfilter.extention.grantOverlayPer
import com.screenguard.privacy.screenfilter.extention.isQPlus
import com.screenguard.privacy.screenfilter.extention.isTiRaMiSu
import com.screenguard.privacy.screenfilter.extention.openAppPermissionSettings
import com.screenguard.privacy.screenfilter.extention.visible
import com.screenguard.privacy.screenfilter.utils.PermissionManager
import com.screenguard.privacy.screenfilter.databinding.LayoutBtsPerBinding
import com.screenguard.privacy.screenfilter.ui.feature.main.MainActivity
import kotlin.apply
import kotlin.collections.all
import kotlin.collections.any
import kotlin.collections.first
import kotlin.collections.firstOrNull
import kotlin.collections.forEach
import kotlin.collections.isEmpty
import kotlin.collections.isNotEmpty
import kotlin.jvm.java

class PermissionBts(
    private val context: Context,
) : BaseBottomSheetFragment<LayoutBtsPerBinding>(true) {

    private lateinit var notificationLauncher: ActivityResultLauncher<String>

    private var hasShownDialog = false

    private val dialog by lazy {
        DialogPer {
            AppOpenManager.getInstance()
                .disableAppResumeWithActivity(MainActivity::class.java)
            context.openAppPermissionSettings()
        }
    }

    override fun initView() {

        notificationLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            updateSwitch()

            if (!isGranted && !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showDialog()
            }
        }

        binding.swOverlay.setOnClickListener {
            if (binding.swOverlay.isChecked) {

                if (!Settings.canDrawOverlays(context)) {
                    requestOverlayPermission()
                }
            }
        }

        binding.swNotification.setOnClickListener {
            if (binding.swNotification.isChecked) {
                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        binding.tvContinue.clickWithBounce {
            dismiss()
        }

        updateSwitch()
    }

    private fun updateSwitch() {
        val isOverlay = Settings.canDrawOverlays(context)

        val isNoti =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requireContext().checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED
            } else true

        binding.swOverlay.isChecked = isOverlay
        binding.swOverlay.isEnabled = !isOverlay

        binding.swNotification.isChecked = isNoti
        binding.swNotification.isEnabled = !isNoti
    }


    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${requireContext().packageName}")
        )
        startActivity(intent)
    }


    private fun showDialog() {
        if (hasShownDialog) return
        hasShownDialog = true

        dialog.showDialog(requireActivity().supportFragmentManager)
    }

    override fun onResume() {
        super.onResume()

        val isOverlay = Settings.canDrawOverlays(context)

        if (!isOverlay && hasShownDialog.not()) {
        }

        updateSwitch()
    }

    override fun setViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LayoutBtsPerBinding =
        LayoutBtsPerBinding.inflate(inflater, container, false)
}
