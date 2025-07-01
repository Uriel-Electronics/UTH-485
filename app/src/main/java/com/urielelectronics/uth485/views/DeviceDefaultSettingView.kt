package com.urielelectronics.uth485.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.urielelectronics.uth485.R.drawable.check_circle
import com.urielelectronics.uth485.R.drawable.visibility_off
import com.urielelectronics.uth485.R.drawable.visibility_on
import com.urielelectronics.uth485.ui.theme.UrielBGBeige
import com.urielelectronics.uth485.ui.theme.UrielBGBeige2
import com.urielelectronics.uth485.ui.theme.UrielBGDarkGray
import com.urielelectronics.uth485.ui.theme.UrielBGPaleWhite
import com.urielelectronics.uth485.ui.theme.UrielBGWhite
import com.urielelectronics.uth485.ui.theme.UrielBorderBeige
import com.urielelectronics.uth485.ui.theme.UrielBorderGray
import com.urielelectronics.uth485.ui.theme.UrielOrange
import com.urielelectronics.uth485.ui.theme.UrielTextDark
import com.urielelectronics.uth485.ui.theme.UrielTextGray
import com.urielelectronics.uth485.ui.theme.UrielTextOrange
import com.urielelectronics.uth485.views.components.Header
import com.urielelectronics.uth485.views.components.Popup
import com.urielelectronics.uth485.views.components.SaveButton
import java.nio.file.WatchEvent

