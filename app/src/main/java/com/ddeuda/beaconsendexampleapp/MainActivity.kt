package com.ddeuda.beaconsendexampleapp

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.ddeuda.beaconsendexampleapp.databinding.ActivityMainBinding
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter
import java.util.*

/**
 * 안드로이드 태블릿의 블루투스 신호를 이용한 비콘 기능을 실증하기 위한 어플리케이션
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(MainViewModel::class.java)

    private val beaconParser = BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25")
    private lateinit var beaconTransmitter: BeaconTransmitter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            lifecycleOwner = this@MainActivity
            viewModel = mainViewModel.apply {
                run.observe(this@MainActivity, { run ->
                    when(run) {
                        false -> beaconSendStart()
                        true -> beaconSendStop()
                    }
                })
            }
        }

        beaconTransmitter = BeaconTransmitter(applicationContext, beaconParser)
    }

    private fun beaconSendStart() {
        Log.d("MainActivity", "Beacon Send Start!")
        try {
            binding.apply {
                val beacon = Beacon.Builder()
                    .setId1("${uuidEditTextText.text}")
                    .setId2("${majorEditTextText.text}")
                    .setId3("${minorEditTextText.text}")
                    .setManufacturer(0x004c)
                    .setTxPower(-59)
                    .setDataFields(listOf(0L))
                    .build()

                beaconTransmitter.startAdvertising(beacon, object : AdvertiseCallback() {
                    override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                        super.onStartSuccess(settingsInEffect)
                        setRun(true)
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

    private fun beaconSendStop() {
        Log.d("MainActivity", "Beacon Send Stop!")

        try {
            beaconTransmitter.stopAdvertising()
            setRun(false)
        } catch (e: Exception) {

        }
    }

    private fun setRun(isRun: Boolean) {
        mainViewModel.setRun(isRun)
    }
}