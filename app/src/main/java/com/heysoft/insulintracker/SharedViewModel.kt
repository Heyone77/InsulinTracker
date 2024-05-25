package com.heysoft.insulintracker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val fchiValue: MutableLiveData<Double> = MutableLiveData()
}