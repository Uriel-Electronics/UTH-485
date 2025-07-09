package com.urielelectronics.uth485.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.collection.IntIntPair
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontVariation
import androidx.lifecycle.ViewModel
import java.sql.Time
import java.time.LocalTime

data class ReservationTime (
    var startHour : Int = 5,
    var startMinute: Int = 30,
    var endHour : Int = 23,
    var endMinute: Int = 30,
    var on : Boolean = false
)

val defaultTimeList =
    listOf(
        ReservationTime(5,30,23,30,false), // 월
        ReservationTime(5,30,23,30,false), // 화
        ReservationTime(5,30,23,30,false), // 수
        ReservationTime(5,30,23,30,false), // 목
        ReservationTime(5,30,23,30,false), // 금
        ReservationTime(5,30,23,30,false), // 토
        ReservationTime(5,30,23,30,false), // 일
    )

enum class SettingMode {
    TempModeSetting,
    TimeModeSetting
}

data class Device (
    var id: Int,
    var name: String = "",
    var group: Int = 0,
    var settingTemp : Int = 30,
    var isLocked : Boolean = false,
    var powerOn : Boolean = false,
    var time : List<ReservationTime> = defaultTimeList,
    var timeChecked : List<Boolean> = listOf(false,false,false,false,false,false,false),
    var mode : SettingMode = SettingMode.TempModeSetting,
    var print : Boolean = true,
    var error : Boolean = false,
    var connectionError : Boolean = false,

)

class MyViewModel: ViewModel() {
    var isConnectLoading = mutableStateOf(false)

    // 0xAF, mode 3
    var deviceNumber by mutableStateOf(0/* TODO - 0으로 지워야함*/) // 단말기 수
    var maxIdx by mutableStateOf(0) // 단말기 최대 Index

    fun updateDeviceCount(
        deviceNumber: Int,
        maxIdx: Int
    ) {
        this.deviceNumber = deviceNumber
        this.maxIdx = maxIdx
    }

    // 0xAF, mode 5
    var password1 by mutableStateOf(0)
    var password2 by mutableStateOf(0)
    var password3 by mutableStateOf(0)
    var password4 by mutableStateOf(0)

    fun updatePassword(
        pw1: Int, pw2: Int, pw3: Int, pw4: Int
    ) {
        this.password1 = pw1
        this.password2 = pw2
        this.password3 = pw3
        this.password4 = pw4
    }

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
    var timeStep by mutableStateOf(0) // 시간단계

    fun updateSettingData(
        rN: Int, V_L: Int, V_H: Int, IF: Int, LY: Int, Ht: Int, ES: Int, interval: Int, firstTime: Int, settingTemp: Int, timeStep: Int
    ) {
        this.rN = rN
        this.V_L = V_L
        this.V_H = V_H
        this.IF = IF
        this.LY = LY
        this.Ht = Ht
        this.ES = ES
        this.intervalTime = interval
        this.fisrtTime = firstTime
        this.settingTemp = settingTemp
        this.timeStep = timeStep
    }

    // 0xAF, mode = 33, index 1 ~ 246, Ex) index = 1, Device 1 ~ 10, ... index 2
    var scanIndex by mutableStateOf(0)

    var rN1 by mutableStateOf(0)
    var power1 by mutableStateOf(0)
    var relay1 by mutableStateOf(0)
    var mode1 by mutableStateOf(0)
    var lock1 by mutableStateOf(0)
    var sensorError1 by mutableStateOf(0)
    var shortError1 by mutableStateOf(0)
    var overHeat1 by mutableStateOf(0)
    var LHMode1 by mutableStateOf(0)
    var settingTemp1 by mutableStateOf(0)
    var settingStep1 by mutableStateOf(0)
    var currentTemp1 by mutableStateOf(0)

    var rN2 by mutableStateOf(0)
    var power2 by mutableStateOf(0)
    var relay2 by mutableStateOf(0)
    var mode2 by mutableStateOf(0)
    var lock2 by mutableStateOf(0)
    var sensorError2 by mutableStateOf(0)
    var shortError2 by mutableStateOf(0)
    var overHeat2 by mutableStateOf(0)
    var LHMode2 by mutableStateOf(0)
    var settingTemp2 by mutableStateOf(0)
    var settingStep2 by mutableStateOf(0)
    var currentTemp2 by mutableStateOf(0)

