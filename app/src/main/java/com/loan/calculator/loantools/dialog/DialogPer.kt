package com.loan.calculator.loantools.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.loan.calculator.loantools.base.BaseDialogFragment
import com.loan.calculator.loantools.extention.tap
import com.loan.calculator.loantools.databinding.DialogPermissionBinding

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