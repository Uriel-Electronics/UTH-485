package com.urielelectronics.uth485.views

import android.widget.Toast
import androidx.collection.IntIntPair
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.ui.theme.UrielBGBeige
import com.urielelectronics.uth485.ui.theme.UrielBGBeigeDark
import com.urielelectronics.uth485.ui.theme.UrielBGWhite
import com.urielelectronics.uth485.ui.theme.UrielBorderGray
import com.urielelectronics.uth485.ui.theme.UrielSwitchGray
import com.urielelectronics.uth485.ui.theme.UrielSwitchGreen
import com.urielelectronics.uth485.ui.theme.UrielTextGray
import com.urielelectronics.uth485.views.components.Header
import com.urielelectronics.uth485.views.components.NumberSelector2

@Composable
fun DeviceTimeSettingView (
    viewState: MutableState<ViewState>,
    viewModel: MyViewModel,
    type : String = "device", // "device" || "group" || "global"
    id : Int,
) {

    var tmpTime by remember { mutableStateOf(
        if(type == "device") viewModel.deviceList[id-1].time
        else defaultTimeList
        )
    }
    val scrollState = rememberScrollState()


    Scaffold (
        topBar = {
            Header(
                title = "사용시간 설정하기",
                content = {},
                viewState,
                isBack = true,
                goBackTo =
                    if(viewState.value == ViewState.DEVICE_TIME_SETTING) ViewState.DEVICE_TEMP_DEVICE_SETTING
                    else if(viewState.value == ViewState.DEVICE_TIME_GROUP_SETTING) ViewState.DEVICE_TEMP_GROUP_SETTING
                    else ViewState.DEVICE_CONNECTED
            )
        }
    ) { headerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(headerPadding)
                .background(UrielBGBeige)
                .verticalScroll(scrollState),
            contentAlignment = Alignment.Center
        ) {
            Column (
                Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {

                WeekHeader()
                listOf("월","화","수","목","금","토","일").forEachIndexed { idx, day ->

                    HorizontalDivider(color = UrielBorderGray,
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .height(2.dp))
                    WeekBlock(
                        week = day,
                        value = tmpTime[idx],
                        onValueChanged = { newTime, changedProp ->
                            val newResTime = tmpTime[idx] // reservationTime
                            val updated = when (changedProp) {
                                "startHour"   -> newResTime.copy(startHour   = newTime)
                                "startMinute" -> newResTime.copy(startMinute = newTime)
                                "endHour"     -> newResTime.copy(endHour     = newTime)
                                "endMinute"   -> newResTime.copy(endMinute   = newTime)
                                else          -> newResTime
                            }
                            val newTmpTime = tmpTime.toMutableList().apply {
                                this[idx] = updated
                            }
                            tmpTime = newTmpTime
                            for(i in 0..viewModel.deviceNumber-1) {
                                if (
                                    if(type == "device") viewModel.deviceList[i].id == id
                                    else if(type == "group") viewModel.deviceList[i].group == id
                                    else true
                                ) {
                                    val oldDevice = viewModel.deviceList[i]
                                    if(!oldDevice.isLocked) {
                                        viewModel.updateDeviceAt(i, oldDevice.copy(time = newTmpTime))
                                    }
                                }
                            }
                        },
                        checked = tmpTime[idx].on,
                        onCheckedChange = { isOn ->
                            val newResTime = tmpTime[idx].copy(on = isOn)
                            val newTmpTime = tmpTime.toMutableList().apply {
                                this[idx] = newResTime
                            }
                            tmpTime = newTmpTime
                            for(i in 0..viewModel.deviceNumber-1) {
                                if (
                                    if(type == "device") viewModel.deviceList[i].id == id
                                    else if(type == "group") viewModel.deviceList[i].group == id
                                    else true
                                ) {
                                    viewModel.updateDeviceAt(i, viewModel.deviceList[i].copy(time = newTmpTime))
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WeekHeader(
) {
    Row(Modifier
        .fillMaxWidth(0.7f)
        .height(60.dp)
        .clip(RoundedCornerShape(36.dp))
        .background(Color(0xFFFFFFFF)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = "요일",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            color = UrielTextGray,
            modifier = Modifier.weight(2f))
        Text(
            text = "켜지는 시간",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            color = UrielTextGray,
            modifier = Modifier.weight(5f))
        Text(
            text = "꺼지는 시간",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            color = UrielTextGray,
            modifier = Modifier.weight(5f))
        Text(
            text = "ON/OFF",
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            color = UrielTextGray,
            modifier = Modifier.weight(3f))
    }
}

@Composable
fun WeekBlock(
    week : String,
    value : ReservationTime,
    onValueChanged : (Int, String) -> Unit,
    checked : Boolean,
    onCheckedChange : (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth(0.7f)
            .height(160.dp)
            .clip(RoundedCornerShape(36.dp))
            .background(if(checked) Color(0xFFFFFFFF) else UrielBGBeigeDark),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = week,
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(2f),
            )
        VerticalDivider(
            color = UrielBorderGray,
            modifier = Modifier
                .height(100.dp))
        Row(
            Modifier
                .weight(5f)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NumberSelector2(
                value = value.startHour,
                onValueChange = { newValue -> onValueChanged(newValue, "startHour")},
                min = 0,
                max = 23,
                activate = checked
            )
            Text(
                text = "시",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(8.dp),)
            NumberSelector2(
                value = value.startMinute,
                onValueChange = { newValue -> onValueChanged(newValue, "startMinute")},
                min = 0,
                max = 59,
                activate = checked
            )
            Text(
                text = "분",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(8.dp),)
        }
        VerticalDivider(color = UrielBorderGray,
            modifier = Modifier
                .height(100.dp))
        Row(
            Modifier
                .weight(5f)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        )  {
            NumberSelector2(
                value = value.endHour,
                onValueChange = { newValue -> onValueChanged(newValue, "endHour")},
                min = 0,
                max = 23,
                activate = checked
            )
            Text(
                text = "시",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(8.dp),)
            NumberSelector2(
                value = value.endMinute,
                onValueChange = { newValue -> onValueChanged(newValue, "endMinute")},
                min = 0,
                max = 59,
                activate = checked
            )
            Text(
                text = "분",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(8.dp),)
        }
        VerticalDivider(color = UrielBorderGray,
            modifier = Modifier
                .height(100.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = UrielBGWhite,
                checkedTrackColor = UrielSwitchGreen,
                uncheckedThumbColor = UrielBGWhite,
                uncheckedTrackColor = UrielSwitchGray
            ),
            modifier = Modifier
                .weight(3f)
        )
    }
}
