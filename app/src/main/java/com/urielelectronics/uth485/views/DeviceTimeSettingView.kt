package com.urielelectronics.uth485.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.ui.theme.UrielBGBeige
import com.urielelectronics.uth485.ui.theme.UrielBGWhite
import com.urielelectronics.uth485.views.components.ControlFooter
import com.urielelectronics.uth485.views.components.Header
import com.urielelectronics.uth485.views.components.NumberSelector
import com.urielelectronics.uth485.views.components.TemperatureGauge

@Composable
fun DeviceTimeSettingView (
    viewState: MutableState<ViewState>,
    viewModel: MyViewModel,
    group : Int
) {

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
                .background(UrielBGBeige),
            contentAlignment = Alignment.Center
        ) {
            // TODO - scrollable 하게 만들기
            Column (
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeekBlock(week = "헤더")
                WeekBlock(week = "월")
                WeekBlock(week = "화")
                WeekBlock(week = "수")
                WeekBlock(week = "목")
                WeekBlock(week = "금")
                WeekBlock(week = "토")
                WeekBlock(week = "일")
            }
        }
    }
}


@Composable
fun WeekBlock(
    week : String
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(72.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight()
                .background(UrielBGWhite)
                .clip(RoundedCornerShape(12.dp)),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = week,
                Modifier.weight(1f))
            Text(
                text = "N시 N분 부터",
                Modifier.weight(3f))
            Text(
                text = "N시 N분 까지",
                Modifier.weight(3f))
            Text(
                text = "ON / OFF",
                Modifier.weight(2f))
        }
    }
}