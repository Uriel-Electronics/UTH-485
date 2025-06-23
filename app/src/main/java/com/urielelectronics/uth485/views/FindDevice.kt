package com.urielelectronics.uth485.views

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.urielelectronics.uth485.MainActivity
import com.urielelectronics.uth485.R
import com.urielelectronics.uth485.ui.theme.Pretendard
import com.urielelectronics.uth485.ui.theme.UrielBGPaleWhite
import com.urielelectronics.uth485.ui.theme.UrielBGWhite
import com.urielelectronics.uth485.ui.theme.UrielTextDark
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FindDevice(viewState: MutableState<ViewState>, foundDevices: MutableState<List<BluetoothDevice>>, selectedDevice: MutableState<BluetoothDevice?>, selectedGatt: MutableState<BluetoothGatt?>, selectedCharacteristic: MutableState<BluetoothGattCharacteristic?>, selectedNotification: MutableState<BluetoothGattCharacteristic?>, mainActivity: MainActivity, viewModel: MyViewModel) {

    val context = LocalContext.current
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter = bluetoothManager.adapter
    val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    val isScanning = remember { mutableStateOf(false) }
    var isLoading by viewModel.isConnectLoading

    var scannedDevices: ArrayList<String> = arrayListOf()
    val scrollState = rememberScrollState()

    var showDialog = remember { mutableStateOf(false) }
    var showPinDialog = remember { mutableStateOf(false) }

    val password = viewModel.password1.toString() + viewModel.password2.toString() + viewModel.password3.toString() + viewModel.password4.toString()
    Log.d("CurrentPW", password)

    val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    val scanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {

            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            val deviceAddress = result.device.address // MAC 주소
            val deviceName = result.device.name ?: "Unknown" // DEVICE NAME

            // 중복된 기기 무시
            if (scannedDevices.contains(deviceAddress)) {
                return
            }

            if (deviceName == "UTH-8DB") {
                val codes = deviceAddress.split(":")
                if (codes[0] == "94" && codes[1] == "C9") {
                    if (!foundDevices.value.any { it.address == deviceAddress }) {
                        foundDevices.value += result.device
//                        saveDeviceName(context, deviceAddress, deviceName)
                    }
                } else {
                    scannedDevices.add(deviceAddress)
                }
            }
        }
    }

    val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            // connected successfully
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // GATT서버에서 사용 가능한 서비스들을 비동기적으로 선택
                Log.d("BleManager", "연결 성공")
                gatt?.discoverServices()
                isLoading = false
