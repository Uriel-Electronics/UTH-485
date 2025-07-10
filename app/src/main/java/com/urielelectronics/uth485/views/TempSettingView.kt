package com.urielelectronics.uth485.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.ui.theme.Pretendard
import com.urielelectronics.uth485.ui.theme.UrielBGBeige
import com.urielelectronics.uth485.ui.theme.UrielBGBeige2
import com.urielelectronics.uth485.ui.theme.UrielBGDarkGray
import com.urielelectronics.uth485.ui.theme.UrielBGPaleWhite
import com.urielelectronics.uth485.ui.theme.UrielBGRed
import com.urielelectronics.uth485.ui.theme.UrielBGWhite
import com.urielelectronics.uth485.ui.theme.UrielBorderGray
import com.urielelectronics.uth485.ui.theme.UrielBorderWhite
import com.urielelectronics.uth485.ui.theme.UrielGradientDarkOrange
import com.urielelectronics.uth485.ui.theme.UrielGradientLightOrange
import com.urielelectronics.uth485.ui.theme.UrielOrange
import com.urielelectronics.uth485.ui.theme.UrielTextDark
import com.urielelectronics.uth485.ui.theme.UrielTextLight
import com.urielelectronics.uth485.views.components.Header


enum class DeviceTempCardTheme {
    Filled,
    Outlined
} // DeviceTemperatureCard()에서 theme = DeviceTempCardTheme.Filled / Outlined -> 원하는 테마로 사용 가능

fun List<Device>.padToTen() : List<Device> {
    var m = this.toMutableList()
    while(m.size % 10 != 0) {
        m.add(Device(0, "", 0))
    }
    return m
}

