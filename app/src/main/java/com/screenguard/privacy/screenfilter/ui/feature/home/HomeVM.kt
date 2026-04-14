package com.screenguard.privacy.screenfilter.ui.feature.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.screenguard.privacy.screenfilter.R

class HomeVM : ViewModel() {
    private val _selectedNav = MutableLiveData(R.id.homeFragment)
    val selectedNav: LiveData<Int> = _selectedNav

    fun setNavigation(destination: Int) {
        if (_selectedNav.value != destination) {
            _selectedNav.value = destination
        }

    }

}