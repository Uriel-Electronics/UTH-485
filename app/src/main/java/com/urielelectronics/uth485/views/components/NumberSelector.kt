package com.urielelectronics.uth485.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.ui.theme.UrielBorderGray
import com.urielelectronics.uth485.ui.theme.UrielGradientDarkOrange
import com.urielelectronics.uth485.ui.theme.UrielGradientLightOrange
import com.urielelectronics.uth485.ui.theme.UrielTextLight

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
                                UrielGradientLightOrange,
                                UrielGradientDarkOrange
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
                                UrielGradientLightOrange,
                                UrielGradientDarkOrange
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

@Composable
fun NumberSelector2(
    value: Int,
    onValueChange: (Int) -> Unit,
    min : Int,
    max : Int = -1,
    activate : Boolean = false
) {

    Column (
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentSize(),
    ) {
        if(activate) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                UrielGradientLightOrange,
                                UrielGradientDarkOrange
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
        }
        // 1) 숫자 필드
        BasicTextField(
            value = if(value >= 10) value.toString() else "0$value",
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
            textStyle = LocalTextStyle.current.copy(
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            ),
            decorationBox = { innerTextField ->
                Box (
                    Modifier
                        .width(72.dp)
                        .height(45.dp)
                        .border(width = 1.dp, color = UrielBorderGray, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .padding(0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
            }

        )
        // 2) 위/아래 버튼
        if(activate) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                UrielGradientLightOrange,
                                UrielGradientDarkOrange
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