package com.urielelectronics.uth485.views

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    group : Int
) {

    var tmpTime by remember { mutableStateOf(defaultTimeList) }
    var checkedList = remember {
        mutableStateListOf<Boolean>(false, false, false, false, false, false, false)
    }
    val scrollState = rememberScrollState()


    Scaffold (
        topBar = {
            Header(
                title = "사용시간 설정하기",
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
                            .fillMaxWidth(0.66f)
                            .height(2.dp))
                    WeekBlock(
                        week = day,
                        valueList = tmpTime[idx],
                        onValueChanged = { newTime, changedProp ->
                            val dayTimeList = tmpTime[idx].toMutableList() // 월요일 list
                            var newTempTime: MutableList<List<IntIntPair>> = mutableListOf()
                            for(i in 0..viewModel.deviceNumber-1) {
                                if (viewModel.deviceList[i].group == group) {
                                    val oldDevice = viewModel.deviceList[i]
                                    tmpTime = oldDevice.time
                                    when (changedProp) {
                                        "startHour" -> {
                                            if(!oldDevice.isLocked) {
                                                dayTimeList[0] = IntIntPair(newTime, dayTimeList[0].second) // 월요일 시작 시간 새로 끼우고 분은 기존 그대로
                                                newTempTime = tmpTime.toMutableList().apply { this[0] = dayTimeList }
                                                viewModel.updateDeviceAt(i, oldDevice.copy(time = newTempTime))
                                            }
                                        }
                                        "startMinute" -> {
                                            if(!oldDevice.isLocked) {
                                                dayTimeList[0] = IntIntPair(dayTimeList[0].first, newTime) // 월요일 시작 분 새로 끼우고 시간은 기존 그대로
                                                newTempTime = tmpTime.toMutableList().apply { this[0] = dayTimeList }
                                                viewModel.updateDeviceAt(i, oldDevice.copy(time = newTempTime))
                                            }
                                        }
                                        "endHour" -> {
                                            if(!oldDevice.isLocked) {
                                                dayTimeList[1] = IntIntPair(newTime, dayTimeList[1].second) // 월요일 끝 시간 새로 끼우고 분은 기존 그대로
                                                newTempTime = tmpTime.toMutableList().apply { this[0] = dayTimeList }
                                                viewModel.updateDeviceAt(i, oldDevice.copy(time = newTempTime))
                                            }
                                        }
                                        "endMinute" -> {
                                            if(!oldDevice.isLocked) {
                                                dayTimeList[1] = IntIntPair(dayTimeList[1].first, newTime) // 월요일 끝 분 새로 끼우고 시간은 기존 그대로
                                                newTempTime = tmpTime.toMutableList().apply { this[0] = dayTimeList }
                                                viewModel.updateDeviceAt(i, oldDevice.copy(time = newTempTime))
                                            }
                                        }
                                    }
                                }
                            tmpTime = newTempTime
                        }

                        },
                        checked = checkedList[idx],
                        onCheckedChange = { checkedList[idx] = it }
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
    valueList : List<IntIntPair>,
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
                value = valueList[0].first,
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
                value = valueList[0].second,
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
                value = valueList[1].first,
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
                value = valueList[1].second,
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
