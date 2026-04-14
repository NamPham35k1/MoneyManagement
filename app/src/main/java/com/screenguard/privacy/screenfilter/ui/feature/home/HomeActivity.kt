package com.screenguard.privacy.screenfilter.ui.feature.home

import android.Manifest
import android.util.Log
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import com.screenguard.privacy.screenfilter.base.BaseActivity
import com.screenguard.privacy.screenfilter.custome_view.BottomViewNav
import com.screenguard.privacy.screenfilter.extention.clickVibrate
import com.screenguard.privacy.screenfilter.R
import com.screenguard.privacy.screenfilter.ads.AdsHelper
import com.screenguard.privacy.screenfilter.ads.AdsHelper.loadCollapseBanner
import com.screenguard.privacy.screenfilter.databinding.ActivityHomeBinding
import com.screenguard.privacy.screenfilter.extention.tap
import com.screenguard.privacy.screenfilter.service.PanelManager
import com.screenguard.privacy.screenfilter.utils.Constants
import com.screenguard.privacy.screenfilter.utils.SlideToggleView
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.component1
import kotlin.collections.component2
import android.provider.Settings
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.screenguard.privacy.screenfilter.bottom_sheet.PermissionBts
import com.screenguard.privacy.screenfilter.extention.logDebug
import com.screenguard.privacy.screenfilter.service.OverlayService
import com.screenguard.privacy.screenfilter.service.TiltPrivacyManager
import com.screenguard.privacy.screenfilter.service.TiltState
import com.screenguard.privacy.screenfilter.ui.feature.SettingActivity
import com.screenguard.privacy.screenfilter.ui.feature.SuccessActivity
import com.screenguard.privacy.screenfilter.ui.feature.how_to_use.HowToUseActivity
import com.screenguard.privacy.screenfilter.ui.feature.pattern.PatternActivity
import com.screenguard.privacy.screenfilter.ui.feature.pattern.PatternAdapter
import com.screenguard.privacy.screenfilter.utils.PatternRepository
import com.screenguard.privacy.screenfilter.utils.PrefUtils

@AndroidEntryPoint
class HomeActivity : BaseActivity<HomeVM, ActivityHomeBinding>() {

