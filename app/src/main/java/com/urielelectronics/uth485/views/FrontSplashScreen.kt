package com.urielelectronics.uth485.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.urielelectronics.uth485.R
import com.urielelectronics.uth485.ui.theme.UrielBGWhite

@Composable
fun FrontSplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(UrielBGWhite)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_splash),
            contentDescription = "LOGO",
            modifier = Modifier.size(120.dp).align(Alignment.Center)
        )

        Image(
            painter = painterResource(id = R.drawable.logo_text),
            contentDescription = "LogoText",
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 120.dp)
        )
    }
}