//                viewModel.isConnectLoading.value = true
                // ...
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("BleManager", "연결 해제")
                gatt?.disconnect()
                // ....
                gatt?.close()
                isLoading = false
                viewState.value = ViewState.HOME
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                gatt.requestMtu(255) // MTU 크기 증가 요청
            }
        }

        // 장치에 대한 새로운 서비스가 발견되었을 때 호출되는 콜백
        @SuppressLint("MissingPermission")
        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int,status: Int) {
//            super.onServicesDiscovered(gatt, status)
            Log.d("BLE", "MTU changed to $mtu")
            // 원격 장치가 성공적으로 탐색된 경우
            if(status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BLETEST", "nice gatt success")
//                viewState.value = ViewState.DEVICE_VIEW

//                gatt.requestMtu(40) // MTU 크기 증가
                val service: BluetoothGattService? = gatt.getService(UUID.fromString("0000ddb0-0000-1000-8000-00805f9b34fb"))
                Log.d("BLE service", service.toString())
                val characteristic: BluetoothGattCharacteristic? = service?.getCharacteristic(UUID.fromString("0000ddb1-0000-1000-8000-00805f9b34fb"))
                Log.d("BLE characteristic", characteristic.toString())
                val notifyCharacteristic: BluetoothGattCharacteristic? = service?.getCharacteristic(
                    UUID.fromString("49535343-1e4d-4bd9-ba61-23c647249616"))
                if (notifyCharacteristic != null) {
                    gatt.setCharacteristicNotification(notifyCharacteristic, true)
                    val descriptor: BluetoothGattDescriptor? = notifyCharacteristic?.getDescriptor(
                        UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
                    )
                    descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    gatt.writeDescriptor(descriptor)

                    selectedGatt.value = gatt
                    selectedNotification.value = notifyCharacteristic
                }

                if (characteristic != null) {
                    CoroutineScope(Main).launch {
                        try {
                            delay(1000)
//                            requestPasswordData(gatt, characteristic)
//                            delay(1200)
                            selectedGatt.value = gatt
                            selectedCharacteristic.value = characteristic
                        } finally {
//                            viewModel.isConnectLoading.value = false
                        }

                    }
                } else {
                    Log.e("BLETEST", "No writable characteristic found")
                }
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BLETEST", "Data written successfully")
                mainActivity.handleCharacteristicUpdate(characteristic.value, viewModel)

//                sendNextChunk(gatt, characteristic)
            } else {
                Log.e("BLETEST", "Characteristic write failed, status: $status")
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            mainActivity.handleCharacteristicUpdate(characteristic.value, viewModel)
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mainActivity.handleCharacteristicUpdate(characteristic.value, viewModel)
            }
        }
    }

    // 스캔 시작
    if (!isScanning.value) {
        bluetoothLeScanner.startScan(null, scanSettings, scanCallback)
        isScanning.value = true
    }

    if (isLoading) {
        Box (modifier = Modifier.fillMaxSize().background(color = UrielBGWhite)) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.Gray
            )
        }
    } else {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = UrielBGWhite),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                Box(modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center) {
                    Text(text = "기기 찾기",
                       style = TextStyle(
                           fontFamily = Pretendard,
                           fontWeight = FontWeight.SemiBold,
                           fontSize = 18.sp,
                           color = UrielTextDark
                       ),
                        modifier = Modifier.padding(vertical = 13.dp)
                    )
                }

                if (foundDevices.value.isEmpty()) {
                    Box(modifier = Modifier
                        .padding(20.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(24.dp))
                        .background(color = UrielBGPaleWhite)
                        .aspectRatio(1.0F),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(painter = painterResource(id = R.drawable.logo_splash),
                                contentDescription = "logo",
                                modifier = Modifier.padding(40.dp))

                            Text(text = "주변의 기기를 찾는 중입니다...",
                                style = TextStyle(
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = UrielTextDark
                                ),
                                textAlign = TextAlign.Center
                            )

                            Text(text = "기기가 보이지 않는다면 기기가 설치되고\n" +
                                    "전원이 켜져있는지 확인해주세요.",
                                style = TextStyle(
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = UrielTextDark,
                                    lineHeight = 23.sp
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                } else {
                    Box(modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(color = UrielBGPaleWhite),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "기기를 찾았습니다.",
                                style = TextStyle(
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = UrielTextDark
                                ),
                                modifier = Modifier.padding(top = 40.dp)
                            )

                            Text(text = "기기 이름을 3초간 누르면 \n이름을 변경할 있습니다.",
                                style = TextStyle(
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = UrielTextDark
                                ),
                                modifier = Modifier.padding(top = 20.dp),
                            )

                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .verticalScroll(scrollState)
                            ) {
                                Column {
                                    foundDevices.value.forEach { device ->
                                        val customName = remember { mutableStateOf(getDeviceName(context, device.address)) }
                                        Box(modifier = Modifier
                                            .padding(top = 20.dp)
                                            .fillMaxWidth()
                                            .padding(all = 20.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(UrielTextDark)
                                            .combinedClickable (
                                                onClick = {
                                                    bluetoothAdapter
                                                        .getRemoteDevice(device.address)
                                                        .connectGatt(context, false, gattCallback)
                                                    showPinDialog.value = true
                                                    selectedDevice.value = device
                                                },
                                                onLongClick = {
                                                    showDialog.value = true // 다이얼로그 표시
                                                    selectedDevice.value = device
                                                }
                                            )
                                        ) {
                                            val codes = device.address
                                            val uuids = codes.split(":")
                                            Text(text = customName.value.ifEmpty { uuids[4].lowercase(Locale.ROOT) + uuids[5].lowercase(Locale.ROOT) },
                                                style = TextStyle(
                                                    fontFamily = Pretendard,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 20.sp,
                                                    color = UrielTextDark
                                                ),
                                                modifier = Modifier
                                                    .align(Alignment.Center)
                                                    .padding(vertical = 20.dp),
                                            )
                                        }

                                        // 다이얼로그 표시
                                        if (showDialog.value && selectedDevice.value == device) {
                                            EditDeviceNameDialog(
                                                context = context,
                                                macAddress = device.address,
                                                onDismiss = { showDialog.value = false },
                                                onNameChanged = { newName ->
                                                    customName.value = newName  // 새로운 이름 즉시 반영
                                                }
                                            )
                                        }

                                        if (showPinDialog.value && selectedDevice.value == device) {
                                            PinCodeDialog(
                                                onDismiss = { showPinDialog.value = false },
                                                onConfirm = { pinCode ->
                                                    // 입력된 PIN 코드 처리 로직
                                                    if (pinCode == password) {
                                                        Log.d("password", password)
                                                        Log.d("입력된 PIN 코드", pinCode)
                                                        showPinDialog.value = false
                                                        viewModel.isConnectLoading.value = true
                                                        CoroutineScope(Main).launch {
                                                            try {
                                                                delay(1000)
                                                                requestPowerData(selectedGatt.value!!, selectedCharacteristic.value!!)
                                                                delay(1200)

                                                                viewState.value = ViewState.DEVICE_CONNECTED
                                                            } finally {
                                                                viewModel.isConnectLoading.value = false
                                                            }
                                                        }
                                                    } else {
                                                        Log.d("WRONG", "PASSWORD WRONG")
                                                    }

                                                }
                                            )
                                        }

                                    }
                                }
                            }

                        }
                    }
                }

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinCodeDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var pinCode by remember { mutableStateOf("") }
    val focusRequesters = List(4) { FocusRequester() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("비밀번호 입력") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // 네 자리 숫자 입력 필드
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(4) { index ->
                        TextField(
                            value = pinCode.getOrNull(index)?.toString() ?: "",
                            onValueChange = { newValue ->
                                val newPinCode = StringBuilder(pinCode)
                                when {
                                    // 값이 비어있으면 해당 인덱스 값을 삭제
                                    newValue.isEmpty() -> {
                                        if (pinCode.length > index) {
                                            newPinCode.deleteCharAt(index)
                                            pinCode = newPinCode.toString()

                                            // 이전 필드로 포커스 이동
                                            if (index > 0) {
                                                focusRequesters[index - 1].requestFocus()
                                            }
                                        }
                                    }

                                    // 새 값이 숫자 하나일 때만 값 추가 또는 수정
                                    newValue.length == 1 && newValue.all { it.isDigit() } -> {
                                        if (pinCode.length > index) {
                                            newPinCode.setCharAt(index, newValue[0])
                                        } else {
                                            newPinCode.append(newValue)
                                        }
                                        pinCode = newPinCode.toString()

                                        // 입력 후 다음 필드로 이동
                                        if (index < 3) {
                                            focusRequesters[index + 1].requestFocus()
                                        }
                                    }
                                }
                            },
                            textStyle = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            maxLines = 1,
                            modifier = Modifier
                                .width(60.dp)
                                .height(80.dp)
                                .background(Color.Transparent)
                                .focusRequester(focusRequesters[index]),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.NumberPassword
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    if (index < 3) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                            ),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Black,
                                unfocusedIndicatorColor = Color.Black,
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (pinCode.length == 4) {
                        onConfirm(pinCode)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.Black),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("연결하기", color = Color.White)
            }
        }
    )
}

