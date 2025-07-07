package com.urielelectronics.uth485.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.urielelectronics.uth485.views.components.NumberSelector
import com.urielelectronics.uth485.views.components.TemperatureGauge

@Composable
fun GroupTempSettingView (
    viewState: MutableState<ViewState>,
    viewModel: MyViewModel
) {
    var currGroup by remember { mutableIntStateOf(if(viewModel.groupCount > 0) 1 else 0) }
    var groupDevice by remember { mutableStateOf<Device>(
        Device(0,"",currGroup,viewModel.currentTemp,false,false)
    ) }

    if (viewState.value == ViewState.DEVICE_TIME_GROUP_SETTING) {
        DeviceTimeSettingView(viewState, viewModel, type = "group", currGroup)
    }
    else {

        Scaffold (
            topBar = {
                Header(
                    title = "그룹 제어",
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
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row (
                        Modifier
                            .weight(2f)
                            .fillMaxWidth(0.5f)
                            .padding(vertical = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        TemperatureGauge("설정 온도", groupDevice.settingTemp)
                        Column(
                            Modifier
                                .fillMaxHeight()
                                .wrapContentWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "그룹 선택",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium
                            )
                            NumberSelector(
                                value = currGroup,
                                onValueChange = { currGroup = it },
                                min = if(viewModel.groupCount > 0) 1 else 0,
                                max = viewModel.groupCount
                            )
                        }
                    }
                    Box (
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ){
                        ControlFooter (
                            viewState = viewState,
                            device = groupDevice,
                            onDeviceChange = { newDevice, changedProp ->
                                for(i in 0..viewModel.deviceNumber-1) {
                                    if (viewModel.deviceList[i].group == currGroup) {
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
                                }
                                groupDevice = newDevice
                            },
                            type = "group"
                        )

                    }
                }
            }
        }
    }
}
