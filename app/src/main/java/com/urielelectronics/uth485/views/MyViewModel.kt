package com.urielelectronics.uth485.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {
    var isConnectLoading = mutableStateOf(false)

    // 0xBF, mode = 1
    var year by mutableStateOf(0)
    var month by mutableStateOf(0)
    var day by mutableStateOf(0)
    var dayOfWeek by mutableStateOf(0) // 요일
    var hour by mutableStateOf(0)
    var minute by mutableStateOf(0)
    var sec by mutableStateOf(0)

    // 0xBF, mode = 3
    var deviceCount by mutableStateOf(0) // 단말기 수


    fun updateDeviceCount(
        deviceCount: Int
    ) {
        this.deviceCount = deviceCount
    }

    // 0xBF, mode = 5
    var password1 by mutableStateOf(0)
    var password2 by mutableStateOf(0)
    var password3 by mutableStateOf(0)
    var password4 by mutableStateOf(0)

    // 0xBF, mode = 21
    var power by mutableStateOf(0)
    var lock by mutableStateOf(0)

    // 0xBF, mode = 23

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

}