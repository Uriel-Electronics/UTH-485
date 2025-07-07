package com.urielelectronics.uth485.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.R.drawable.lock
import com.urielelectronics.uth485.R.drawable.lock_open
import com.urielelectronics.uth485.R.drawable.minus
import com.urielelectronics.uth485.R.drawable.plus
import com.urielelectronics.uth485.R.drawable.power
import com.urielelectronics.uth485.R.drawable.tune
import com.urielelectronics.uth485.ui.theme.UrielBGBeige2
import com.urielelectronics.uth485.views.Device
import com.urielelectronics.uth485.views.MyViewModel
import com.urielelectronics.uth485.views.ViewState

@Composable
fun ControlFooter(
    viewState: MutableState<ViewState>,
    viewModel : MyViewModel,
    device: Device,
    onDeviceChange: (Device, String) -> Unit,
    type : String = "device" // "device" || "group" || "global"
) {

    val controlList =
        if(type == "global") listOf<String>("온도 내림", "온도 올림", "잠금", "전원")
        else listOf<String>("온도 내림", "온도 올림", "잠금", "전원", "시간 설정")

    Box (
        Modifier
            .fillMaxWidth()
            .height(256.dp)
            .background(UrielBGBeige2),
        contentAlignment = Alignment.Center
    ) {
        Row (
            Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if("온도 내림" in controlList) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    UrielFancyButton(
                        icon = ImageVector.vectorResource(minus),
                        onClick = {
                            // TODO - 온도 내림 작동
                            if(!device.isLocked) {
                                onDeviceChange(device.copy(settingTemp =
                                    if(device.settingTemp > viewModel.V_L) device.settingTemp - 1 else device.settingTemp),
                                    "settingTemp")
                            }
                        }
                    )
                    Text(
                        text = "온도 내림",
                        fontSize = 18.sp
                    )
                }
            }
            if("온도 올림" in controlList) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    UrielFancyButton(
                        icon = ImageVector.vectorResource(plus),
                        onClick = {
                            // TODO - 온도 올림 작동
                            if (!device.isLocked) {
                                onDeviceChange(device.copy(settingTemp =
                                    if(device.settingTemp < viewModel.V_H) device.settingTemp + 1 else device.settingTemp),
                                    "settingTemp")
                            }
                        }
                    )
                    Text(
                        text = "온도 올림",
                        fontSize = 18.sp
                    )
                }
            }
            if("잠금" in controlList) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    UrielFancyButton(
                        icon =
                            if(device.isLocked) ImageVector.vectorResource(lock)
                            else ImageVector.vectorResource(lock_open),
                        onClick = {
                            // TODO - 잠금 작동
                            onDeviceChange(device.copy(isLocked = !device.isLocked), "isLocked")
                        },
                        onSelected = device.isLocked,
                    )
                    Text(
                        text = "잠금",
                        fontSize = 18.sp
                    )
                }
            }
            if("전원" in controlList) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    UrielFancyButton(
                        icon = ImageVector.vectorResource(power),
                        onClick = {
                            // TODO - 전원 작동
                            onDeviceChange(device.copy(powerOn = !device.powerOn), "powerOn")
                        },
                        onSelected = device.powerOn,
                    )
                    Text(
                        text = "전원",
                        fontSize = 18.sp
                    )
                }
            }
            if("시간 설정" in controlList) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    UrielFancyButton(
                        icon = ImageVector.vectorResource(tune),
                        onClick = {
                            // TODO - 시간 설정 뷰로 이동
                            when(type) {
                                "device" -> viewState.value = ViewState.DEVICE_TIME_SETTING
                                "group" -> viewState.value = ViewState.DEVICE_TIME_GROUP_SETTING
                                "global" -> viewState.value = ViewState.DEVICE_TIME_GLOBAL_SETTING
                            }
                        },
                    )
                    Text(
                        text = "시간 설정",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}