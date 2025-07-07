package com.urielelectronics.uth485.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.ui.theme.UrielBGBeige
import com.urielelectronics.uth485.views.components.ControlFooter
import com.urielelectronics.uth485.views.components.Header
import com.urielelectronics.uth485.views.components.TemperatureGauge

@Composable
fun DeviceTempSettingView (
    viewState: MutableState<ViewState>,
    viewModel: MyViewModel,
    deviceId : Int
    ) {

    val deviceIdx = viewModel.deviceList.indexOfFirst { it.id == deviceId }
    if(deviceIdx < 0) return
    var currDevice by remember { mutableStateOf<Device>(viewModel.deviceList[deviceIdx]) }

    if (viewState.value == ViewState.DEVICE_TIME_SETTING) {
        DeviceTimeSettingView(viewState, viewModel, type = "device", currDevice.id)
    }
    else {
        Scaffold (
            topBar = {
                Header(
                    title = "개별 제어",
                    content = {},
                    viewState,
                    isBack = true,
                    goBackTo = ViewState.DEVICE_TEMP_SETTING
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
                    Column (Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top) {

                        Text(
                            currDevice.name,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Light)
                        Row (Modifier.fillMaxWidth(0.6f),
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            TemperatureGauge("설정 온도", currDevice.settingTemp)
                            TemperatureGauge("현재 온도", viewModel.currentTemp)
                        }
                    }
                    Box (
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ){
                        ControlFooter (
                            viewState = viewState,
                            device = currDevice,
                            onDeviceChange = { newDevice, changedProp ->
                                currDevice = newDevice
                                viewModel.updateDeviceAt(deviceIdx, newDevice)
                            },
                            type = "device"
                        )

                    }
                }
            }
        }
    }
}





