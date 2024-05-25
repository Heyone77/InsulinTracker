package com.heysoft.insulintracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val fchiValue: MutableLiveData<Double> = MutableLiveData()

}

