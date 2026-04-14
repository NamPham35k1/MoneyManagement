package com.screenguard.privacy.screenfilter.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    protected val mBoxActionState = MutableLiveData<ViewModelActionState>()
    val boxActionState: LiveData<ViewModelActionState> = mBoxActionState

    sealed class ViewModelActionState {
        data object Loading : ViewModelActionState()

        data class Fail(val msg: String) : ViewModelActionState()

        data class Success(val msg: String = "") : ViewModelActionState()
    }
}


