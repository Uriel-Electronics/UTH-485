package com.urielelectronics.uth485.views

import android.annotation.SuppressLint
import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.urielelectronics.uth485.R

enum class ViewState {
    HOME,

    DEVICE_REGISTER,
    DEVICE_PW_SETTING,
    DEVICE_LOGIN,
    DEVICE_CONNECTED,

    DEVICE_DEFAULT_SETTING,
    DEVICE_TEMP_SETTING,
    DEVICE_TEMP_EACH_SETTING,
    DEVICE_TEMP_ALL_SETTING,
    DEVICE_TEMP_GROUP_SETTING,
    DEVICE_TIME_SETTING,
    DEVICE_STATUS,
    DEVICE_ERROR_STATUS,
    DEVICE_TEMP_GRAPH,

    FIND_DEVICE,

}

enum class SidePanel { None, Settings }

@SuppressLint("MissingPermission")
@Composable
fun HomeView(viewModel: MyViewModel) {
    var viewState: MutableState<ViewState> = remember { mutableStateOf(ViewState.HOME) }
    var isSettingOpen by remember { mutableStateOf(false) }

    if (viewState.value == ViewState.HOME) {
        Box(modifier = Modifier.fillMaxSize()) {

            // 1. 배경 이미지
            Image(
                painter = painterResource(id = R.drawable.main_view_01),
                contentDescription = "배경 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // 2. 연결 버튼
            Button(
                onClick = { /* TODO: 연결 동작 */ },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-90).dp)
                    .size(width = 280.dp, height = 80.dp),
                shape = RoundedCornerShape(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF32363D)
                )
            ) {
                Text(
                    text = "연결",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "이동",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            // 3. 설정 아이콘 또는 닫기 아이콘
            IconButton(
                onClick = { isSettingOpen = !isSettingOpen },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "설정",
                    tint = Color.Black,
                    modifier = Modifier.size(60.dp)
                )
            }

            // 4. 설정 사이드바 표시
            if (isSettingOpen) {
                SettingsSideBar(
                    onClose = { isSettingOpen = false },
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
fun SettingsSideBar(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(Color(0xFFF5F5F5))
            .zIndex(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // 상단: 닫기 아이콘 + 메뉴
            Column {
                // ❌ 닫기 버튼 (오른쪽 상단)
                Box(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "닫기",
                            modifier = Modifier.size(36.dp),
                            tint = Color.Black
                        )
                    }
                }

                // 메뉴 항목들
                Column(modifier = Modifier.padding(top = 8.dp),verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* TODO: 단말기 등록 */ },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("단말기 등록", fontSize = 18.sp)
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* TODO: 사용자 정보 설정 */ },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("사용자 정보 설정", fontSize = 18.sp)
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // 하단: 종료 버튼
            Button(
                onClick = { /* TODO: 앱 종료 */ },
                modifier = Modifier.fillMaxWidth().offset(y = (-60).dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32363D))
            ) {
                Text("종료", color = Color.White)
            }
        }
    }
}