@Composable
fun EditDeviceNameDialog(
    context: Context,
    macAddress: String,
    onDismiss: () -> Unit,
    onNameChanged: (String) -> Unit
) {
    var newName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("기기 이름 변경") },
        text = {
            TextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("새 이름 입력") }
            )
        },
        confirmButton = {
            Button(onClick = {
                saveDeviceName(context, macAddress, newName)
                onNameChanged(newName)
                onDismiss()
            }) {
                Text("저장")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@OptIn(ExperimentalUnsignedTypes::class)
@SuppressLint("MissingPermission")
fun sendPacket(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, packet: UByteArray) {
    Log.d("BLETEST", "PACKET SENDING STARTED")

    characteristic.value = packet.toByteArray()
    val result = gatt.writeCharacteristic(characteristic)

    Log.d("BLETEST", "PACKET Sending ${result}")
}

@OptIn(ExperimentalUnsignedTypes::class)
fun requestPowerData(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
    val CHECKSUM: UInt = 175.toUByte() + 21.toUByte()
    val packet: UByteArray = ubyteArrayOf(175u, 21u, 0u,0u,0u,0u,0u,0u,0u,0u,0u,0u,0u, CHECKSUM.toUByte(), 13u, 10u)

    Log.d("REQPWD", packet.contentToString())

    sendPacket(gatt, characteristic, packet)
//    writeLargeData(gatt, characteristic, packet.asByteArray())
}

@OptIn(ExperimentalUnsignedTypes::class)
fun requestPasswordData(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
    val CHECKSUM: UInt = 175.toUByte() + 5.toUByte()
    val packet: UByteArray = ubyteArrayOf(175u, 5u, 0u,0u,0u,0u,0u,0u,0u,0u,0u,0u,0u, CHECKSUM.toUByte(), 13u, 10u)

    Log.d("REQPWD", packet.contentToString())

    sendPacket(gatt, characteristic, packet)
//    writeLargeData(gatt, characteristic, packet.asByteArray())
}

fun saveDeviceName(context: Context, macAddress: String, customName: String) {
    val sharedPreferences = context.getSharedPreferences("ble_devices", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString(macAddress, customName)  // MAC 주소 -> 사용자 지정 이름 매핑
        apply()
    }
}

fun getDeviceName(context: Context, macAddress: String): String {
    val sharedPreferences = context.getSharedPreferences("ble_devices", Context.MODE_PRIVATE)
    val name = macAddress.split(":")
    return sharedPreferences.getString(macAddress, name[4].lowercase(Locale.ROOT) + name[5].lowercase(
        Locale.ROOT)) ?: "Unknown Device"
}