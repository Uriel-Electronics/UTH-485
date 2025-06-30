package com.urielelectronics.uth485.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.widget.TableLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.ui.theme.UrielBGBeige
import com.urielelectronics.uth485.ui.theme.UrielBGOrange
import com.urielelectronics.uth485.ui.theme.UrielBGPaleWhite
import com.urielelectronics.uth485.ui.theme.UrielBGWhite
import com.urielelectronics.uth485.ui.theme.UrielButtonOrange
import com.urielelectronics.uth485.ui.theme.UrielHeaderGray
import com.urielelectronics.uth485.ui.theme.UrielTableHeaderGray
import com.urielelectronics.uth485.ui.theme.UrielTextDark
import com.urielelectronics.uth485.ui.theme.UrielTextGray
import com.urielelectronics.uth485.ui.theme.UrielTextLight
import com.urielelectronics.uth485.ui.theme.UrielTextOrange


class Device(var id: Int, var name: String, var group: Int)

@SuppressLint("MissingPermission")
@Composable
fun DeviceRegisterView(viewState: MutableState<ViewState>, viewModel: MyViewModel) {
    var deviceCount by remember { mutableIntStateOf(viewModel.deviceNumber) }
    var groupCount by remember { mutableIntStateOf(0) }
    val devices = remember { mutableStateListOf<Device>() }
    var showSavePopUp = remember { mutableStateOf(false) }

    LaunchedEffect(deviceCount) {
        // 늘려야 할 때
        if (devices.size < deviceCount) {
            repeat(deviceCount - devices.size) {
                devices.add(Device(
                    id = devices.size + 1,
                    name = (devices.size + 1).toString(),
                    group = 0
                ))
            }
        }
        // 줄여야 할 때
        else if (devices.size > deviceCount) {
            repeat(devices.size - deviceCount) {
                devices.removeAt(deviceCount)
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

                        Table(
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
                                .clip(RoundedCornerShape(16.dp))
                            , maxGroup = groupCount
                        )

                        // 2. 저장 버튼
                        Button(
                            onClick = {
                                //TODO - viewModel.devieNumber 업데이트
                                viewModel.deviceNumber = deviceCount

                                //TODO - popup 띄우기
                                showSavePopUp.value = true
                            },
                            modifier = Modifier
                                .wrapContentSize(),
                            shape = RoundedCornerShape(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = UrielButtonOrange
                            )
                        ) {
                            Text(
                                text = "저장",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = UrielTextLight,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "saveIcon",
                                tint = UrielTextLight,
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(horizontal = 8.dp)
                            )
                        }
                        if(showSavePopUp.value) {
                            Popup(
                                title = "단말기 등록",
                                content = { Text(
                                    text = "저장이 완료되었습니다.",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                ) },
                                confirmText = "확인",
                                type= "alarm",
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

@Composable
fun NumberSelector(
    value: Int,
    onValueChange: (Int) -> Unit,
    min : Int,
    max : Int = -1
) {

    Row (
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .height(60.dp),
    ) {
        // 1) 숫자 필드
        OutlinedTextField(
            value = value.toString(),
            onValueChange = { new ->
                new.toIntOrNull()?.let { v ->
                    if(max >= 0) {
                        onValueChange(v.coerceIn(min..max))
                    }
                    else {
                        onValueChange(v.coerceAtLeast(min))
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .width(180.dp)
                .height(60.dp),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 24.sp
            )
        )
        // 2) 위/아래 버튼
        Column(
            modifier = Modifier
                .padding(end = 4.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                UrielButtonOrange,
                                UrielBGOrange
                            )
                        )
                    )
                    .clickable {
                        if(max >= 0) {
                            val next = (value + 1).coerceAtMost(max)
                            onValueChange(next)
                        }
                        else {
                            val next = (value + 1)
                            onValueChange(next)
                        }
                    }
            ) {
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = "증가",
                    tint = UrielTextLight,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                UrielButtonOrange,
                                UrielBGOrange
                            )
                        )
                    )
                    .clickable {
                        val prev = (value - 1).coerceAtLeast(min)
                        onValueChange(prev)
                    }
            ) {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "감소",
                    tint = UrielTextLight,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Table(
    headers : List<String>,
    rows : List<Device>,
    weights : List<Float>,
    modifier: Modifier,
    maxGroup : Int
) {
    val scrollState = rememberScrollState()
    var showNamePopUp = remember { mutableStateOf(false) }
    var showGroupPopUp = remember { mutableStateOf(false) }
    var selectedDevice = remember { mutableStateOf<Device>(Device(0,"",0)) }


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
                                                selectedDevice.value = device
                                                showNamePopUp.value = true // 이름 변경 팝업 표시
                                            } else if(colIdx == 2) {
                                                selectedDevice.value = device
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
            oldName = selectedDevice.value.name,
            onDismiss = { showNamePopUp.value = false },
            onNameChanged = { newName ->
                selectedDevice.value.name = newName  // 새로운 이름 즉시 반영
            }
        )
    }
    else if(showGroupPopUp.value) {
        EditDeviceGroupPopUp (
            oldGroup = selectedDevice.value.group.toString(),
            onDismiss = { showGroupPopUp.value = false },
            onGroupChanged = { newGroup ->
                selectedDevice.value.group = newGroup  // 새로운 그룹 즉시 반영
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