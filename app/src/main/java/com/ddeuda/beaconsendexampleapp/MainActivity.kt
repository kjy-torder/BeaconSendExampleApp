package com.ddeuda.beaconsendexampleapp

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter
import java.util.*

/**
 * 안드로이드 태블릿의 블루투스 신호를 이용한 비콘 기능을 실증하기 위한 어플리케이션
 */
class MainActivity : AppCompatActivity() {
    val tag = "BeaconSend"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        beaconSendStart(
            uuid = "ae0e0002-0001-0000-0000-000000000000",
            major = "4977",
            minor = "3638"
        )
    }

    fun beaconSendStart(uuid: String, major: String, minor: String) {
        Log.e(tag, "비콘 송신 시작")

        try {
            val beacon = Beacon.Builder()
                .setId1(uuid)
                .setId2(major)
                .setId3(minor)
                .setManufacturer(0x004c)
                .setTxPower(-59)
                .setDataFields(listOf(0L))
                .build()

            Log.e(tag, "$beacon")

            val beaconParser = BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25")
            val beaconTransmitter = BeaconTransmitter(applicationContext, beaconParser)
            beaconTransmitter.startAdvertising(beacon, object : AdvertiseCallback() {
                override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                    super.onStartSuccess(settingsInEffect)
                    Log.e(tag, "비콘 송신 기능 활성화 성공")
                }

                override fun onStartFailure(errorCode: Int) {
                    super.onStartFailure(errorCode)
                    Log.e(tag, "비콘 송신 기능 활성화 실패")
                }
            })
        } catch (e: Exception) {
            Log.e(tag, "비콘 송신 실패 : $e")
        }
    }
}