    var rN3 by mutableStateOf(0)
    var power3 by mutableStateOf(0)
    var relay3 by mutableStateOf(0)
    var mode3 by mutableStateOf(0)
    var lock3 by mutableStateOf(0)
    var sensorError3 by mutableStateOf(0)
    var shortError3 by mutableStateOf(0)
    var overHeat3 by mutableStateOf(0)
    var LHMode3 by mutableStateOf(0)
    var settingTemp3 by mutableStateOf(0)
    var settingStep3 by mutableStateOf(0)
    var currentTemp3 by mutableStateOf(0)

    var rN4 by mutableStateOf(0)
    var power4 by mutableStateOf(0)
    var relay4 by mutableStateOf(0)
    var mode4 by mutableStateOf(0)
    var lock4 by mutableStateOf(0)
    var sensorError4 by mutableStateOf(0)
    var shortError4 by mutableStateOf(0)
    var overHeat4 by mutableStateOf(0)
    var LHMode4 by mutableStateOf(0)
    var settingTemp4 by mutableStateOf(0)
    var settingStep4 by mutableStateOf(0)
    var currentTemp4 by mutableStateOf(0)

    var rN5 by mutableStateOf(0)
    var power5 by mutableStateOf(0)
    var relay5 by mutableStateOf(0)
    var mode5 by mutableStateOf(0)
    var lock5 by mutableStateOf(0)
    var sensorError5 by mutableStateOf(0)
    var shortError5 by mutableStateOf(0)
    var overHeat5 by mutableStateOf(0)
    var LHMode5 by mutableStateOf(0)
    var settingTemp5 by mutableStateOf(0)
    var settingStep5 by mutableStateOf(0)
    var currentTemp5 by mutableStateOf(0)

    var rN6 by mutableStateOf(0)
    var power6 by mutableStateOf(0)
    var relay6 by mutableStateOf(0)
    var mode6 by mutableStateOf(0)
    var lock6 by mutableStateOf(0)
    var sensorError6 by mutableStateOf(0)
    var shortError6 by mutableStateOf(0)
    var overHeat6 by mutableStateOf(0)
    var LHMode6 by mutableStateOf(0)
    var settingTemp6 by mutableStateOf(0)
    var settingStep6 by mutableStateOf(0)
    var currentTemp6 by mutableStateOf(0)

    var rN7 by mutableStateOf(0)
    var power7 by mutableStateOf(0)
    var relay7 by mutableStateOf(0)
    var mode7 by mutableStateOf(0)
    var lock7 by mutableStateOf(0)
    var sensorError7 by mutableStateOf(0)
    var shortError7 by mutableStateOf(0)
    var overHeat7 by mutableStateOf(0)
    var LHMode7 by mutableStateOf(0)
    var settingTemp7 by mutableStateOf(0)
    var settingStep7 by mutableStateOf(0)
    var currentTemp7 by mutableStateOf(0)

    var rN8 by mutableStateOf(0)
    var power8 by mutableStateOf(0)
    var relay8 by mutableStateOf(0)
    var mode8 by mutableStateOf(0)
    var lock8 by mutableStateOf(0)
    var sensorError8 by mutableStateOf(0)
    var shortError8 by mutableStateOf(0)
    var overHeat8 by mutableStateOf(0)
    var LHMode8 by mutableStateOf(0)
    var settingTemp8 by mutableStateOf(0)
    var settingStep8 by mutableStateOf(0)
    var currentTemp8 by mutableStateOf(0)

    var rN9 by mutableStateOf(0)
    var power9 by mutableStateOf(0)
    var relay9 by mutableStateOf(0)
    var mode9 by mutableStateOf(0)
    var lock9 by mutableStateOf(0)
    var sensorError9 by mutableStateOf(0)
    var shortError9 by mutableStateOf(0)
    var overHeat9 by mutableStateOf(0)
    var LHMode9 by mutableStateOf(0)
    var settingTemp9 by mutableStateOf(0)
    var settingStep9 by mutableStateOf(0)
    var currentTemp9 by mutableStateOf(0)

