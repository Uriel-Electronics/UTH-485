package com.urielelectronics.uth485

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.urielelectronics.uth485.ui.theme.UTH485Theme
import com.urielelectronics.uth485.views.FrontSplashScreen
import com.urielelectronics.uth485.views.HomeView
import com.urielelectronics.uth485.views.MyViewModel
import com.urielelectronics.uth485.views.ViewState
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    var REQUEST_PERMISSON: Int = 0
    private var backPressedTime: Long = 0
    private lateinit var toast: Toast

    private val viewModel: MyViewModel by viewModels()
    var viewState by mutableStateOf(ViewState.HOME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermission()

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (viewState) {
                    ViewState.HOME -> {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - backPressedTime < 2000) {
                            toast.cancel()
                            finish()
                            System.exit(0)
                            return
                        }
                    } else -> {
                        toast = Toast.makeText(this@MainActivity, "한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
            }
        })

//        enableEdgeToEdge()
        setContent {
            var showMainScreen by remember { mutableStateOf(false) }

            UTH485Theme {
                LaunchedEffect(key1 = true) {
                    delay(2500)
                    showMainScreen = true
                }

                FrontSplashScreen()

                if (showMainScreen) {
                    HomeView(viewModel = viewModel)
                }
            }
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class, ExperimentalStdlibApi::class)
    fun handleCharacteristicUpdate(data: ByteArray, viewModel: MyViewModel) {
        val response = data.toUByteArray()
        if (response[0] == 0xBF.toUByte()) {
            val mode = response[1] // Mode
            val buf0 = response[2] // rN = 1 ~ 99
            val currentStatus = response[3] // STATUS bit 0, Power ON/OFF
//                val currentPower = if ((currentStatus and 1u).toInt() != 0) 1 else 0 // bit 0
//                val currentRelay = if ((currentStatus and 2u).toInt() != 0) 1 else 0 // OUT Relay
//                val currentMode = if ((currentStatus and 4u).toInt() != 0 ) 1 else 0 // TimerMode/SensorMode
            val currentRelay = response[4]
            val currentMode = response[5]
            val buf5 = response[6] // LOCK OFF/ON
            val buf6 = response[7] // AO
            val buf7 = response[8] // AS
            val buf8 = response[9] // Ht
            val buf9 = response[10] // 통신 Error
            val buf10 = response[11] // 설정 온도
            val buf11 = response[12] // 설정 단계
            val buf12 = response[13] // 현재 온도

        } else if (response[0] == 0xEF.toUByte()) {
            // Page = 0 ~ 8191, HEAD
            val head0 = response[1] // Page High (4Byte)
            val head1 = response[2] // Page Low (4Byte)
            val head2 = response[3] // Month
            val head3 = response[4] // Date
            val head4 = response[5] // Hour
            val head5 = response[6] // Min
            val head6 = response[7] // Second
            val head7 = response[8] // Device Number
            val head8 = response[9] // Device Start (1 ~ 100)
            val head9 = response[10] // Device End ( 1 ~ 100)

            // DATA 200

        }
    }

    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        permissions.entries.forEach {
//            Timber.d("${it.key} = ${it.value}")
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.NFC,
                ),
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.NFC,
                ),
            )
        } else {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }
}
