package com.urielelectronics.uth485.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.ui.theme.UrielBGLightRed
import com.urielelectronics.uth485.ui.theme.UrielBGWhite
import com.urielelectronics.uth485.ui.theme.UrielOrange
import com.urielelectronics.uth485.ui.theme.UrielTableHeaderBeige
import com.urielelectronics.uth485.ui.theme.UrielTableHeaderGray
import com.urielelectronics.uth485.ui.theme.UrielTextDark
import com.urielelectronics.uth485.ui.theme.UrielTextGray
import com.urielelectronics.uth485.ui.theme.UrielTextOrange
import com.urielelectronics.uth485.views.Device
import com.urielelectronics.uth485.views.ErrorLog
import com.urielelectronics.uth485.views.MyViewModel
import com.urielelectronics.uth485.views.SettingMode
import com.urielelectronics.uth485.views.formattedErrorLog

@Composable
fun Table(
    headers: List<String>,
    rows: List<Any>,
    weights: List<Float>,
    modifier: Modifier,
    viewModel : MyViewModel,
    type : String = "status" // "status" || "error"
) {
    val scrollState = rememberScrollState()
//    var selectedRow by remember { mutableStateOf<Device>(Device(0,"",0)) }

    fun deviceRow(device : Device) = listOf(
        device.name,
        if (device.powerOn) "On" else "Off",
        if (device.mode == SettingMode.TempModeSetting) "온도" else "시간",
        if (device.print) "출력" else "없음",
        if (device.error) "발생" else "없음",
        if (device.connectionError) "발생" else "없음",
        if (device.mode == SettingMode.TempModeSetting)
            if (!device.error && !device.connectionError) "${device.settingTemp}°C"
            else "0°C" // TODO - 단말기 에러 / 통신 에러 둘중 하나만 걸려도 0도씨로 표현?
        else "${viewModel.step}단계", // TODO - 시간 단계는 모든 단말기에 대해 동일?
        "${viewModel.currentTemp}°C",
        "${viewModel.intervalTime}분" // TODO - 시간주기는 기본값설정에서 정하는데 왜 단말기마다 다른 값?
    )

    fun errorRow(errorlog : formattedErrorLog) = listOf(
        0.toString(),
        errorlog.date.toString(),
        errorlog.circuit,
        errorlog.msg,
        errorlog.time.toString()
    )

    Box(
        modifier = modifier) {
        Column {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(UrielTableHeaderBeige)
            ) {
                headers.forEachIndexed { idx, title ->
                    Box(
                        modifier = Modifier
                            .weight(weights[idx])
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 24.sp,
                            color = UrielTextOrange,
                        )
                    }
                    if (idx < headers.lastIndex) {
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                                .background(UrielTextGray)
                        )
                    }
                }
            }
            HorizontalDivider(color = UrielTextGray)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .background(UrielBGWhite)
            ) {
                Column {
                    rows.forEachIndexed { rowIdx, row ->
                        var rowColor = UrielBGWhite
                        if (type == "status") {
                            val tmp = row as Device
                            if(tmp.error || tmp.connectionError) {
                                rowColor = UrielBGLightRed
                            }
                        }
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .background(rowColor)
                        ) {
                            var cells = if (type == "status") deviceRow(row as Device)
                            else errorRow(row as formattedErrorLog)
                            cells.forEachIndexed { colIdx, cell ->
                                Box(modifier = Modifier
                                    .weight(weights[colIdx])
                                    .padding(8.dp)
                                    /* .clickable(
                                        onClick = {
                                            // make the row background color to UrielBGBeige
                                        }
                                    ) */
                                    ,
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if(type == "error" && colIdx == 0) (rowIdx+1).toString() else cell,
                                        fontSize = 20.sp,
                                        fontWeight = if(type == "status" && colIdx == 0) // 이름 column Bold
                                            FontWeight.SemiBold else FontWeight.Normal,
                                        color =
                                            if(type == "status"
                                                && (cells[4] == "발생" || cells[5] == "발생")
                                                && (colIdx == 1 || cell == "발생")) // 에러 발생 시 전원 column 이거나 에러 / 통신에러 column 이라면
                                                UrielOrange
                                            else UrielTextDark
                                    )
                                }
                                if (colIdx < headers.lastIndex) {
                                    Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .fillMaxHeight()
                                            .background(UrielTextGray)
                                    )
                                }
                            }
                        }
                        HorizontalDivider(
                            color = UrielTextGray
                        )
                    }
                }
            }
        }
    }
}