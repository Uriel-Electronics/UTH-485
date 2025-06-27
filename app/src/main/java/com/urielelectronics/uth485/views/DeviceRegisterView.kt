package com.urielelectronics.uth485.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.R

@SuppressLint("MissingPermission")
@Composable
fun DeviceRegisterView(viewState: MutableState<ViewState>) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 1. 배경 이미지
        Image(
            painter = painterResource(id = R.drawable.land_view),
            contentDescription = "배경 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. 연결 버튼
        Button(
            onClick = { viewState.value = ViewState.DEVICE_CONNECTED },
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
                text = "돌아가기",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}