package com.ddeuda.beaconsendexampleapp

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter

class MainViewModel : ViewModel() {
    private val _isRun = MutableLiveData<Boolean>(false)
    val isRun: LiveData<Boolean> = _isRun

    private val _run = MutableLiveData<Boolean>()
    val run: LiveData<Boolean> = _run

    fun setRun(isRun: Boolean) {
        _isRun.postValue(isRun)
    }

    private fun beaconToggle() {
        _run.apply {
            postValue((value ?: true).not())
        }
    }

    fun beaconButtonClick(v: View) {
        beaconToggle()
    }
}