    var rN10 by mutableStateOf(0)
    var power10 by mutableStateOf(0)
    var relay10 by mutableStateOf(0)
    var mode10 by mutableStateOf(0)
    var lock10 by mutableStateOf(0)
    var sensorError10 by mutableStateOf(0)
    var shortError10 by mutableStateOf(0)
    var overHeat10 by mutableStateOf(0)
    var LHMode10 by mutableStateOf(0)
    var settingTemp10 by mutableStateOf(0)
    var settingStep10 by mutableStateOf(0)
    var currentTemp10 by mutableStateOf(0)

    fun updateScanData1(
        scanIndex: Int,
        rN1: Int,
        power1: Int,
        relay1: Int,
        mode1: Int,
        lock1: Int,
        sensorError1: Int,
        shortError1: Int,
        overHeat1: Int,
        LHMode1: Int,
        settingTemp1: Int,
        settingStep1: Int,
        currentTemp1: Int
    ) {
        this.scanIndex = scanIndex
        this.rN1 = rN1
        this.power1 = power1
        this.relay1 = relay1
        this.mode1 = mode1
        this.lock1 = lock1
        this.sensorError1 = sensorError1
        this.shortError1 = shortError1
        this.overHeat1 = overHeat1
        this.LHMode1 = LHMode1
        this.settingTemp1 = settingTemp1
        this.settingStep1 = settingStep1
        this.currentTemp1 = currentTemp1
    }

    fun updateScanData2(
        scanIndex: Int,
        rN2: Int,
        power2: Int,
        relay2: Int,
        mode2: Int,
        lock2: Int,
        sensorError2: Int,
        shortError2: Int,
        overHeat2: Int,
        LHMode2: Int,
        settingTemp2: Int,
        settingStep2: Int,
        currentTemp2: Int
    ) {
        this.scanIndex = scanIndex
        this.rN2 = rN2
        this.power2 = power2
        this.relay2 = relay2
        this.mode2 = mode2
        this.lock2 = lock2
        this.sensorError2 = sensorError2
        this.shortError2 = shortError2
        this.overHeat2 = overHeat2
        this.LHMode2 = LHMode2
        this.settingTemp2 = settingTemp2
        this.settingStep2 = settingStep2
        this.currentTemp2 = currentTemp2
    }

    fun updateScanData3(
        scanIndex: Int,
        rN3: Int,
        power3: Int,
        relay3: Int,
        mode3: Int,
        lock3: Int,
        sensorError3: Int,
        shortError3: Int,
        overHeat3: Int,
        LHMode3: Int,
        settingTemp3: Int,
        settingStep3: Int,
        currentTemp3: Int
    ) {
        this.scanIndex = scanIndex
        this.rN3 = rN3
        this.power3= power3
        this.relay3 = relay3
        this.mode3 = mode3
        this.lock3 = lock3
        this.sensorError3 = sensorError3
        this.shortError3 = shortError3
        this.overHeat3 = overHeat3
        this.LHMode3 = LHMode3
        this.settingTemp3 = settingTemp3
        this.settingStep3 = settingStep3
        this.currentTemp3 = currentTemp3
    }

    fun updateScanData4(
        scanIndex: Int,
        rN4: Int,
        power4: Int,
        relay4: Int,
        mode4: Int,
        lock4: Int,
        sensorError4: Int,
        shortError4: Int,
        overHeat4: Int,
        LHMode4: Int,
        settingTemp4: Int,
        settingStep4: Int,
        currentTemp4: Int
    ) {
        this.scanIndex = scanIndex
        this.rN4 = rN4
        this.power4 = power4
        this.relay4 = relay4
        this.mode4 = mode4
        this.lock4 = lock4
        this.sensorError4 = sensorError4
        this.shortError4 = shortError4
        this.overHeat4 = overHeat4
        this.LHMode4 = LHMode4
        this.settingTemp4 = settingTemp4
        this.settingStep4 = settingStep4
        this.currentTemp4 = currentTemp4
    }

