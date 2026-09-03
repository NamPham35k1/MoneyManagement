package com.screenguard.privacy.screenfilter.ui.fragment.fragment_compare

import com.screenguard.privacy.screenfilter.base.BaseFragment
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.FragmentPlaceholderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompareFragment : BaseFragment<CommonVM, FragmentPlaceholderBinding>() {
    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java
    override fun getViewDataBinging(): FragmentPlaceholderBinding = FragmentPlaceholderBinding.inflate(layoutInflater)

    override fun initView() {
        binding.tvTitle.text = "Compare Fragment"
    }

    override fun bindViewModel() {
    }
}
