package com.urielelectronics.uth485.views

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.media.Image
import android.view.View
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.urielelectronics.uth485.MainActivity
import com.urielelectronics.uth485.R
import com.urielelectronics.uth485.ui.theme.Pretendard
import com.urielelectronics.uth485.ui.theme.UrielTextDark

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
    val foundDevices: MutableState<List<BluetoothDevice>> = remember { mutableStateOf(emptyList()) }
    val selectedDevice: MutableState<BluetoothDevice?> = remember { mutableStateOf(null) }
    val characteristic: MutableState<BluetoothGattCharacteristic?> = remember { mutableStateOf(null) }
    val notifyCharacteristic: MutableState<BluetoothGattCharacteristic?> = remember { mutableStateOf(null) }
    val gatt: MutableState<BluetoothGatt?> = remember { mutableStateOf(null) }

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
                onClick = { viewState.value = ViewState.FIND_DEVICE },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-60).dp)
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
        }
    } else if (viewState.value == ViewState.FIND_DEVICE) {
        FindDevice(viewState = viewState, foundDevices = foundDevices, selectedDevice, gatt, characteristic, notifyCharacteristic, mainActivity = MainActivity(), viewModel = viewModel)
    } else if (viewState.value == ViewState.DEVICE_CONNECTED) {
        // TODO! : 여기서부터 연결된 상태로 진입합니다.
        Text(text = "연결된 단말기의 수 : ${viewModel.deviceCount}",
            style = TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                fontSize = 50.sp,
                color = UrielTextDark
            )
        )

    }
}



