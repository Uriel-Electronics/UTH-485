package com.urielelectronics.uth485.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.collection.IntIntPair
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.sql.Time
import java.time.LocalTime

val defaultTimeList =
    listOf(
        listOf( // 월
            IntIntPair(5,30), IntIntPair(23,30)
        ),
        listOf( // 화
            IntIntPair(5,30), IntIntPair(23,30)
        ),
        listOf( // 수
            IntIntPair(5,30), IntIntPair(23,30)
        ),
        listOf( // 목
            IntIntPair(5,30), IntIntPair(23,30)
        ),
        listOf( // 금
            IntIntPair(5,30), IntIntPair(23,30)
        ),
        listOf( // 토
            IntIntPair(5,30), IntIntPair(23,30)
        ),
        listOf( // 일
            IntIntPair(5,30), IntIntPair(23,30)
        ),
    )

data class Device (
    var id: Int,
    var name: String,
    var group: Int,
    var settingTemp : Int = 30,
    var isLocked : Boolean = false,
    var powerOn : Boolean = false,
    var time : List<List<IntIntPair>> = defaultTimeList,
    var timeChecked : List<Boolean> = listOf(false,false,false,false,false,false,false)

)

class MyViewModel: ViewModel() {
    var isConnectLoading = mutableStateOf(false)

    // 0xAF
    var deviceNumber by mutableStateOf(13/* TODO - 0으로 지워야함*/) // 단말기 수
    var password1 by mutableStateOf(0)
    var password2 by mutableStateOf(0)
    var password3 by mutableStateOf(0)
    var password4 by mutableStateOf(0)

    var power by mutableStateOf(0)
    var lock by mutableStateOf(0)

    // mode = 27
    var rN by mutableStateOf(0) // rN
    var V_H by mutableStateOf(0) // 최고온도
    var V_L by mutableStateOf(0) // 최저온도
    var IF by mutableStateOf(0) // 온도편차
    var LY by mutableStateOf(0) // 지연시간
    var Ht by mutableStateOf(0) // 과승온도
    var ES by mutableStateOf(0) // 보정온도
    var intervalTime by mutableStateOf(0) // 시간주기
    var fisrtTime by mutableStateOf(0) // 초기투입시간
    var settingTemp by mutableStateOf(0) // 설정온도
    var step by mutableStateOf(0) // 시간단계

    // --- TODO - 추가된 전역 변수들 ---
    var groupCount by mutableIntStateOf(3) // 그룹 수
    val initialDeviceList = listOf<Device>(
        Device(1, "101호 특실", 1),
        Device(2, "102호", 1),
        Device(3, "103호", 2),
        Device(4, "104호", 2),
        Device(5, "105호", 3),
        Device(6, "106호", 1),
        Device(7, "107호", 3),
        Device(8, "108호", 2),
        Device(9, "109호", 2),
        Device(10, "110호", 1),
        Device(11, "111호", 3),
        Device(12, "112호", 1),
        Device(13, "113호", 3),
    )
    var deviceList = mutableStateListOf<Device>().apply {
        addAll(initialDeviceList)
    } // 단말기 리스트
    fun updateDeviceList(newDeviceList : List<Device>) {
        deviceList.clear()
        deviceList.addAll(newDeviceList)
    } // 단말기 리스트 전체 업데이트 메서드
    fun updateDeviceAt(index : Int, newDevice : Device) {
        deviceList[index] = newDevice
    } // 단말기 업데이트 메서드
    var currentTemp by mutableStateOf(28) // 현재 온도
    // --- TODO ---
}