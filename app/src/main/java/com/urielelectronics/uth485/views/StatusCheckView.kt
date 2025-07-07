package com.urielelectronics.uth485.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.ui.theme.UrielBGBeige2
import com.urielelectronics.uth485.ui.theme.UrielBGBeige3
import com.urielelectronics.uth485.ui.theme.UrielBGWhite
import com.urielelectronics.uth485.ui.theme.UrielTableHeaderGray
import com.urielelectronics.uth485.ui.theme.UrielTextGray
import com.urielelectronics.uth485.ui.theme.UrielTextOrange
import com.urielelectronics.uth485.views.components.Header
import com.urielelectronics.uth485.views.components.Table
import java.sql.Time
import java.util.Date

data class ErrorLog (
    val date : Long,
    val circuit : String,
    val msg : String,
)

@Composable
fun StatusCheckView (viewState: MutableState<ViewState>, viewModel: MyViewModel) {
    var deviceList = viewModel.deviceList

    Scaffold(
        topBar = {
            Header(
                title = "상태확인",
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
                .background(UrielBGBeige3),
            contentAlignment = Alignment.Center
        ) {
            Table(
                headers = listOf("이름", "전원", "모드", "출력", "에러", "통신에러", "설정온도/시간", "현재온도", "시간주기"),
                rows = deviceList.toList(),
                weights = listOf(2f, 1f, 1f, 1f, 1f, 2f, 2.5f, 2f, 2f),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(48.dp)
                    .border(
                        width = 1.dp,
                        color = UrielTextGray,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp)),
                viewModel = viewModel,
                type = "status"
            )
        }
    }
}