    fun updateScanData5(
        scanIndex: Int,
        rN5: Int,
        power: Int,
        relay: Int,
        mode: Int,
        lock: Int,
        sensorError: Int,
        shortError: Int,
        overHeat: Int,
        LHMode: Int,
        settingTemp: Int,
        settingStep: Int,
        currentTemp: Int
    ) {
        this.scanIndex = scanIndex
        this.rN5 = rN5
        this.power5 = power
        this.relay5 = relay
        this.mode5 = mode
        this.lock5 = lock
        this.sensorError5 = sensorError
        this.shortError5 = shortError
        this.overHeat5 = overHeat
        this.LHMode5 = LHMode
        this.settingTemp5 = settingTemp
        this.settingStep5 = settingStep
        this.currentTemp5 = currentTemp
    }

    fun updateScanData6(
        scanIndex: Int,
        rN6: Int,
        power: Int,
        relay: Int,
        mode: Int,
        lock: Int,
        sensorError: Int,
        shortError: Int,
        overHeat: Int,
        LHMode: Int,
        settingTemp: Int,
        settingStep: Int,
        currentTemp: Int
    ) {
        this.scanIndex = scanIndex
        this.rN6= rN6
        this.power6 = power
        this.relay6 = relay
        this.mode6 = mode
        this.lock6 = lock
        this.sensorError6 = sensorError
        this.shortError6 = shortError
        this.overHeat6 = overHeat
        this.LHMode6 = LHMode
        this.settingTemp6 = settingTemp
        this.settingStep6 = settingStep
        this.currentTemp6 = currentTemp
    }



    // --- TODO - 추가된 전역 변수들 ---
    var tempErrMsgOn by mutableStateOf<Boolean>(true)
    var tempErrMsgFrequency by mutableIntStateOf(5)

    var timeErrMsgOn by mutableStateOf<Boolean>(true)
    var timeErrMsgFrequency by mutableIntStateOf(5)

    var groupCount by mutableIntStateOf(0) // 그룹 수, 초기값 0
    val initialDeviceList = listOf<Device>(
        Device(1, "101호", 1),
        Device(2, "102호", 1),
        Device(3, "103호", 2),
        Device(4, "104호", 2),
        Device(5, "105호", 3, error = true, connectionError = true),
        Device(6, "106호", 1),
        Device(7, "107호", 3),
        Device(8, "108호", 2, mode = SettingMode.TimeModeSetting),
        Device(9, "109호", 2, error = true),
        Device(10, "110호", 1),
//        Device(11, "111호", 3, connectionError = true),
//        Device(12, "112호", 1, mode = SettingMode.TimeModeSetting),
//        Device(13, "113호", 3),
    )

    var deviceList = mutableStateListOf<Device>().apply { // 단말기 리스트
        addAll(initialDeviceList)
    }

    val initialGroupList = listOf<Device>(
        Device(0, "group_1", 1),
        Device(0, "group_2", 2),
        Device(0, "group_3", 3),
    )

    var groupDeviceList = mutableStateListOf<Device>().apply { // 단말기 리스트
        addAll(initialGroupList)
    }

    var globalDevice = Device(0, "global_device", 0)
    fun updateDeviceList(newDeviceList : List<Device>) { // 단말기 리스트 전체 업데이트 메서드
        deviceList.clear()
        deviceList.addAll(newDeviceList)
    }
    fun updateDeviceGroup(groupCount : Int) {
        val oldGroupList = groupDeviceList
        groupDeviceList.clear()
        repeat(groupCount) { i ->
            // oldList 에 i 인덱스가 있으면 그 값을, 없으면 빈 Device()를
            val element = oldGroupList.getOrNull(i) ?: Device(
                id = 0,
                name = "group_${i+1}",
                group = i+1,
            )
            groupDeviceList.add(element)
        }
    }
    fun updateDeviceAt(index : Int, newDevice : Device) { // 단말기 업데이트 메서드
        deviceList[index] = newDevice
    }
    fun updateGroupDevice(groupId : Int, newGroupDevice : Device) {
        groupDeviceList[groupId - 1] = newGroupDevice
    }
    fun updateGlobalDevice(newGlobalDevice : Device) {
        globalDevice = newGlobalDevice
    }
    var currentTemp by mutableStateOf(28) // 현재 온도
    // --- TODO ---
}