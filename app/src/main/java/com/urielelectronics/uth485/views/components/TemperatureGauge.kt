package com.urielelectronics.uth485.views.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.R.drawable.thermometer
import com.urielelectronics.uth485.ui.theme.UrielBGBeige2
import com.urielelectronics.uth485.ui.theme.UrielGradientDarkOrange
import com.urielelectronics.uth485.ui.theme.UrielGradientLightOrange
import com.urielelectronics.uth485.ui.theme.UrielIconGray
import com.urielelectronics.uth485.ui.theme.UrielOrange
import com.urielelectronics.uth485.ui.theme.UrielTableHeaderGray
import com.urielelectronics.uth485.views.MyViewModel

@Composable
fun TemperatureGauge (
    title : String,
    value : Int,
    size : Dp = 320.dp,
    viewModel : MyViewModel
) {

    Box(
        Modifier
            .clip(CircleShape)
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        val sweepWidth = 32.dp
        Canvas(modifier = Modifier
            .size(size)
            .padding(sweepWidth / 2)
            .graphicsLayer( rotationZ =  -90f )) {

            val maxValue = viewModel.V_H // 다이얼 게이지의 최댓값 = 기본값 설정의 V_H (최고온도)
            val sweepAngle = (value.toFloat() / maxValue).coerceIn(0f,1f) * 360f
            val gradientGauge = Brush.sweepGradient(
                colorStops = arrayOf(
                    0.0f to UrielGradientLightOrange,
                    0.25f to UrielGradientDarkOrange,
                    0.5f to UrielOrange,
                    0.75f to UrielGradientDarkOrange,
                    1.0f to UrielGradientLightOrange
                ),
                center = center)

            drawArc(
                color = Color(0xFFe9e9e9),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = (sweepWidth).toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                brush = gradientGauge,
                startAngle = 0f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = (sweepWidth).toPx(), cap = StrokeCap.Round)

            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize(0.8f)
                .padding(12.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFEFEFE),
                            UrielBGBeige2
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .shadow(elevation = 20.dp, shape = CircleShape, clip = false)
                    .clip(CircleShape)
                    .background(UrielTableHeaderGray),
                contentAlignment = Alignment.Center

            ){
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(thermometer),
                            contentDescription = "온도계",
                            modifier = Modifier.padding(8.dp),
                            tint = UrielIconGray
                        )
                        Text(
                            title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,)
                    }
                    Text(
                        "${value}°",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Medium,
                        style = TextStyle(
                            brush = Brush.verticalGradient(listOf(UrielGradientLightOrange, UrielOrange)),
                        ),
                        modifier = Modifier
                            .padding(start = 12.dp))
                }
            }
        }
    }
}