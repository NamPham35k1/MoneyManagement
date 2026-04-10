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
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.loan.calculator.loantools.service.OverlayService

@AndroidEntryPoint
class HomeActivity : BaseActivity<HomeVM, ActivityHomeBinding>() {

    override fun createViewModel(): Class<HomeVM> = HomeVM::class.java
    override fun getViewDataBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
//    private var collapseBanner: CollapseBannerManager? = null

    private lateinit var panelManager: PanelManager
    override fun initView() {
        panelManager = PanelManager(this)
        enableEdgeToEdge()
        onBottomClick()
//        collapseBanner = loadCollapseBanner(
//            this, this,
//            Constants.AdsKey.collapse_home,
//            Constants.RemoteKeys.collapse_home,
//            binding.frAds
//        )
        seekBar()
        toggleOverlayPanel()
    }

    override fun bindViewModel() {

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
    }
    private fun seekBar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvPercent.text = "$progress%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.seekBarSensitivity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvPercentSentivity.text = "$progress°"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.seekBarControlPanel.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvPerentOpacity.text = "$progress%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.rgDirection.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbLeft -> {
                }

                R.id.rbRight -> {
                }

                R.id.rbBoth -> {
                }
            }
        }
    }

    private fun toggleOverlayPanel() {
        val toggle = findViewById<SlideToggleView>(R.id.slideToggle)

        toggle.onToggleChanged = { isOn ->
            sharePrefApp.checktoggle = isOn
            if (isOn) {
                if (!canDrawOverlay()) {
                    requestOverlayPermission()
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
    override fun onBackPressedSystem() {
        finishAffinity()
    }
}