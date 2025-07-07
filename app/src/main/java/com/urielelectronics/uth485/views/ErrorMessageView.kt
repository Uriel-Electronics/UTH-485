package com.urielelectronics.uth485.views

import android.widget.CalendarView
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.urielelectronics.uth485.R.drawable.calendar_month
import com.urielelectronics.uth485.ui.theme.UrielBGBeige3
import com.urielelectronics.uth485.ui.theme.UrielBGWhite
import com.urielelectronics.uth485.ui.theme.UrielBorderGray
import com.urielelectronics.uth485.ui.theme.UrielOrange
import com.urielelectronics.uth485.ui.theme.UrielTableHeaderBeige
import com.urielelectronics.uth485.ui.theme.UrielTextDark
import com.urielelectronics.uth485.ui.theme.UrielTextGray
import com.urielelectronics.uth485.views.components.Header
import com.urielelectronics.uth485.views.components.Table
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.collections.plus

data class formattedErrorLog (
    val date : String,
    val circuit : String,
    val msg : String,
    val time : String
)

val errorMsgList = listOf<ErrorLog>(
    ErrorLog(circuit = "0003", msg = "통신에러", date = cal(2025, 7, 14, 23, 35, 21)),
    ErrorLog(circuit = "0003", msg = "통신에러", date = cal(2025, 7, 14, 11, 35, 21)),
    ErrorLog(circuit = "0003", msg = "통신에러", date = cal(2025, 7, 15, 13, 35, 21)),
    ErrorLog(circuit = "0003", msg = "통신에러", date = cal(2025, 7, 15, 14, 34, 25)),
    ErrorLog(circuit = "0003", msg = "통신에러", date = cal(2025, 7, 15, 15, 15, 51)),
    ErrorLog(circuit = "0003", msg = "통신에러", date = cal(2025, 7, 16, 1, 33, 21)),
)




fun cal(
    year : Int,
    month : Int,
    day : Int,
    hour : Int = 0,
    min : Int = 0,
    sec : Int = 0,
    mil : Int = 0) : Long {
    val cal = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month-1)     // 0-기반: JAN=0, JUL=6
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, hour)          // 24h
        set(Calendar.MINUTE, min)
        set(Calendar.SECOND, sec)
        set(Calendar.MILLISECOND, mil)
    }
    val customMillis: Long = cal.timeInMillis
    return customMillis
}



@Composable
fun ErrorMessageView (viewState: MutableState<ViewState>, viewModel: MyViewModel) {
    var formattedErrorMsgList : List<formattedErrorLog> by remember {
        mutableStateOf(
            errorMsgList.toList().map { item ->
                formattedErrorLog(
                    date = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA).format(item.date),
                    circuit = item.circuit,
                    msg = item.msg,
                    time = SimpleDateFormat("a hh:mm:ss", Locale.KOREA).format(item.date)
                )
            }
        )
    }
    var inputList : List<formattedErrorLog> by remember {
        mutableStateOf(formattedErrorMsgList) }

    Scaffold(
        topBar = {
            Header(
                title = "에러메세지",
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
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                ) {
                SearchBox(
                    onSearchClick = { dateStart, dateEnd ->
                        // TODO - 검색기간 - 검색 버튼 클릭시 동작
                        inputList = errorMsgList.filter { item ->
                            item.date in dateStart..dateEnd
                        }.toList().map { item ->
                            formattedErrorLog(
                                date = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA).format(item.date),
                                circuit = item.circuit,
                                msg = item.msg,
                                time = SimpleDateFormat("a hh:mm:ss", Locale.KOREA).format(item.date)
                            )
                        }
                    },
                    onDeleteClick = { circuitStart, circuitEnd ->
                        // TODO - 회로설정 - 삭제 버튼 클릭시 동작
                    }
                )
                Table(
                    headers = listOf("", "일자", "회로", "에러메세지", "발생시간"),
                    rows = inputList,
                    weights = listOf(1f, 3f, 2f, 7f, 3f),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 12.dp, horizontal = 58.dp)
                        .border(
                            width = 1.dp,
                            color = UrielTextGray,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp)),
                    viewModel = viewModel,
                    type = "error"
                )
            }
        }
    }
}