    override fun createViewModel(): Class<HomeVM> = HomeVM::class.java
    override fun getViewDataBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)

    //    private var collapseBanner: CollapseBannerManager? = null
    private var tiltManager: TiltPrivacyManager? = null
    private var isUpdatingUI = false

    //
    private var guideStep = 0
    private lateinit var panelManager: PanelManager
    override fun initView() {
        //
        if (sharePrefApp.isFirstOpenTapHome) {
            startGuide()
        }
        //
        binding.seekBar.max = 100
        binding.seekBar.progress = 80
        //
        binding.seekBarSensitivity.progress = 23
        binding.tvPercentSentivity.text = "23°"
//
        binding.seekBarControlPanel.progress = sharePrefApp.panelOpacity

        tiltManager?.start()

        panelManager = PanelManager.getInstance(this, sharePrefApp)

        tiltManager = TiltPrivacyManager(this) {

        }
        tiltManager?.tiltMode = TiltPrivacyManager.TiltMode.LEFT

        enableEdgeToEdge()
        onBottomClick()
//        collapseBanner = loadCollapseBanner(
//            this, this,
//            Constants.AdsKey.collapse_home,
//            Constants.RemoteKeys.collapse_home,
//            binding.frAds
//        )
        seekBar()
        dataPattern()
        toggleOverlayPanel()
        logDebug("CheckInToggle", "Home ${sharePrefApp.checktoggle}")

    }

    override fun bindViewModel() {
        binding.tvSeeMore.tap {
            showActivity(PatternActivity::class.java, null)
        }
        binding.icHowToUse.tap {
            showActivity(HowToUseActivity::class.java, null)
        }

        binding.icSetting.tap {
            showActivity(SettingActivity::class.java, null)
        }
    }

    private fun onBottomClick() {
        val toggle = findViewById<SlideToggleView>(R.id.slideToggle)
        toggle.onToggleChanged = { isOn ->
            Log.d("Toggle", "State: $isOn")
        }
    }

    override fun onResume() {
        super.onResume()

        val toggle = findViewById<SlideToggleView>(R.id.slideToggle)

        toggle.setChecked(sharePrefApp.checktoggle)
        //
        binding.switchOverlay.isChecked = sharePrefApp.switchTurnService
        //  disable nếu toggle OFF
        binding.switchOverlay.isEnabled = sharePrefApp.checktoggle

        // check per
        if (hasAllPermission() && sharePrefApp.switchTurnService) {
            isUpdatingUI = true
            binding.switchOverlay.isChecked = true
            isUpdatingUI = false
        }
    }

    private fun seekBar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvPercent.text = "$progress%"
                val alpha = progress / 100f
                panelManager.maxDimAlpha = alpha

                binding.tvPercent.text = "${(alpha * 100).toInt()}%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.seekBarSensitivity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val angle = progress.coerceAtLeast(5)
                binding.tvPercentSentivity.text = "$angle°"
                panelManager.updateAngle(angle.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.seekBarControlPanel.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvPerentOpacity.text = "$progress%"
                panelManager.updatePanelOpacity(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.rgDirection.setOnCheckedChangeListener { _, checkedId ->
            TiltState.mode = when (checkedId) {
                R.id.rbLeft -> TiltPrivacyManager.TiltMode.LEFT
                R.id.rbRight -> TiltPrivacyManager.TiltMode.RIGHT
                else -> TiltPrivacyManager.TiltMode.BOTH
            }
        }

    }

    private fun toggleOverlayPanel() {
        val toggle = findViewById<SlideToggleView>(R.id.slideToggle)

        val switchListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (isUpdatingUI) return@OnCheckedChangeListener
            if (sharePrefApp.checktoggle) {
                if (isChecked) {
                    if (!hasAllPermission()) {
                        PermissionBts(this).show(supportFragmentManager, "PermissionBts")
                        isUpdatingUI = true
                        binding.switchOverlay.isChecked = false
                        isUpdatingUI = false

                        return@OnCheckedChangeListener
                    }
                    sharePrefApp.switchTurnService = true

                    if (!panelManager.isShowing()) {
                        panelManager.showPanel()
                    }

                    startOverlayService()

                } else {
                    sharePrefApp.switchTurnService = false

                    panelManager.hidePanel()
                    stopService(Intent(this, OverlayService::class.java))


                }
            }
        }

        binding.switchOverlay.setOnCheckedChangeListener(switchListener)

        // TOGGLE MASTER (SlideToggleView)
        toggle.onToggleChanged = { isOn ->
            sharePrefApp.checktoggle = isOn
            val bundle = Bundle().apply {
                putString("KEY_TURN", if (isOn) "TURN_ON" else "TURN_OFF")
            }

            showActivity(SuccessActivity::class.java, bundle)
            if (!isOn) {
                sharePrefApp.switchTurnService = false

                binding.switchOverlay.setOnCheckedChangeListener(null)
                binding.switchOverlay.isChecked = false
                binding.switchOverlay.isEnabled = false
                binding.switchOverlay.setOnCheckedChangeListener(switchListener)

                panelManager.hidePanel()
                stopService(Intent(this, OverlayService::class.java))
            } else {
                binding.switchOverlay.isEnabled = true
            }
        }
    }


    private fun canDrawOverlay(): Boolean {
        return Settings.canDrawOverlays(this)
    }

    private fun startOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }


    private fun dataPattern() {
        val data = PatternRepository.getAllPatterns()
        val adapter = PatternHomeAdapter(data) { sharePrefApp.idPattern = it }
        binding.rvPattern.adapter = adapter

        adapter.selectedId = sharePrefApp.idPattern

    }
    private fun hasAllPermission(): Boolean {
        return canDrawOverlay() && hasNotificationPermission()
    }
    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
        } else true
    }
    override fun onBackPressedSystem() {
        finishAffinity()
    }
    //
    private fun startGuide() {
        guideStep = 0

        binding.viewDim.visibility = View.VISIBLE

        showGuideStep()

        binding.viewDim.setOnClickListener {
            guideStep++

            if (guideStep >= 3) {
                hideAllGuide()
                sharePrefApp.isFirstOpenTapHome = false
            } else {
                showGuideStep()
            }
        }
    }
    private fun hideAllGuide() {
        binding.viewDim.visibility = View.GONE

        binding.imghtuse1.visibility = View.GONE
        binding.tvhts1.visibility = View.GONE

        binding.imghtuse2.visibility = View.GONE
        binding.tvhts2.visibility = View.GONE

        binding.imghtuse3.visibility = View.GONE
        binding.tvhts3.visibility = View.GONE
    }
    private fun showGuideStep() {

        hideAllGuide()

        binding.viewDim.visibility = View.VISIBLE

        when (guideStep) {

            0 -> {
                binding.imghtuse1.visibility = View.VISIBLE
                binding.tvhts1.visibility = View.VISIBLE
            }

            1 -> {
                binding.imghtuse2.visibility = View.VISIBLE
                binding.tvhts2.visibility = View.VISIBLE
            }

            2 -> {
                binding.imghtuse3.visibility = View.VISIBLE
                binding.tvhts3.visibility = View.VISIBLE
            }
        }
    }
}
