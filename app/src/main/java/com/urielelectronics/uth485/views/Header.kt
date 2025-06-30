package com.urielelectronics.uth485.views


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.ui.theme.UrielHeaderGray
import com.urielelectronics.uth485.ui.theme.UrielTextDark

/** header **/
@Composable
fun Header(title : String,
           content: @Composable RowScope.() -> Unit,
           viewState: MutableState<ViewState>,
           isBack : Boolean = false) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(UrielHeaderGray),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        /** For other views with header **/
        if (isBack) {
            Back(viewState)
            Text(
                text = "UTH-485RC $title",
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = UrielTextDark
            )
        }
        /** For LandingView **/
        else {
            content()
        }
    }
}

/** 뒤로 가기 **/
@Composable
fun Back(viewState: MutableState<ViewState>) {
    IconButton(
        onClick = {
            /** viewState 로 화면을 전환하고 있어 강제로 viewState를 이전으로 되돌려서 뒤로가기 구현 **/
            viewState.value = ViewState.DEVICE_CONNECTED
        }
    ){
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "뒤로 가기",
            tint = UrielTextDark,
            modifier = Modifier
                .size(20.dp)
        )
    }
}