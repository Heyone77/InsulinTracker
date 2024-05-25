package com.example.insulintracker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val fchiValue: MutableLiveData<Double> = MutableLiveData()
}