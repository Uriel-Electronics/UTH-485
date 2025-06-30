package com.urielelectronics.uth485.views.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.urielelectronics.uth485.ui.theme.UrielTextLight
import com.urielelectronics.uth485.ui.theme.UrielOrange

@Composable
fun SaveButton(
    onButtonClick : () -> Unit,
    text : String,
    backgroundColor: Color = UrielOrange,
    icon : ImageVector
    ) {
    Button(
        onClick = onButtonClick,
        modifier = Modifier
            .wrapContentSize(),
        shape = RoundedCornerShape(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        )
    ) {
        Text(
            text = text,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = UrielTextLight,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
        Icon(
            imageVector = icon,
            contentDescription = "saveIcon",
            tint = UrielTextLight,
            modifier = Modifier
                .size(64.dp)
                .padding(horizontal = 8.dp)
        )
    }
}