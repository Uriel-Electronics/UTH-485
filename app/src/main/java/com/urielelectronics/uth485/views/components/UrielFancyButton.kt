package com.urielelectronics.uth485.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.ui.theme.UrielBGWhite
import com.urielelectronics.uth485.ui.theme.UrielGradientDarkGray
import com.urielelectronics.uth485.ui.theme.UrielGradientLightGray
import com.urielelectronics.uth485.ui.theme.UrielGradientLightOrange
import com.urielelectronics.uth485.ui.theme.UrielIconGray
import com.urielelectronics.uth485.ui.theme.UrielOrange

@Composable
fun UrielFancyButton(
    text : String = "",
    icon : ImageVector = Icons.Default.Person,
    type : String = "icon",
    onClick : () -> Unit,
    onSelected : Boolean = false,
    size : Dp = 96.dp,
    fontSize : TextUnit = 24.sp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val gradientBackground = Brush.linearGradient(
        colors = listOf(UrielBGWhite, UrielGradientLightGray),
        start = Offset(0f,0f),
        end = Offset.Infinite
    )
    val gradientBorder = Brush.verticalGradient(
        colors =
            if(!isPressed && !onSelected) listOf(UrielBGWhite, UrielGradientLightGray)
            else listOf(UrielGradientLightOrange, UrielOrange)
    )
    val gradientContent = Brush.verticalGradient(
        colors =
            if(!isPressed && !onSelected) listOf(UrielIconGray, UrielIconGray)
            else listOf(UrielGradientLightOrange, UrielOrange)
    )
    val borderWidth =
        if(!isPressed && !onSelected) 1.dp
        else 4.dp

    Box (
        Modifier
            .size(size)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        UrielGradientDarkGray,
                        UrielBGWhite
                    )
                ),
                shape = CircleShape
            )
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .border(
                    width = borderWidth,
                    brush = gradientBorder,
                    shape = CircleShape
                )
                .background(
                    brush = gradientBackground,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            if(type == "text") {
                Text(
                    text,
                    style = TextStyle(
                        brush = gradientContent,
                        fontSize = fontSize
                    )
                )
            }
            else if(type == "icon") {
                Icon(
                    contentDescription = text,
                    tint = Color.White,
                    modifier = Modifier
                        .graphicsLayer(alpha = 0.99f)
                        .fillMaxSize(0.8f)
                        .padding(12.dp)
                        .drawWithContent {
                            // Draw the icon normally
                            drawContent()
                            // Overlay gradient only on the icon's shape
                            drawRect(
                                brush = gradientContent,
                                blendMode = BlendMode.SrcIn
                            )
                        },
                    painter = rememberVectorPainter(icon)
                )
            }
        }
    }
}