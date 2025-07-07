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
    var tempErrMsgOn by mutableStateOf<Boolean>(true)
    var tempErrMsgFrequency by mutableIntStateOf(5)

    var timeErrMsgOn by mutableStateOf<Boolean>(true)
    var timeErrMsgFrequency by mutableIntStateOf(5)

    var groupCount by mutableIntStateOf(3) // 그룹 수
    val initialDeviceList = listOf<Device>(
        Device(1, "101호 특실", 1),
        Device(2, "102호", 1),
        Device(3, "103호", 2),
        Device(4, "104호", 2),
        Device(5, "105호", 3, error = true, connectionError = true),
        Device(6, "106호", 1),
        Device(7, "107호", 3),
        Device(8, "108호", 2),
        Device(9, "109호", 2, error = true),
        Device(10, "110호", 1),
        Device(11, "111호", 3, connectionError = true),
        Device(12, "112호", 1),
        Device(13, "113호", 3),
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