enum class SettingMode {
    TempModeSetting,
    TimeModeSetting
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDefaultSettingView(viewState: MutableState<ViewState>, viewModel: MyViewModel) {
    var settingMode = remember { mutableStateOf(SettingMode.TempModeSetting)}
    var showSavePopUp = remember { mutableIntStateOf(CLOSE) }

    var rN = remember { mutableStateOf(viewModel.rN.toString() )}// rN
    var settingTemp = remember { mutableStateOf(viewModel.settingTemp.toString())} // 설정온도

    var V_H = remember { mutableStateOf(viewModel.V_H.toString())} // 최고온도
    var V_L = remember { mutableStateOf(viewModel.V_L.toString())} // 최저온도
    var IF = remember { mutableStateOf(viewModel.IF.toString())}// 온도편차
    var LY = remember { mutableStateOf(viewModel.LY.toString())}// 지연시간
    var Ht = remember { mutableStateOf(viewModel.Ht.toString())}// 과승온도
    var ES = remember { mutableStateOf(viewModel.ES.toString())}// 보정온도

    var intervalTime = remember { mutableStateOf(viewModel.intervalTime.toString())} // 시간주기
    var fisrtTime = remember { mutableStateOf(viewModel.fisrtTime.toString())} // 초기투입시간
    var step = remember { mutableIntStateOf(viewModel.step)} // 시간단계

    var tempErrMsgOn = remember { mutableStateOf<Boolean>(true) }
    var tempErrMsgFrequency = remember { mutableIntStateOf(5) }
    var timeErrMsgOn = remember { mutableStateOf<Boolean>(true) }
    var timeErrMsgFrequency = remember { mutableIntStateOf(5) }
    val errMsgFrequencyOptions = listOf<Int>(5, 10, 15, 30, 50, 60)

    Scaffold (
        topBar = {
            Header(
                title = "",
                content = {},
                viewState,
                isBack = true
            )
        }
    ) { headerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(UrielBGBeige)
            .padding(headerPadding),
            contentAlignment = Alignment.Center

        ) {
            Column(
                Modifier
                    .fillMaxWidth(0.6f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 1. 제목 & 페이지 버튼
                Row (
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text (
                        text =
                            if(settingMode.value == SettingMode.TempModeSetting) "온도모드 설정"
                            else "시간모드 설정",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = UrielOrange
                    )

                    // 모드 전환 버튼
                    Row (
                        Modifier
                            .width(128.dp)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MoveButton(
                            settingMode = settingMode,
                            isLeft = true,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(6.dp))
                        MoveButton(
                            settingMode = settingMode,
                            isLeft = false,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }


                // 2. 본문
                Column (
                    Modifier.padding(vertical = 6.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    EditSettingBox(
                        titles =
                            if (settingMode.value == SettingMode.TempModeSetting)
                                listOf("온도상한설정", "온도편차", "Delay 시간", "과승온도", "보정온도")
                            else
                                listOf("시간주기", "초기투입시간", "단계설정"),
                        contents =
                            if (settingMode.value == SettingMode.TempModeSetting)
                                // 온도모드 설정
                                listOf(
                                    {
                                        Row (
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 6.dp),
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                "최저온도",
                                                color = UrielOrange,
                                                fontWeight = FontWeight.Medium
                                            )
                                            Box(
                                                Modifier.weight(1f)
                                            ){
                                                ValueTextField(
                                                    value = V_L.value,
                                                    onValueChange = { input -> onNumberChange(input, V_L) },
                                                    unit = "°C",
                                                    example = null,
                                                )
                                            }
                                            Text(
                                                text = "~  "
                                            )
                                            Text(
                                                text ="최고온도",
                                                color = UrielOrange,
                                                fontWeight = FontWeight.Medium
                                            )
                                            Box(
                                                Modifier.weight(1f)
                                            ){
                                                ValueTextField(
                                                    value = V_H.value,
                                                    onValueChange = { input -> onNumberChange(input, V_H) },
                                                    unit = "°C",
                                                    example = null
                                                )
                                            }
                                        }
                                    },
                                    {
                                        Box (Modifier
                                            .width(360.dp)) {
                                            ValueTextField(
                                                value = IF.value,
                                                onValueChange = { input -> onNumberChange(input, IF) },
                                                unit = "°C",
                                                example = "예) 0~5°C"
                                            )
                                        }
                                    },
                                    {
                                        Box (Modifier
                                            .width(360.dp)) {
                                            ValueTextField(
                                                value = LY.value,
                                                onValueChange = { input -> onNumberChange(input, LY) },
                                                unit = "초",
                                                example = "예) 1~60초"
                                            )
                                        }
                                    },
                                    {
                                        Box (Modifier.width(360.dp)) {
                                            ValueTextField(
                                                value = Ht.value,
                                                onValueChange = { input -> onNumberChange(input, Ht) },
                                                unit = "°C",
                                                example = "예) -20~80°C"
                                            )
                                        }
                                    },
                                    {
                                        Box (Modifier.width(360.dp)) {
                                            ValueTextField(
                                                value = ES.value,
                                                onValueChange = { input -> onNumberChange(input, ES) },
                                                unit = "°C",
                                                example = "예) -50~50°C"
                                            )
                                        }
                                    }
                                )
                            else
                                // 시간 모드 설정
                                listOf(

                                    {
                                        Box (Modifier.width(400.dp)) {
                                            ValueTextField(
                                                value = intervalTime.value,
                                                onValueChange = { input -> onNumberChange(input, intervalTime) },
                                                unit = "분",
                                                example = "예) 1~60분"
                                            )
                                        }
                                    },
                                    {
                                        Box (Modifier.width(360.dp)) {
                                            ValueTextField(
                                                value = fisrtTime.value,
                                                onValueChange = { input -> onNumberChange(input, fisrtTime) },
                                                unit = "초",
                                                example = "예) 1~60초"
                                            )
                                        }
                                    },
                                    {
                                        NumberButtonTable(
                                            value = step.value,
                                            onValueChange = { step.value = it }
                                        )
                                    }
                                )

                    )

                    // 3. 에러메세지 발생 주기
                    EditSettingBox(
                        titles = listOf("에러메세지", "발생주기"),
                        contents =
                            listOf(
                            {
                                    // 켜짐 / 꺼짐 버튼
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            Modifier
                                                .background(
                                                    color =
                                                        if ((settingMode.value == SettingMode.TempModeSetting && tempErrMsgOn.value) ||
                                                            (settingMode.value == SettingMode.TimeModeSetting && timeErrMsgOn.value))
                                                            UrielBGDarkGray
                                                        else UrielBGWhite,
                                                    shape = CircleShape
                                                )
                                                .clip(CircleShape)
                                                .border(
                                                    width = 1.dp,
                                                    color = UrielBorderGray,
                                                    shape = CircleShape
                                                )
                                                .clickable(onClick = {
                                                    if(settingMode.value == SettingMode.TempModeSetting) tempErrMsgOn.value = true
                                                    else if (settingMode.value == SettingMode.TimeModeSetting) timeErrMsgOn.value = true})
                                                .size(36.dp),
                                            contentAlignment = Alignment.Center

                                        ){
                                            if ((settingMode.value == SettingMode.TempModeSetting && tempErrMsgOn.value) ||
                                                (settingMode.value == SettingMode.TimeModeSetting && timeErrMsgOn.value)) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                contentDescription = "켜짐",
                                                tint = UrielBGWhite,
                                                modifier = Modifier
                                                    .size(18.dp),
                                                )
                                            }
                                        }
                                        Spacer(Modifier.width(6.dp))
                                        Text("켜짐",
                                            fontWeight = FontWeight.Medium)

                                        Spacer(Modifier.width(24.dp))

                                        // 꺼짐
                                        Box(
                                            Modifier
                                                .background(
                                                    color = if ((settingMode.value == SettingMode.TempModeSetting && !tempErrMsgOn.value) ||
                                                        (settingMode.value == SettingMode.TimeModeSetting && !timeErrMsgOn.value)) UrielBGDarkGray else UrielBGWhite,
                                                    shape = CircleShape
                                                )
                                                .clip(CircleShape)
                                                .border(
                                                    width = 1.dp,
                                                    color = UrielBorderGray,
                                                    shape = CircleShape
                                                )
                                                .clickable(onClick = {
                                                    if(settingMode.value == SettingMode.TempModeSetting) tempErrMsgOn.value = false
                                                    else if (settingMode.value == SettingMode.TimeModeSetting) timeErrMsgOn.value = false })
                                                .size(36.dp),
                                            contentAlignment = Alignment.Center
                                            ){
                                            if ((settingMode.value == SettingMode.TempModeSetting && !tempErrMsgOn.value) ||
                                                (settingMode.value == SettingMode.TimeModeSetting && !timeErrMsgOn.value)) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "꺼짐",
                                                    tint = UrielBGWhite,
                                                    modifier = Modifier
                                                        .size(18.dp),
                                                )
                                            }
                                        }
                                        Spacer(Modifier.width(6.dp))
                                        Text("꺼짐",
                                            fontWeight = FontWeight.Medium)
                                    }
                            },
                            {   Row (verticalAlignment = Alignment.CenterVertically) {
                                    // "n분 마다" 드롭다운
                                    FrequencyDropdown(
                                        options = errMsgFrequencyOptions,
                                        selectedOption=
                                            if(settingMode.value == SettingMode.TempModeSetting) tempErrMsgFrequency.intValue
                                            else timeErrMsgFrequency.intValue
                                        ,
                                        onOptionSelected= {
                                            if(settingMode.value == SettingMode.TempModeSetting) tempErrMsgFrequency.intValue = it
                                            else timeErrMsgFrequency.intValue = it },
                                        modifier = Modifier.width(128.dp)
                                    )

                                    Spacer(Modifier.width(8.dp))
                                    Text("마다", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        )
                    )
                }


                // 4. 설정 / 닫기 버튼
                Row (
                    Modifier
                        .fillMaxWidth(0.7f)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SaveButton(
                        onButtonClick = {
                            // showSavePopUp.value = FAILURE
                            // if (TODO - 조건 추가)

                                showSavePopUp.value = SUCCESS
                                viewModel.V_L = V_L.value.toInt()
                                viewModel.V_H = V_H.value.toInt()
                                viewModel.IF = IF.value.toInt()
                                viewModel.LY = LY.value.toInt()
                                viewModel.Ht = Ht.value.toInt()
                                viewModel.ES = ES.value.toInt()
                                viewModel.intervalTime = intervalTime.value.toInt()
                                viewModel.fisrtTime = fisrtTime.value.toInt()
                                viewModel.step = step.value.toInt()

                                // viewModel.errMsgOn = errMsgOn.value
                                // viewModel.errMsgFrequency = errMsgFrequency.value
                                // TODO - 기본값설정 업데이트
                        },
                        text = "설정",
                        backgroundColor = UrielOrange,
                        icon = ImageVector.vectorResource(id = check_circle),
                        modifier = Modifier.weight(1f)
                    )
                    SaveButton(
                        onButtonClick = {
                            viewState.value = ViewState.DEVICE_CONNECTED
                        },
                        text = "닫기",
                        backgroundColor = UrielBGDarkGray,
                        icon = Icons.Default.Refresh,
                        modifier = Modifier.weight(1f)
                    )
                }

                // 설정 (저장) 팝업 동작
                if(showSavePopUp.intValue != CLOSE) {
                    Popup(
                        title = "기본값 설정",
                        content = {
                            Text(
                                text =
                                    if (showSavePopUp.intValue == SUCCESS) "설정되었습니다."
                                    else "설정값을 다시 확인해주세요.",
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        },
                        confirmText = "확인",
                        type = "alarm",
                        onConfirm = { showSavePopUp.intValue = CLOSE },
                        onDismiss = { showSavePopUp.intValue = CLOSE }
                    )
                }
            }
        }
    }
}


