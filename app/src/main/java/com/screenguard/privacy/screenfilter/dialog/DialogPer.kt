package com.screenguard.privacy.screenfilter.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.screenguard.privacy.screenfilter.base.BaseDialogFragment
import com.screenguard.privacy.screenfilter.extention.tap
import com.screenguard.privacy.screenfilter.databinding.DialogPermissionBinding

import kotlin.apply

class DialogPer(
    private val onClickOke: () -> Unit
) : BaseDialogFragment<DialogPermissionBinding>(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogPermissionBinding = DialogPermissionBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.apply {
            tvOke.tap {
                dismiss()
                onClickOke.invoke()
            }
        }

    }

    override fun bindView() {

    }


}