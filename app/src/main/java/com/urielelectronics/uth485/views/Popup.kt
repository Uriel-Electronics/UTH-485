package com.urielelectronics.uth485.views

import android.app.AlertDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.urielelectronics.uth485.ui.theme.UrielBGOrange
import com.urielelectronics.uth485.ui.theme.UrielBGWhite
import com.urielelectronics.uth485.ui.theme.UrielTextGray
import com.urielelectronics.uth485.ui.theme.UrielTextOrange

@Composable
fun Popup(
    title: String,
    content : @Composable () -> Unit,
    type : String = "alarm", // "confirm" || "alarm"
    confirmText: String = "예",
    dismissText: String = "아니요",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog (onDismissRequest = onDismiss) {
        // 닫기 버튼 (X)
        Column (
            Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box (
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth(0.075f)
                        .background(Color.Transparent)
                        .border(
                            width = 2.dp,
                            color = UrielBGWhite,
                            shape = RoundedCornerShape(128.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "닫기",
                        tint = UrielBGWhite
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
            ) {
                Box {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 제목
                        Text(
                            text = title,
                            //  = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                            modifier = Modifier.padding(horizontal = 24.dp),
                            color = UrielBGOrange,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        HorizontalDivider(
                            modifier = Modifier
                                .height(1.dp),
                            color = UrielTextGray

                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        // 본문

                        Box (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            content()
                        }


                        Spacer(modifier = Modifier.height(24.dp))

                        // 버튼
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .padding(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (type == "confirm") {
                                OutlinedButton(
                                    onClick = onDismiss,
                                    modifier = Modifier.weight(1f),
                                    border = BorderStroke(1.dp, UrielTextGray)
                                ) {
                                    Text(
                                        text = dismissText,
                                        color = UrielTextGray,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                            OutlinedButton (
                                onClick = onConfirm,
                                modifier = Modifier
                                    .weight(1f),
                                border = BorderStroke(1.dp, UrielTextOrange)
                            ) {
                                Text(
                                    text = confirmText,
                                    color = UrielBGOrange,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}