@Composable
fun MoveButton(
    settingMode : MutableState<SettingMode>,
    isLeft : Boolean,
    modifier: Modifier? = Modifier
) {
    IconButton(
        onClick = {
            if(isLeft) {
                if(settingMode.value == SettingMode.TimeModeSetting) {
                    settingMode.value = SettingMode.TempModeSetting
                }
            }
            else {
                if(settingMode.value == SettingMode.TempModeSetting) {
                    settingMode.value = SettingMode.TimeModeSetting
                }
            }
        },
        Modifier
            .border(
                if (isLeft) {
                    if (settingMode.value == SettingMode.TimeModeSetting) {
                        BorderStroke(1.dp, UrielOrange)
                    } else {
                        BorderStroke(1.dp, UrielBorderGray)
                    }
                } else {
                    if (settingMode.value == SettingMode.TempModeSetting) {
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
                if(isLeft) Icons.Outlined.KeyboardArrowLeft
                else Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "이동",
            tint =
                if(isLeft) {
                    if(settingMode.value == SettingMode.TimeModeSetting)
                        UrielOrange
                    else UrielBorderGray
                }
            else {
                    if(settingMode.value == SettingMode.TempModeSetting)
                        UrielOrange
                    else UrielBorderGray
                }
            ,
            modifier = Modifier
                .size(60.dp)
        )
    }
}

@Composable
fun EditSettingBox(
    titles : List<String>,
    contents : List<@Composable () -> Unit>,
    //value: String,
    //onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, UrielBorderBeige, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(UrielBGWhite)
    ) {
        (titles zip contents).forEachIndexed { index, (title, content) ->
            Row(
                modifier = Modifier
                    .defaultMinSize(minHeight = 32.dp)
                    .wrapContentHeight()
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 왼쪽: 제목
                Text(
                    text = title,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(UrielBGBeige2)
                        .padding(
                            start = 24.dp, end = 24.dp,
                            top = if (index == 0) 24.dp else 6.dp,
                            bottom = if (index == titles.lastIndex) 24.dp else 6.dp
                        ),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = UrielTextDark,
                    textAlign = TextAlign.Center
                )
                // 오른쪽: 입력 뷰
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight()
                        .background(UrielBGWhite)
                        .padding(
                            start = 36.dp, end = 36.dp,
                            top = if (index == 0) 24.dp else 6.dp,
                            bottom = if (index == contents.lastIndex) 24.dp else 6.dp
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    content()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValueTextField(
    value: String,
    onValueChange: (String) -> Unit,
    unit : String,
    example : String?,
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row (modifier = Modifier
                .fillMaxWidth(if (example == null) 1f else 0.65f),
            verticalAlignment = Alignment.CenterVertically
        ){
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight()
                    .border(1.dp, UrielBorderGray, RoundedCornerShape(36.dp)),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        innerTextField()
                    }
                }
            )
            Spacer(Modifier.width(6.dp))
            Text(unit,
                fontWeight = FontWeight.Medium)
        }
        Text(
            text = example ?: "",
            color = UrielTextGray,
            fontWeight = FontWeight.Medium,)
    }
}

@Composable
fun NumberButtonTable(
    value : Int,
    onValueChange: (Int) -> Unit
) {
    Column {
        Row {
            NumberButton(1, onValueChange = onValueChange, value)
            NumberButton(2, onValueChange = onValueChange, value)
            NumberButton(3, onValueChange = onValueChange, value)
            NumberButton(4, onValueChange = onValueChange, value)
            NumberButton(5, onValueChange = onValueChange, value)
        }
        Row {
            NumberButton(6, onValueChange = onValueChange, value)
            NumberButton(7, onValueChange = onValueChange, value)
            NumberButton(8, onValueChange = onValueChange, value)
            NumberButton(9, onValueChange = onValueChange, value)
            NumberButton(10, onValueChange = onValueChange, value)
        }
    }
}

@Composable
fun NumberButton(
    value: Int,
    onValueChange : (Int) -> Unit,
    selectedValue : Int
) {
    Button (
        onClick = { onValueChange(value) },
        Modifier
            .size(72.dp)
            .padding(4.dp),
        shape = CircleShape,
        border = BorderStroke(2.dp,
            if(value == selectedValue) UrielOrange else UrielBorderGray),
        colors = ButtonDefaults.buttonColors(
            containerColor = UrielBGPaleWhite,
            contentColor = if(value == selectedValue) UrielOrange else UrielBorderGray),
        contentPadding = PaddingValues(0.dp)
    ) { Text(
        text = value.toString(),
        fontSize = 20.sp,
    ) }
}


// 0만 입력된 textfield 에 숫자 입력시 덮어씌우는 입력 기능
fun onNumberChange(input : String, mutable : MutableState<String>) {
    val digits = input.filter { it.isDigit() }
    mutable.value = when {
        mutable.value == "0" && digits.isNotEmpty() -> {
            digits.takeLast(1)
        }
        else -> digits
    }
}


@Composable
fun FrequencyDropdown(
    options: List<Int> = listOf(5,10,15,30,50,60),
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {
        // 드롭다운 박스
        Row(
            modifier = Modifier
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .clip(RoundedCornerShape(4.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .then(modifier),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text =
                if(selectedOption == 60) "1시간"
                    else selectedOption.toString() + "분",
                fontSize = 18.sp)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(
                        if (expanded) 180f else 0f
                    )
            )
        }

        // 메뉴
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(UrielBGWhite)
                .border(1.dp, UrielBorderGray)
                .then(modifier),
        )  {
            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onOptionSelected(option)
                            expanded = false
                        }
                        .padding(vertical = 2.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        if(option == 60) "1시간"
                        else option.toString() + "분", fontSize = 18.sp)
                }
            }
        }

    }
}