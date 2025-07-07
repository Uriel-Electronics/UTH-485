package com.urielelectronics.uth485.views

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.R.drawable.check_circle
import com.urielelectronics.uth485.ui.theme.UrielBGBeige
import com.urielelectronics.uth485.ui.theme.UrielBGWhite
import com.urielelectronics.uth485.ui.theme.UrielOrange
import com.urielelectronics.uth485.ui.theme.UrielTableHeaderGray
import com.urielelectronics.uth485.ui.theme.UrielTextDark
import com.urielelectronics.uth485.ui.theme.UrielTextGray
import com.urielelectronics.uth485.ui.theme.UrielTextOrange
import com.urielelectronics.uth485.views.components.Header
import com.urielelectronics.uth485.views.components.NumberSelector
import com.urielelectronics.uth485.views.components.Popup
import com.urielelectronics.uth485.views.components.SaveButton



@SuppressLint("MissingPermission")
@Composable
fun DeviceRegisterView(viewState: MutableState<ViewState>, viewModel: MyViewModel) {
    var deviceCount by remember { mutableIntStateOf(viewModel.deviceNumber) }
    var groupCount by remember { mutableIntStateOf(viewModel.groupCount) }
    var devices by remember { mutableStateOf(emptyList<Device>()) }
    var showSavePopUp = remember { mutableStateOf(false) }

    LaunchedEffect(viewState.value) {
        if(viewState.value == ViewState.DEVICE_REGISTER) {
            devices = viewModel.deviceList.toList()
        }
    }


    LaunchedEffect(deviceCount) {
        // 늘려야 할 때
        if (devices.size < deviceCount) {
            repeat(deviceCount - devices.size) {
                devices += Device(
                    id = devices.size + 1,
                    name = (devices.size + 1).toString(),
                    group = 0
                )
            }
        }
        // 줄여야 할 때
        else if (devices.size > deviceCount) {
            repeat(devices.size - deviceCount) {
                devices = devices.subList(0, deviceCount)
            }
        }
    }

    Scaffold (
        topBar = {
            Header(
                title = "단말기 등록",
                content = {},
                viewState,
                isBack = true
            )
        }
    ) { headerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(headerPadding)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {

                // 좌측 안내 문구 section
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(UrielBGWhite)
                ){
                    // 1. 안내 텍스트
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        IconAndText(text = "단말기 개수를 등록하세요")
                        IconAndText(text = "단말기의 그룹을 지정하세요")
                        IconAndText(text = "단말기 별도 이름을 바꾸어 주세요")
                    }
                }

                // 우측 메인 section
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(4f)
                        .background(UrielBGBeige)
                ){
                    Column(
                        modifier = Modifier
                        .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        // 단말기 수, 그룹 수 조정
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Text(
                                text = "단말기 수",
                                fontSize = 24.sp
                            )
                            NumberSelector(
                                value = deviceCount,
                                onValueChange = { deviceCount = it.coerceAtLeast(0) },
                                min = 0
                            )
                            Text(
                                text = "그룹 수",
                                fontSize = 24.sp
                            )
                            NumberSelector(
                                value = groupCount,
                                onValueChange = { groupCount = it.coerceAtLeast(0) },
                                min = 0
                            )
                        }

                        DeviceTable(
                            headers = listOf("단말기번호", "이름", "그룹분류"),
                            rows = devices,
                            weights = listOf(
                                2f, 5f, 3f
                            )
                            ,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(440.dp)
                                .padding(horizontal = 72.dp, vertical = 24.dp)
                                .border(
                                    width = 1.dp,
                                    color = UrielTextGray,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clip(RoundedCornerShape(16.dp)),
                            maxGroup = groupCount,
                        )

                        // 2. 저장 버튼
                        SaveButton(
                            onButtonClick = {
                                viewModel.deviceNumber = deviceCount

                                // TODO - viewModel.group, devices 업데이트
                                viewModel.groupCount = groupCount

                                viewModel.updateDeviceList(devices)
                                viewModel.updateDeviceGroup(groupCount)
                                // TODO

                                showSavePopUp.value = true
                            },
                            text = "저장",
                            backgroundColor = UrielOrange,
                            icon = ImageVector.vectorResource(id = check_circle)
                        )
                        if(showSavePopUp.value) {
                            Popup(
                                title = "단말기 등록",
                                content = {
                                    Text(
                                        text = "저장이 완료되었습니다.",
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp
                                    )
                                },
                                confirmText = "확인",
                                type = "alarm",
                                onConfirm = { showSavePopUp.value = false },
                                onDismiss = { showSavePopUp.value = false }
                            )
                        }
                    }

                }
            }
        }
    }

}

