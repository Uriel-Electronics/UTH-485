package com.urielelectronics.uth485.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.urielelectronics.uth485.ui.theme.UrielBGBeige
import com.urielelectronics.uth485.views.components.ControlFooter
import com.urielelectronics.uth485.views.components.Header
import com.urielelectronics.uth485.views.components.TemperatureGauge

@Composable
fun GlobalTempSettingView (
    viewState: MutableState<ViewState>,
    viewModel: MyViewModel
) {

    var globalDevice by remember { mutableStateOf<Device>(
        Device(0,"",0,viewModel.currentTemp,false,false)) }

    Scaffold (
        topBar = {
            Header(
                title = "전체 제어",
                content = {},
                viewState,
                isBack = true,
                goBackTo = ViewState.DEVICE_CONNECTED
            )
        }
    ) { headerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(headerPadding)
                .background(UrielBGBeige),
            contentAlignment = Alignment.Center
        ) {
            Column (
                Modifier.fillMaxSize()
            ) {
                Box (Modifier
                    .weight(2f)
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center)
                {
                    TemperatureGauge("설정 온도", globalDevice.settingTemp)
                }
                Box (
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ){
                    ControlFooter (
                        viewState = viewState,
                        device = globalDevice,
                        onDeviceChange = { newDevice, changedProp ->
                            for(i in 0..viewModel.deviceNumber-1) {
                                val oldDevice = viewModel.deviceList[i]
                                when (changedProp) {
                                    "settingTemp" -> {
                                        if(!oldDevice.isLocked) {
                                            viewModel.updateDeviceAt(i, oldDevice.copy(settingTemp = newDevice.settingTemp))
                                        }
                                    }
                                    "isLocked" -> {
                                        if ((oldDevice.isLocked && !newDevice.isLocked) // 잠금 되어있는 단말기에 대해 잠금을 해제했을 떄
                                            || (!oldDevice.isLocked && newDevice.isLocked)) { // 잠금되어있지 않은 단말기들에 대해 잠금을 걸 때
                                            viewModel.updateDeviceAt(i, oldDevice.copy(isLocked = newDevice.isLocked))
                                        }
                                    }
                                    "powerOn" -> {
                                        if (!oldDevice.isLocked) {
                                            viewModel.updateDeviceAt(i, oldDevice.copy(powerOn = newDevice.powerOn))
                                        }
                                    }
                                }
                            }
                            globalDevice = newDevice
                        }
                    )
                }
            }
        }
    }
}