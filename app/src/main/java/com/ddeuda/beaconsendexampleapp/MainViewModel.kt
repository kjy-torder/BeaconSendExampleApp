package com.ddeuda.beaconsendexampleapp

import android.app.Application
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _isRun = MutableLiveData<Boolean>(false)
    val isRun: LiveData<Boolean> = _isRun

    val iBeaconLayout = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"
    private val beaconParser =
        BeaconParser().setBeaconLayout(iBeaconLayout)
//        BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT)
    private val beaconTransmitter: BeaconTransmitter by lazy {
        BeaconTransmitter(application, beaconParser).apply {
            advertiseMode = AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY  //비콘 송신 주기를 짧게 합니다.
        }
    }

    fun setRun(isRun: Boolean) {
        _isRun.postValue(isRun)
    }

    fun beaconStart(uuid: String, major: String, minor: String) {
        try {
            val beacon = Beacon.Builder()
                .setId1(uuid)
                .setId2(major)
                .setId3(minor)
                .setManufacturer(0x004c)
                .setTxPower(-59)
                .setDataFields(listOf(0L))
                .build()

            beaconTransmitter.apply {
                startAdvertising(beacon, object : AdvertiseCallback() {
                    override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                        super.onStartSuccess(settingsInEffect)
                        setRun(isStarted)
                    }

                    override fun onStartFailure(errorCode: Int) {
                        super.onStartFailure(errorCode)
                        setRun(false)
                    }
                })
            }
        } catch (e: Exception) {
            setRun(false)
        }
    }

    fun beaconStop() {
        try {
            beaconTransmitter.stopAdvertising()
            setRun(false)
        } catch (e: Exception) {
            setRun(beaconTransmitter.isConnectable)
        }
    }
}