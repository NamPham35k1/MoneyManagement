package com.loan.calculator.loantools.ui.feature.home

import android.Manifest
import android.util.Log
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import com.loan.calculator.loantools.base.BaseActivity
import com.loan.calculator.loantools.custome_view.BottomViewNav
import com.loan.calculator.loantools.extention.clickVibrate
import com.loan.calculator.loantools.R
import com.loan.calculator.loantools.ads.AdsHelper
import com.loan.calculator.loantools.ads.AdsHelper.loadCollapseBanner
import com.loan.calculator.loantools.databinding.ActivityHomeBinding
import com.loan.calculator.loantools.extention.tap
import com.loan.calculator.loantools.service.PanelManager
import com.loan.calculator.loantools.utils.Constants
import com.loan.calculator.loantools.utils.SlideToggleView
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.component1
import kotlin.collections.component2
import android.provider.Settings
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.loan.calculator.loantools.extention.logDebug
import com.loan.calculator.loantools.service.OverlayService
import com.loan.calculator.loantools.service.TiltPrivacyManager
import com.loan.calculator.loantools.service.TiltState
import com.loan.calculator.loantools.ui.feature.SettingActivity
import com.loan.calculator.loantools.ui.feature.how_to_use.HowToUseActivity
import com.loan.calculator.loantools.ui.feature.pattern.PatternActivity
import com.loan.calculator.loantools.ui.feature.pattern.PatternAdapter
import com.loan.calculator.loantools.utils.PatternRepository
import com.loan.calculator.loantools.utils.PrefUtils

@AndroidEntryPoint
class HomeActivity : BaseActivity<HomeVM, ActivityHomeBinding>() {

    override fun createViewModel(): Class<HomeVM> = HomeVM::class.java
    override fun getViewDataBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)

    //    private var collapseBanner: CollapseBannerManager? = null
    private var tiltManager: TiltPrivacyManager? = null
    private lateinit var panelManager: PanelManager
    override fun initView() {
        binding.seekBar.max = 100
        binding.seekBar.progress = 80
        //
        binding.seekBarSensitivity.progress = 23
        binding.tvPercentSentivity.text = "23°"

        tiltManager?.start()

        panelManager = PanelManager.getInstance(this)

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
        logDebug("CheckInToggle","Home ${sharePrefApp.checktoggle}")

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
            if (sharePrefApp.checktoggle) {
                sharePrefApp.switchTurnService = isChecked

                if (isChecked) {
                    if (!canDrawOverlay()) {
                        requestOverlayPermission()
                        binding.switchOverlay.isChecked = false
                    } else {
                        requestNotificationPermission()
                        if (!panelManager.isShowing()) {
                            panelManager.showPanel()
                        }
                        startOverlayService()
                    }
                } else {
                    panelManager.hidePanel()
                    stopService(Intent(this, OverlayService::class.java))
                }
            }
        }

        binding.switchOverlay.setOnCheckedChangeListener(switchListener)

        // TOGGLE MASTER (SlideToggleView)
        toggle.onToggleChanged = { isOn ->
            sharePrefApp.checktoggle = isOn

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

    private fun startOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun requestOverlayPermissionNotification() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
    }

    private fun dataPattern() {
        val data = PatternRepository.getAllPatterns()
        val adapter = PatternHomeAdapter(data) { sharePrefApp.idPattern = it }
        binding.rvPattern.adapter = adapter

        adapter.selectedId = sharePrefApp.idPattern

    }

    override fun onBackPressedSystem() {
        finishAffinity()
    }
}