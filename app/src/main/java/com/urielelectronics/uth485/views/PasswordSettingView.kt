package com.urielelectronics.uth485.views

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.ui.theme.UrielBGBeige
import com.urielelectronics.uth485.ui.theme.UrielBGBeige2
import com.urielelectronics.uth485.ui.theme.UrielBGWhite
import com.urielelectronics.uth485.ui.theme.UrielTextDark
import com.urielelectronics.uth485.ui.theme.UrielTextGray
import com.urielelectronics.uth485.R.drawable.visibility_off
import com.urielelectronics.uth485.R.drawable.visibility_on
import com.urielelectronics.uth485.R.drawable.check_circle
import com.urielelectronics.uth485.ui.theme.UrielOrange
import com.urielelectronics.uth485.views.components.Header
import com.urielelectronics.uth485.views.components.Popup
import com.urielelectronics.uth485.views.components.SaveButton


const val CLOSE = 0
const val FAILURE = 1
const val SUCCESS = 2

@Composable
fun PasswordSettingView(viewState: MutableState<ViewState>, viewModel: MyViewModel) {
    var newPassword by remember { mutableStateOf("") }
    var newPasswordCheck by remember { mutableStateOf("") }
    var showSavePopUp = remember { mutableStateOf(CLOSE) }


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
            .padding(headerPadding)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {

                // 좌측 안내 문구 section
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(UrielBGWhite),
                    contentAlignment = Alignment.TopStart,
                ){
                    Text(
                        text = "사용자 정보 설정",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(32.dp))
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
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        PasswordInputField(
                            title = "비밀번호",
                            password= newPassword,
                            onPasswordChange = { newPassword = it }
                        )
                        PasswordInputField(
                            title = "비밀번호 확인",
                            password= newPasswordCheck,
                            onPasswordChange = { newPasswordCheck = it }
                        )

                        Spacer(
                            Modifier.height(36.dp)
                        )

                        // 2. 저장 버튼

                        SaveButton(
                            onButtonClick = {
                                showSavePopUp.value = FAILURE
                                if(newPassword == newPasswordCheck) {
                                    // if (TODO - 조건 추가 (최소길이, 특수문자, 대소문자 등등))
                                        showSavePopUp.value = SUCCESS
                                        // TODO - user password 업데이트
                                }
                            },
                            text = "저장",
                            backgroundColor = UrielOrange,
                            icon = ImageVector.vectorResource(id = check_circle)
                        )

                        if(showSavePopUp.value != CLOSE) {
                            Popup(
                                title = "사용자 정보 설정",
                                content = {
                                    Text(
                                        text =
                                            if (showSavePopUp.value == SUCCESS) "저장이 완료되었습니다."
                                            else "비밀번호를 다시 확인해주세요.",
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp
                                    )
                                },
                                confirmText = "확인",
                                type = "alarm",
                                onConfirm = { showSavePopUp.value = CLOSE },
                                onDismiss = { showSavePopUp.value = CLOSE }
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun PasswordInputField(
    title : String,
    password: String,
    onPasswordChange: (String) -> Unit
) {
    var visibility = remember { mutableStateOf<Boolean>(true) }


    Row(
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .height(84.dp)
            .padding(6.dp)
            .border(
                width = 1.dp,
                color = UrielTextGray,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
        ,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
        ){
        Box (
            modifier = Modifier
                .fillMaxHeight()
                .background(UrielBGBeige2)
                .weight(2f),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .padding(horizontal = 36.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = UrielTextDark,
                textAlign = TextAlign.Start,

                )
        }
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = { Text(title) },
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 24.sp
            ),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxHeight()
                .weight(3f)
                .background(UrielBGWhite),
            trailingIcon = {
                IconButton(onClick = {visibility.value = !visibility.value}) {
                    Icon(
                        imageVector = if (visibility.value) ImageVector.vectorResource(id = visibility_off)
                        else ImageVector.vectorResource(id = visibility_on),
                        contentDescription = "토글 비밀번호 표시"
                    )
                }
            },
            visualTransformation = if (visibility.value)
                VisualTransformation.None
            else
                PasswordVisualTransformation()
        )
    }
}