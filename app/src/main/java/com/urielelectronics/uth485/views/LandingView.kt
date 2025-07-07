package com.urielelectronics.uth485.views

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.R
import com.urielelectronics.uth485.ui.theme.UrielBGOrange
import com.urielelectronics.uth485.ui.theme.UrielBGRed
import com.urielelectronics.uth485.ui.theme.UrielBGWhite
import com.urielelectronics.uth485.ui.theme.UrielTextDark
import com.urielelectronics.uth485.ui.theme.UrielTextGray
import com.urielelectronics.uth485.ui.theme.UrielTextLight
import com.urielelectronics.uth485.views.components.Header
import com.urielelectronics.uth485.views.components.Popup
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.system.exitProcess

@SuppressLint("ContextCastToActivity")
@Composable
fun LandingView(viewState : MutableState<ViewState>, viewModel: MyViewModel) {
    var showExitPopUp by remember { mutableStateOf(false) }
    val activity = (LocalContext.current as Activity)
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.land_view),
            contentDescription = "로고 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Header(
            "LandingView",
            {
                Image(
                    painter = painterResource(id = R.drawable.logo_splash_black),
                    contentDescription = "로고 이미지",
                    modifier = Modifier
                        .width(72.dp)
                        .padding(horizontal = 20.dp)
                )
                HeaderDropdown(
                    "단말기 등록",
                    onSelect = arrayOf({ viewState.value = ViewState.DEVICE_REGISTER })
                )
                HeaderDropdown(
                    "기본값 설정",
                    onSelect = arrayOf({ viewState.value = ViewState.DEVICE_DEFAULT_SETTING })
                )
                HeaderDropdown(
                    "온도 제어",
                    onSelect = arrayOf(
                        { viewState.value = ViewState.DEVICE_TEMP_SETTING },
                        { viewState.value = ViewState.DEVICE_TEMP_GROUP_SETTING },
                        { viewState.value = ViewState.DEVICE_TEMP_GLOBAL_SETTING }
                    ),
                    arrayOf("개별 제어", "그룹 제어", "전체 제어")
                )
                HeaderDropdown(
                    "예약 설정",
                    onSelect = arrayOf({ viewState.value = ViewState.DEVICE_TIME_SETTING })
                )
                HeaderDropdown(
                    "상태 확인",
                    onSelect = arrayOf({ viewState.value = ViewState.DEVICE_STATUS })
                )
                HeaderDropdown(
                    "에러 메세지",
                    onSelect = arrayOf({ viewState.value = ViewState.DEVICE_ERROR_STATUS })
                )
                HeaderDropdown(
                    "사용자 정보 설정",
                    onSelect = arrayOf({ viewState.value = ViewState.DEVICE_PW_SETTING })
                )
                HeaderDropdown(
                    "종료",
                    /* TODO: exit */
                    onSelect = arrayOf({
                        showExitPopUp = true
                    })
                )
            },
            viewState
        )



        // 시간 계산
        val nowFlow = remember {
            flow {
                while (true) {
                    emit(Calendar.getInstance())
                    delay(1_000L)
                }
            }
        }

        // 매초마다 Flow -> State
        val now by nowFlow.collectAsState(initial = Calendar.getInstance())

        // 날짜 포맷
        val dateText = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(now.time)

        // 시간 포맷
        val timeText = SimpleDateFormat("a h:mm", Locale.KOREA).format(now.time)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(UrielBGWhite)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = dateText,
                color = UrielTextDark,
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp))
            VerticalDivider(
                color = UrielTextGray,
                modifier = Modifier
                    .height(20.dp)
                    .padding(vertical = 4.dp, horizontal = 16.dp))
            Text(text = timeText,
                color = UrielTextDark,
                modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp))
        }


    }
    // 종료 팝업
    if(showExitPopUp) {
        Popup(
            title = "종료",
            content = {
                Text(
                    text = "앱을 종료하시겠습니까?",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            },
            confirmText = "확인",
            type = "confirm",
            onConfirm = {
                activity.finishAffinity()
                exitProcess(0)
            },
            onDismiss = { showExitPopUp = false }
        )
    }
}


// each dropdown components in header
@Composable
fun HeaderDropdown(
    title: String,
    onSelect : Array<() -> Unit>,
    items: Array<String> = emptyArray<String>()
) {
    var expanded by remember { mutableStateOf(false) }

    Box (
        modifier = Modifier
            .clickable {
                expanded = true
                /* 메뉴 클릭 처리 */
                if(items.isEmpty()){
                    onSelect[0]()
                }
            }
    ) {
        Text(
            text = title,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(UrielBGOrange)
                .padding(0.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .padding(0.dp)
            ) {
                items.forEachIndexed { idx, label ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            /* 세부 메뉴 클릭 처리 */
                            onSelect[idx]()
                        },
                        contentPadding = PaddingValues(0.dp),
                        text = {
                            Text(
                                label,
                                color = UrielTextLight,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        },
                    )
                    if(idx < items.lastIndex){
                        HorizontalDivider(
                            color = UrielBGRed,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}