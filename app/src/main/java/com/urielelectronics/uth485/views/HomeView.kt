package com.urielelectronics.uth485.views

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.MainActivity
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

enum class ViewState {
    HOME,

    DEVICE_REGISTER,
    DEVICE_PW_SETTING,
    DEVICE_LOGIN,
    DEVICE_CONNECTED,

    DEVICE_DEFAULT_SETTING,
    DEVICE_TEMP_SETTING,
    DEVICE_TEMP_DEVICE_SETTING,
    DEVICE_TEMP_GLOBAL_SETTING,
    DEVICE_TEMP_GROUP_SETTING,

    DEVICE_TIME_SETTING,
    DEVICE_TIME_GROUP_SETTING,
    DEVICE_TIME_GLOBAL_SETTING,

    DEVICE_STATUS,
    DEVICE_ERROR_STATUS,

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
        LandingView(viewState = viewState, viewModel = viewModel)
    }
    else if (viewState.value == ViewState.DEVICE_REGISTER) {
        DeviceRegisterView(viewState = viewState, viewModel = viewModel)
    }
    else if (viewState.value == ViewState.DEVICE_DEFAULT_SETTING) {
        DeviceDefaultSettingView(viewState = viewState, viewModel = viewModel)
    }
    else if (viewState.value == ViewState.DEVICE_TEMP_SETTING
        || viewState.value == ViewState.DEVICE_TEMP_DEVICE_SETTING
        || viewState.value == ViewState.DEVICE_TIME_SETTING) {
        TempSettingView(viewState = viewState, viewModel = viewModel)
    }
    else if (viewState.value == ViewState.DEVICE_TEMP_GROUP_SETTING
        || viewState.value == ViewState.DEVICE_TIME_GROUP_SETTING) {
        GroupTempSettingView(viewState = viewState, viewModel = viewModel)
    }
    else if (viewState.value == ViewState.DEVICE_TEMP_GLOBAL_SETTING) {
        GlobalTempSettingView(viewState = viewState, viewModel = viewModel)
    }
    else if(viewState.value == ViewState.DEVICE_TIME_GLOBAL_SETTING) {
        DeviceTimeSettingView(viewState, viewModel, type = "global", 0)
    }
    else if (viewState.value == ViewState.DEVICE_STATUS) {
        StatusCheckView(viewState = viewState, viewModel = viewModel)
    }
    else if (viewState.value == ViewState.DEVICE_ERROR_STATUS) {
        ErrorMessageView(viewState = viewState, viewModel = viewModel)
    }
    else if (viewState.value == ViewState.DEVICE_PW_SETTING) {
        PasswordSettingView(viewState = viewState, viewModel = viewModel)
    }
}





