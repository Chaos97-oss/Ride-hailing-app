package com.example.ridehaillingapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ridehaillingapp.data.repository.RideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RideRepository
) : ViewModel() {

}