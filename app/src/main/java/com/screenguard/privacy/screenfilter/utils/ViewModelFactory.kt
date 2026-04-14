package com.screenguard.privacy.screenfilter.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

object ViewModelFactory {
    val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            return when (modelClass) {
                /*LogoResultViewModel::class.java -> {
                    val application = extras[APPLICATION_KEY] ?: throw IllegalArgumentException(
                        "Application is required to create LogoResultViewModel"
                    )
                    val database = AppDatabase.getInstance(application)
                    val fileUtils = FileUtils(application)
                    LogoResultViewModel(database, fileUtils) as T
                }*/

                else -> modelClass.getDeclaredConstructor().newInstance()
            }
        }
    }
}