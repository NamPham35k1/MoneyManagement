package com.screenguard.privacy.screenfilter.ui.fragment.fragment_home
import com.screenguard.privacy.screenfilter.base.BaseFragment
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<CommonVM, FragmentHomeBinding>() {

    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java
    override fun getViewDataBinging(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun initView() {

    }

    override fun bindViewModel() {

    }

    override fun onResume() {
        super.onResume()
    }


}
