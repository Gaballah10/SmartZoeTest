package com.example.smartzonetest.network.viewmodefac

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartzonetest.app.userrepo.NewsRepository
import com.example.smartzonetest.app.viewmodel.HomeViewModel

class HomeModelFactory (
    private val repository: NewsRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}