package com.ddeuda.beaconsendexampleapp

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY
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
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            MainViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
                lifecycleOwner = this@MainActivity
                activity = this@MainActivity
                viewModel = mainViewModel
            }
    }

    fun beaconButtonOnClick(v: View) {
            when (mainViewModel.isRun.value) {
                true -> beaconSendStop()
                else -> beaconSendStart()
            }
    }

    private fun beaconSendStart() {
        binding.apply {
            Log.d("MainActivity", "Beacon Send Start! ${minorEditTextText.text}")
            mainViewModel.beaconStart(
                uuid = "${uuidEditTextText.text}",
                major = "${majorEditTextText.text}",
                minor = "${minorEditTextText.text}"
            )
        }
    }

    private fun beaconSendStop() {
        Log.d("MainActivity", "Beacon Send Stop!")
        mainViewModel.beaconStop()
    }
}