@Composable
fun SearchBox(
    onSearchClick : (Long, Long) -> Unit,
    onDeleteClick : (String, String) -> Unit
) {
    var dateStart by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var dateEnd by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var circuitStart by remember { mutableStateOf("") }
    var circuitEnd by remember { mutableStateOf("") }


    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(vertical = 12.dp, horizontal = 48.dp)
            .border(1.dp, UrielBorderGray, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(UrielTableHeaderBeige)

    ) {
        Column (
            Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
                ){
                Text(
                    text = "검색기간",
                    Modifier.padding(horizontal = 12.dp)
                )

                DatePickerField(
                    modifier = Modifier
                        .width(240.dp)
                        .height(48.dp),
                    initialDate = dateStart,
                    onDateSelected = { dateStart = it },
                    type = "start"
                )
                Text(
                    text = "부터"
                )
                DatePickerField(
                    modifier = Modifier
                        .width(240.dp)
                        .height(48.dp),
                    initialDate = dateEnd,
                    onDateSelected = { dateEnd = it },
                    type = "end"
                )
                Text(
                    text = "까지"
                )
                Box (
                    Modifier
                        .width(64.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(UrielBGWhite)
                        .border(1.dp, UrielOrange, RoundedCornerShape(32.dp))
                        .clickable(onClick = {
                            onSearchClick(dateStart, dateEnd)
                        }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "검색",
                        color = UrielOrange
                    )
                }
            }

            HorizontalDivider(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                color = UrielBorderGray,
                thickness = 1.dp
            )

            Row(Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "회로설정",
                    Modifier.padding(horizontal = 12.dp)
                )
                Text(
                    text = "시작",

                )
                Box(
                    Modifier.width(128.dp)
                ) {
                    ValueTextField(
                        value = circuitStart,
                        onValueChange = { circuitStart = it },
                        unit = null,
                        example = null
                    )
                }
                Text(
                    text = "종료",

                )
                Box(
                    Modifier.width(128.dp)
                ) {
                    ValueTextField(
                        value = circuitEnd,
                        onValueChange = { circuitEnd = it },
                        unit = null,
                        example = null
                    )
                }
                Box (
                    Modifier
                        .width(64.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(UrielBGWhite)
                        .border(1.dp, UrielTextDark, RoundedCornerShape(32.dp))
                        .clickable(onClick = {
                            onDeleteClick(circuitStart, circuitEnd)
                        }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "삭제",
                        color = UrielTextDark
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    modifier: Modifier = Modifier,
    initialDate: Long = System.currentTimeMillis(),
    dateFormat: String = "yyyy년 M월 d일",
    onDateSelected: (Long) -> Unit = {},
    type : String = "start" // "start" or "end
) {


    // 1) 상태: 선택된 날짜, 다이얼로그 표시 여부
    var selectedMillis by remember { mutableStateOf(initialDate) }
    var expanded by remember { mutableStateOf(false) }

    // 2) 표시용 문자열
    var dateText = remember(selectedMillis) {
        SimpleDateFormat(dateFormat, Locale.getDefault())
            .format(Date(selectedMillis))
    }

    // 5) 드롭다운처럼 보이는 읽기 전용 필드
    Box (
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(36.dp))
                .background(UrielBGWhite)
                .border(1.dp, UrielBorderGray, RoundedCornerShape(36.dp))
                .padding(horizontal = 12.dp, vertical = 0.dp)
        ) {
            BasicTextField(
                value = dateText,
                onValueChange = { /* 읽기 전용 */ },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        innerTextField()
                    }
                }
            )
            Icon(
                imageVector = ImageVector.vectorResource(calendar_month),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(36.dp))
                .clickable { expanded = true }
        )
        if(expanded) {
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(0, 72),   // 필드 바로 아래
                onDismissRequest = { expanded = false },
            ) {

                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, UrielBorderGray),
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .width(300.dp)
                        .height(320.dp)
                ) {

                    Column(
                        Modifier
                            .wrapContentSize()
                            .padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = { expanded = false },
                        ) {
                            Icon(Icons.Default.Close,
                                contentDescription = null)
                        }
                        AndroidView(
                            modifier = Modifier
                                .size(width = 280.dp, height = 240.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface),
                            factory = { ctx ->
                                CalendarView(ctx).apply {
                                    date = initialDate
                                    setOnDateChangeListener { _, year, month, dayOfMonth ->
                                        // month는 0~11
                                        val cal = Calendar.getInstance().apply {
                                            set(year, month, dayOfMonth)
                                            if(type == "start") {
                                                set(Calendar.HOUR_OF_DAY, 0)
                                                set(Calendar.MINUTE,      0)
                                                set(Calendar.SECOND,      0)
                                                set(Calendar.MILLISECOND, 0)
                                            }
                                            else {
                                                set(Calendar.HOUR_OF_DAY, 23)
                                                set(Calendar.MINUTE,      59)
                                                set(Calendar.SECOND,      59)
                                                set(Calendar.MILLISECOND, 59)
                                            }
                                        }
                                        selectedMillis = cal.timeInMillis
                                        onDateSelected(selectedMillis)
                                    }
                                }
                            },
                        )

                    }
                }
            }
        }
    }

}

/*
*pickerState.selectedDateMillis?.let { ms ->
                            selectedMillis = ms
                            onDateSelected(ms)
                        }
* */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InlineDatePickerField(
    modifier: Modifier = Modifier,
    initialDate: Long = System.currentTimeMillis(),
    dateFormat: String = "yyyy년 M월 d일",
    onDateSelected: (Long) -> Unit = {}
) {
    var selectedMillis by remember { mutableStateOf(initialDate) }
    var expanded by remember { mutableStateOf(false) }
    val dateText = remember(selectedMillis) {
        SimpleDateFormat(dateFormat, Locale.getDefault())
            .format(Date(selectedMillis))
    }
    val pickerState = rememberDatePickerState(initialSelectedDateMillis = selectedMillis)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        // 읽기전용 텍스트필드
        OutlinedTextField(
            value = dateText,
            onValueChange = { /* no-op */ },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()  // 메뉴의 anchor 역할
                .fillMaxWidth(),
            label = { Text("날짜 선택") }
        )

        // 팝업 메뉴 영역에 달력과 확인/취소 버튼 배치
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                // 메뉴 전체 크기 고정
                .requiredWidth(320.dp)
                .requiredHeight(360.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            AndroidView(
                modifier = Modifier
                    .size(width = 280.dp, height = 300.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface),
                factory = { ctx ->
                    CalendarView(ctx).apply {
                        date = initialDate
                        setOnDateChangeListener { _, year, month, dayOfMonth ->
                            // month는 0~11
                            val cal = Calendar.getInstance().apply {
                                set(year, month, dayOfMonth)
                            }
                            onDateSelected(cal.timeInMillis)
                        }
                    }
                }
            )


            // 확인 / 취소 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { expanded = false }) {
                    Text("취소")
                }
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { ms ->
                        selectedMillis = ms
                        onDateSelected(ms)
                    }
                    expanded = false
                }) {
                    Text("확인")
                }
            }
        }
    }
}