@Composable
fun TempSettingView (viewState: MutableState<ViewState>, viewModel: MyViewModel) {
    var page = remember { mutableIntStateOf(0) }
    var deviceList = viewModel.deviceList
    var gridList = deviceList.padToTen()
    var selectedDeviceId by remember {mutableStateOf<Int>(0)}

    if (viewState.value == ViewState.DEVICE_TEMP_DEVICE_SETTING || viewState.value == ViewState.DEVICE_TIME_SETTING) {
        DeviceTempSettingView(viewState, viewModel, selectedDeviceId)
    }
    else {
        Scaffold (
            topBar = {
                Header(
                    title = "온도 제어",
                    content = {},
                    viewState,
                    isBack = true
                )
            }
        ) { headerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(headerPadding)
                    .background(UrielBGBeige2),
                contentAlignment = Alignment.Center
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        gridList.forEachIndexed{ idx, device ->
                            if( idx / 10 == page.intValue && idx % 10 < 5 ) {
                                if(device.id != 0) {
                                    DeviceTemperatureCard(
                                        title = device.name,
                                        onCardClicked = {
                                            selectedDeviceId = device.id
                                            viewState.value = ViewState.DEVICE_TEMP_DEVICE_SETTING
                                        },
                                        settingTemp = device.settingTemp,
                                        currentTemp = viewModel.currentTemp,
                                        theme = if(device.powerOn) DeviceTempCardTheme.Filled else DeviceTempCardTheme.Outlined
                                    )
                                }
                                else {
                                    Spacer(
                                        Modifier
                                            .size(220.dp)
                                            .padding(32.dp)
                                    )
                                }
                            }
                        }
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        gridList.forEachIndexed{ idx, device ->
                            if( idx / 10 == page.intValue && idx % 10 >= 5 ) {
                                if(device.id != 0) {
                                    DeviceTemperatureCard(
                                        title = device.name,
                                        onCardClicked = {
                                            selectedDeviceId = device.id
                                            viewState.value = ViewState.DEVICE_TEMP_DEVICE_SETTING;
                                        },
                                        settingTemp = device.settingTemp,
                                        currentTemp = viewModel.currentTemp,
                                        theme = if(device.powerOn) DeviceTempCardTheme.Filled else DeviceTempCardTheme.Outlined
                                    )
                                }
                                else {
                                    Spacer(
                                        Modifier
                                            .size(220.dp)
                                            .padding(32.dp)
                                    )
                                }
                            }
                        }
                    }
                    Row (
                        Modifier
                            .width(128.dp)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PageMoveButton(
                            page = page,
                            maxPage = (if(deviceList.isEmpty()) deviceList.size / 10 else (deviceList.size - 1) / 10),
                            isLeft = true,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(6.dp))
                        PageMoveButton(
                            page = page,
                            maxPage = (if(deviceList.isEmpty()) deviceList.size / 10 else (deviceList.size - 1) / 10),
                            isLeft = false,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceTemperatureCard (
    title : String,
    onCardClicked : () -> Unit,
    settingTemp : Int,
    currentTemp : Int,
    theme : DeviceTempCardTheme = DeviceTempCardTheme.Filled
) {
    val filled = (theme == DeviceTempCardTheme.Filled)

    val textColor = if(filled) UrielTextLight else UrielTextDark
    val unitColor = if(filled) UrielTextLight else UrielOrange

    val titleFontWeight = if(filled) FontWeight.Medium else FontWeight.Normal
    val textFontWeight = if(filled) FontWeight.Medium else FontWeight.Light

    val gradientBackground = Brush.linearGradient(
        colors =
            if(filled) listOf(UrielGradientLightOrange, UrielOrange)
            else listOf(UrielBGWhite, UrielBGWhite),
        start = Offset(0f,0f),
        end = Offset.Infinite
    )
    val gradientBorder = Brush.linearGradient(
        colors =
            if(!filled) listOf(UrielGradientLightOrange, UrielOrange)
            else listOf(UrielBorderWhite, UrielBorderWhite),
        start = Offset(0f,0f),
        end = Offset.Infinite
    )

    Box (
        modifier = Modifier
            .border(
                width = if(filled) 4.dp else 2.dp,
                shape = RoundedCornerShape(48.dp),
                brush = gradientBorder)
            .shadow(
                elevation = if(filled) 8.dp else 0.dp,
                shape = RoundedCornerShape(48.dp),
                clip = false)
            .clip(RoundedCornerShape(48.dp))
            .background(gradientBackground)
            .clickable( onClick = onCardClicked )
            .size(220.dp)
            .padding(start = 18.dp, end = 18.dp, top = 28.dp, bottom = 36.dp)
    ) {
        Column (
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = textColor,
                fontSize = 28.sp,
                fontWeight = titleFontWeight,
                fontFamily = Pretendard
            )
            Row (
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "설정 온도",
                        color = UrielTextDark,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Pretendard)
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = textColor)) {
                                append(settingTemp.toString())
                            }
                            withStyle(style = SpanStyle(color = unitColor)) {
                                append("°")
                            }
                        },
                        fontSize = 40.sp,
                        fontWeight = textFontWeight,
                        modifier = Modifier.padding(end = 4.dp),
                        fontFamily = Pretendard
                    )
                }
                VerticalDivider(
                    Modifier
                        .fillMaxHeight(0.5f)
                        .width(0.5.dp)
                        .background(UrielBGPaleWhite)
                )
                Column(
                    Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "현재 온도" ,
                        color = UrielTextDark,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Pretendard)
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = textColor)) {
                                append(currentTemp.toString())
                            }
                            withStyle(style = SpanStyle(color = unitColor)) {
                                append("°")
                            }
                        },
                        fontSize = 40.sp,
                        fontWeight = textFontWeight,
                        modifier = Modifier.padding(start = 12.dp),
                        fontFamily = Pretendard
                    )
                }
            }
        }

    }
}

@Composable
fun PageMoveButton(
    page : MutableIntState,
    maxPage : Int,
    isLeft : Boolean,
    modifier: Modifier? = Modifier
) {
    IconButton(
        onClick = {
            if(isLeft) {
                if(page.intValue != 0) {
                    page.intValue  -= 1
                }
            }
            else {
                if(page.intValue < maxPage) {
                    page.intValue  += 1
                }
            }
        },
        Modifier
            .border(
                if (isLeft) {
                    if (page.intValue != 0) {
                        BorderStroke(1.dp, UrielOrange)
                    } else {
                        BorderStroke(1.dp, UrielBorderGray)
                    }
                } else {
                    if (page.intValue < maxPage) {
                        BorderStroke(1.dp, UrielOrange)
                    } else {
                        BorderStroke(1.dp, UrielBorderGray)
                    }
                }, RoundedCornerShape(8.dp)
            )
            .then(modifier ?: Modifier),
    ){
        Icon(
            imageVector =
                if(isLeft) Icons.AutoMirrored.Filled.KeyboardArrowLeft
                else Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "이동",
            tint =
                if(isLeft) {
                    if(page.intValue != 0)
                        UrielOrange
                    else UrielBorderGray
                }
                else {
                    if(page.intValue < maxPage)
                        UrielOrange
                    else UrielBorderGray
                }
            ,
            modifier = Modifier
                .size(60.dp)
        )
    }
}