@Composable
fun IconAndText(text : String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,


    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "warningIcon",
            tint = UrielTextDark,
            modifier = Modifier
                .size(24.dp)
                .alignBy { it.measuredHeight },
        )
        Text(
            text = text,
            fontSize = 24.sp,
            color = UrielTextDark,
            modifier = Modifier
                .alignByBaseline(),
        )
    }
}




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeviceTable(
    headers: List<String>,
    rows: List<Device>,
    weights: List<Float>,
    modifier: Modifier,
    maxGroup: Int,
) {
    val scrollState = rememberScrollState()
    var showNamePopUp = remember { mutableStateOf(false) }
    var showGroupPopUp = remember { mutableStateOf(false) }
    var selectedDevice by remember { mutableStateOf<Device>(Device(0,"",0)) }


    Box(
        modifier = modifier) {
        Column {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(UrielTableHeaderGray)
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
                    rows.forEachIndexed { rowIdx, device ->
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                        ) {
                            listOf(device.id.toString(), device.name, device.group.toString())
                                .forEachIndexed { colIdx, cell ->
                                Box(modifier = Modifier
                                    .weight(weights[colIdx])
                                    .padding(8.dp)
                                    .combinedClickable(
                                        onClick = {
                                            // selectedDevice.value = device
                                        },
                                        onLongClick = {
                                            if(colIdx == 1) {
                                                selectedDevice = device
                                                showNamePopUp.value = true // 이름 변경 팝업 표시
                                            } else if(colIdx == 2) {
                                                selectedDevice = device
                                                showGroupPopUp.value = true // 이름 변경 팝업 표시
                                            }
                                        }
                                    ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cell,
                                        fontSize = 20.sp,
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
        // TODO - Custom ScrollBar
    }
    // 다이얼로그 표시
    if (showNamePopUp.value) {
        EditDeviceNamePopUp(
            oldName = selectedDevice.name,
            onDismiss = { showNamePopUp.value = false },
            onNameChanged = { newName ->
                selectedDevice.name = newName  // 새로운 이름 즉시 반영
            }
        )
    }
    else if(showGroupPopUp.value) {
        EditDeviceGroupPopUp (
            oldGroup = selectedDevice.group.toString(),
            onDismiss = { showGroupPopUp.value = false },
            onGroupChanged = { newGroup ->
                selectedDevice.group = newGroup  // 새로운 그룹 즉시 반영
            },
            maxGroup = maxGroup
        )
    }

}

@Composable
fun EditDeviceNamePopUp(
    oldName : String,
    onDismiss: () -> Unit,
    onNameChanged: (String) -> Unit
) {
    var newName by remember { mutableStateOf("") }

    Popup(
        title = "단말기 이름 변경",
        content = {
            TextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("새 이름 입력") }
            )
        },
        type = "confirm",
        confirmText = "확인",
        dismissText = "취소",
        onConfirm = {
            onNameChanged(newName)
            onDismiss()
        },
        onDismiss = {
            onDismiss()
        }
    )
}


@Composable
fun EditDeviceGroupPopUp(
    oldGroup : String,
    onDismiss: () -> Unit,
    onGroupChanged: (Int) -> Unit,
    maxGroup : Int
) {
    var newGroup by remember { mutableIntStateOf(0) }

    Popup(
        title = "단말기 그룹 변경",
        content = {
            NumberSelector(
                value = newGroup,
                onValueChange = { newGroup = it },
                min = 0,
                max = maxGroup
            )
        },
        type = "confirm",
        confirmText = "확인",
        dismissText = "취소",
        onConfirm = {
            onGroupChanged(newGroup)
            onDismiss()
        },
        onDismiss = {
            onDismiss()
        }
    )
}