package com.urielelectronics.uth485

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
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
            val buf0 = response[2] // buf0
            val buf1 = response[3] // buf1
            val buf2 = response[4] // buf2
            val buf3 = response[5] // buf3
            val buf4 = response[6] // buf4
            val buf5 = response[7] // buf5
            val buf6 = response[8] // buf6
            val buf7 = response[9] // buf7
            val buf8 = response[10] // buf8
            val buf9 = response[11] // buf9
            val buf10 = response[12] // buf10
            val buf11 = response[13] // buf11
            val checksum = response[14] // checksum 양방향 통신 확인값
            Log.d("checksum", checksum.toString())

            var calculatedChecksum: UByte = 0xBF.toUByte()
            for (i in 1 until 14) {
                calculatedChecksum = (calculatedChecksum + response[i]).toUByte()
            }

            Log.d("calculatedChecksum", calculatedChecksum.toString())

            if (calculatedChecksum == checksum) {
                val modeNum = mode.toInt()

                when (modeNum) {
                    1 -> {
                        val year = buf0.toInt() // 연
                        val month = buf1.toInt() // 월
                        val day = buf2.toInt() // 일
                        val dayOfWeek = buf3.toInt() // 요일
                        Log.d("요일", dayOfWeek.toString())
                        val hour = buf4.toInt() // 시간
                        val minute = buf5.toInt() // 분
                        val second = buf6.toInt() // 초
                    }

                    3 -> {
                        val deviceCounts = buf0.toInt() // ( 단말기 수, 자동 설정값 보다 우선 )
                        val maxIdx = buf1.toInt()
                        Log.d("연결된 기기 수 :", deviceCounts.toString())
                        Log.d("단말기 MaxIdx :", maxIdx.toString())
                        viewModel.updateDeviceCount(deviceCounts, maxIdx)
                    }

                    5 -> {
                        val pw1 = buf0.toInt() // 비밀번호 첫자리
                        val pw2 = buf1.toInt() // 비밀번호 둘째자리
                        val pw3 = buf2.toInt() // 비밀번호 셋째자리
                        val pw4 = buf3.toInt() // 비밀번호 넷째자리

                        viewModel.updatePassword(pw1, pw2, pw3, pw4)
                    }

                    21 -> {
                        // rN == 0x00, ALL Select
                        // 전체 단말기를 1개씩 ON/OFF Delay 시간 (default: 1 sec)
                        val rN = buf0.toInt() // Each or All Select (0 ~ 255)
                        val powerStatus =
                            if ((buf1 and 1u).toInt() != 0) 1 else 0 // (buf1 & bit0) Power Status
                        val lockStatus =
                            if ((buf1 and 8u).toInt() != 0) 1 else 0 // (buf3 & bit 3) Lock Status
                    }

                    23 -> {
                        // rN == 0x00, ALL Select
                        val rN = buf0.toInt() // Each or All Select (0 ~ 255)
                        val year = buf1.toInt() // 연
                        val month = buf2.toInt() // 월
                        val day = buf3.toInt() // 일
                        val dayOfWeek = buf4.toInt() // 요일 (1:월 ~ 7:일)
                        val hour = buf5.toInt() // 시
                        val minute = buf6.toInt() // 분
                        val second = buf7.toInt() // 초
                        val dayOfWeeks = buf8.toInt() // 요일별 ON/OFF
                    }

                    25 -> {
                        // rN == 0x00, ALL Select
                        val rN = buf0.toInt() // Each or All Select (0 ~ 255)
                        val dayOfWeeks = buf1.toInt()
                    }

                    27 -> {
                        // rN == 0x00, ALL Select
                        val rN = buf0.toInt() // Each or All Select (0 ~ 255)
                        val V_L = buf1.toInt() // 최저온도 (0 ~ V_H)도
                        val V_H = buf2.toInt() // 최고온도 (V_L ~ 80)도
                        val IF = buf3.toInt() // 온도편차 (0 ~ 5)도
                        val LY = buf4.toInt() // 지연시간 (1 ~ 60)초 (전체, 그룹의 파워 설정시 지연시간 으로 사용)
                        val Ht = buf5.toInt() // 과승온도 (0 ~ 80)도
                        val ES = buf6.toInt() // 보정온도 (-10 ~ 10)도
                        val interval = buf7.toInt() // 주기 시간 (1 ~ 60)분
                        val firstTime = buf8.toInt() // 초기 투입 시간 (0 ~ 30)분
                        val settingTemp = buf9.toInt() // 설정온도 (V_L ~ V_H)도
                        val timeStep = buf10.toInt() // 시간 단계 (1 ~ 10)단계

                        viewModel.updateSettingData(rN, V_L, V_H, IF, LY, Ht, ES, interval, firstTime, settingTemp, timeStep)
                    }

                    29 -> {
                        // rN == 0x00, ALL Select
                        val rN = buf0.toInt() // Each or All Select (0 ~ 255)
                        val settingTemp = buf1.toInt() // 설정온도 (V_L ~ V_H)도
                        val timeStep = buf2.toInt() // 시간 단계 (1 ~ 10)단계
                    }

                    33 -> {
                        // Setting Scan Index, 0xEE 응답에 Index 값 반영
                        val index = buf0.toInt() // Index (1 ~ 246)
                        // index + 10 data(40 bytes), 응답없는 단말기 = 0
                        // EX) 127 ~ 136 scan : index = 127
                    }

                    34 -> {
                        // Group Power Status Setting, Group Lock Status Setting
                        val groups =
                            buf0.toInt() // 0x00: ALL 설정, (1 ~ 255), Group 단말기를 1개 씩 ON/OFF Delay (Default: 1sec)
                        val groupPower = if ((buf1 and 1u).toInt() != 0) 1 else 0
                        val groupLock = if ((buf1 and 8u).toInt() != 0) 1 else 0
                    }

                    35 -> {
                        // Group dayOfWeek Setting
                        val groups =
                            buf0.toInt() // 0x00: ALL 설정, (1 ~ 255), Group 단말기를 1개 씩 ON/OFF Delay (Default: 1sec)
                        val groupDayOfWeeks = buf1.toInt() // Group's DayOfWeeks ON/OFF
                    }

                    36 -> {
                        // Group Temp Setting, Group Time Step Setting
                        val groups =
                            buf0.toInt() // 0x00: ALL 설정, (1 ~ 255), Group 단말기를 1개 씩 ON/OFF Delay (Default: 1sec)
                        val groupSettingTemp = buf1.toInt() // (V_L ~ V_H)도
                        val groupTimeStep = buf2.toInt() // (1 ~ 10)단계
                    }
                }
            }
        } else if (response[0] == 0xDF.toUByte()) {
            val mode = response[1]
            val buf0 = response[2]
            val buf1 = response[3]
            val buf2 = response[4]
            val buf3 = response[5]
            val buf4 = response[6]
            val buf5 = response[7]
            val buf6 = response[8]
            val buf7 = response[9]
            val buf8 = response[10]
            val buf9 = response[11]
            val buf10 = response[12]
            val buf11 = response[13]
            val buf12 = response[14]
            val buf13 = response[15]
            val buf14 = response[16]
            val buf15 = response[17]
            val buf16 = response[18]
            val buf17 = response[19]
            val buf18 = response[20]
            val buf19 = response[21]
            val buf20 = response[22]
            val buf21 = response[23]
            val buf22 = response[24]
            val buf23 = response[25]
            val buf24 = response[26]
            val buf25 = response[27]
            val buf26 = response[28]
            val buf27 = response[29]
            val checksum = response[30]
            Log.d("checksum", checksum.toString())

            var calculatedChecksum: UByte = 0xDF.toUByte()
            for (i in 1..30) {
                calculatedChecksum = (calculatedChecksum + response[i]).toUByte()
            }

            Log.d("calculatedChecksum", calculatedChecksum.toString())

            if (calculatedChecksum == checksum) {
                val modeNum = mode.toInt()

                when (modeNum) {
                    1 -> {

                    }
                }
            }

        } else if (response[0] == 0xEE.toUByte()) {
            // 스캔 데이터 응답, Index = (0 ~ 246), 구조 = (4Bytes 배열) * 10개
            val index = response[1] // Index (0xBF로부터)
            val scannedIndex = index.toInt()

            // rN1
            val buf0 = response[2] // rN (1 ~ 255, 0: 통신 Error)
            val buf1 = response[3] // STATUS
            val rN1 = buf0.toInt()
            val powerStatus1 = if ((buf1 and 1u).toInt() != 0) 1 else 0 // 0: Power off, 1: Power On
            val relayStatus1 = if ((buf1 and 2u).toInt() != 0) 1 else 0 // 0: Relay Off, 1: Relay On
            val modeStatus1 = if ((buf1 and 4u).toInt() != 0) 1 else 0 // 0 : Time Mode, 1: Sensor Mode
            val lockStatus1 = if ((buf1 and 8u).toInt() != 0) 1 else 0 // 0: Lock Off, 1: Lock On
            val openError1 = if ((buf1 and 16u).toInt() != 0) 1 else 0 // 0: Open Error Off, 1: Open Error On
            val shortError1 = if ((buf1 and 32u).toInt() != 0) 1 else 0 // 0: Short Error Off, 1: Short Error On
            val overHeatError1 = if ((buf1 and 64u).toInt() != 0) 1 else 0 // 0: OverHeat Error Off, 1: OverHeat Error On
            val LHMode1 = if ((buf1 and 128u).toInt() != 0) 1 else 0 // 0: LL, 1: HH
            val buf2 = response[4] // SettingTemp
            val settingTemp1 = buf2.toInt()
            val buf3 = response[5]
            val settingStep1 = if ((buf3 and 1u).toInt() != 0) 1 else 0   // rN1 설정 단계
            val currentTemp1 = if ((buf3 and 2u).toInt() != 0) 1 else 0 // rN1 현재온도

            // rN2
            val buf4 = response[6]
            val buf5 = response[7]
            val rN2 = buf4.toInt()
            val powerStatus2 = if ((buf5 and 1u).toInt() != 0) 1 else 0 // 0: Power off, 1: Power On
            val relayStatus2 = if ((buf5 and 2u).toInt() != 0) 1 else 0 // 0: Relay Off, 1: Relay On
            val modeStatus2 = if ((buf5 and 4u).toInt() != 0) 1 else 0 // 0 : Time Mode, 1: Sensor Mode
            val lockStatus2 = if ((buf5 and 8u).toInt() != 0) 1 else 0 // 0: Lock Off, 1: Lock On
            val openError2 = if ((buf5 and 16u).toInt() != 0) 1 else 0 // 0: Open Error Off, 1: Open Error On
            val shortError2 = if ((buf5 and 32u).toInt() != 0) 1 else 0 // 0: Short Error Off, 1: Short Error On
            val overHeatError2 = if ((buf5 and 64u).toInt() != 0) 1 else 0 // 0: OverHeat Error Off, 1: OverHeat Error On
            val LHMode2 = if ((buf5 and 128u).toInt() != 0) 1 else 0 // 0: LL, 1: HH
            val buf6 = response[8] // SettingTemp
            val settingTemp2 = buf6.toInt()
            val buf7 = response[9]
            val settingStep2 = if ((buf7 and 1u).toInt() != 0) 1 else 0   // rN1 설정 단계
            val currentTemp2 = if ((buf7 and 2u).toInt() != 0) 1 else 0 // rN1 현재온도

            // rN3
            val buf8 = response[10]
            val buf9 = response[11]
            val rN3 = buf8.toInt()
            val powerStatus3 = if ((buf9 and 1u).toInt() != 0) 1 else 0 // 0: Power off, 1: Power On
            val relayStatus3 = if ((buf9 and 2u).toInt() != 0) 1 else 0 // 0: Relay Off, 1: Relay On
            val modeStatus3 = if ((buf9 and 4u).toInt() != 0) 1 else 0 // 0 : Time Mode, 1: Sensor Mode
            val lockStatus3 = if ((buf9 and 8u).toInt() != 0) 1 else 0 // 0: Lock Off, 1: Lock On
            val openError3 = if ((buf9 and 16u).toInt() != 0) 1 else 0 // 0: Open Error Off, 1: Open Error On
            val shortError3 = if ((buf9 and 32u).toInt() != 0) 1 else 0 // 0: Short Error Off, 1: Short Error On
            val overHeatError3 = if ((buf9 and 64u).toInt() != 0) 1 else 0 // 0: OverHeat Error Off, 1: OverHeat Error On
            val LHMode3 = if ((buf9 and 128u).toInt() != 0) 1 else 0 // 0: LL, 1: HH
            val buf10 = response[12] // SettingTemp
            val settingTemp3 = buf6.toInt()
            val buf11 = response[13]
            val settingStep3 = if ((buf11 and 1u).toInt() != 0) 1 else 0   // rN1 설정 단계
            val currentTemp3 = if ((buf11 and 2u).toInt() != 0) 1 else 0 // rN1 현재온도

            // rN4
            val buf12 = response[14]
            val buf13 = response[15]
            val rN4 = buf12.toInt()
            val powerStatus4 = if ((buf13 and 1u).toInt() != 0) 1 else 0 // 0: Power off, 1: Power On
            val relayStatus4 = if ((buf13 and 2u).toInt() != 0) 1 else 0 // 0: Relay Off, 1: Relay On
            val modeStatus4 = if ((buf13 and 4u).toInt() != 0) 1 else 0 // 0 : Time Mode, 1: Sensor Mode
            val lockStatus4 = if ((buf13 and 8u).toInt() != 0) 1 else 0 // 0: Lock Off, 1: Lock On
            val openError4 = if ((buf13 and 16u).toInt() != 0) 1 else 0 // 0: Open Error Off, 1: Open Error On
            val shortError4 = if ((buf13 and 32u).toInt() != 0) 1 else 0 // 0: Short Error Off, 1: Short Error On
            val overHeatError4 = if ((buf13 and 64u).toInt() != 0) 1 else 0 // 0: OverHeat Error Off, 1: OverHeat Error On
            val LHMode4 = if ((buf13 and 128u).toInt() != 0) 1 else 0 // 0: LL, 1: HH
            val buf14 = response[16] // SettingTemp
            val settingTemp4 = buf14.toInt()
            val buf15 = response[17]
            val settingStep4 = if ((buf15 and 1u).toInt() != 0) 1 else 0   // 설정 단계
            val currentTemp4 = if ((buf15 and 2u).toInt() != 0) 1 else 0 // 현재온도

            // rN5
            val buf16 = response[18]
            val buf17 = response[19]
            val rN5 = buf16.toInt()
            val powerStatus5 = if ((buf17 and 1u).toInt() != 0) 1 else 0 // 0: Power off, 1: Power On
            val relayStatus5 = if ((buf17 and 2u).toInt() != 0) 1 else 0 // 0: Relay Off, 1: Relay On
            val modeStatus5 = if ((buf17 and 4u).toInt() != 0) 1 else 0 // 0 : Time Mode, 1: Sensor Mode
            val lockStatus5 = if ((buf17 and 8u).toInt() != 0) 1 else 0 // 0: Lock Off, 1: Lock On
            val openError5 = if ((buf17 and 16u).toInt() != 0) 1 else 0 // 0: Open Error Off, 1: Open Error On
            val shortError5 = if ((buf17 and 32u).toInt() != 0) 1 else 0 // 0: Short Error Off, 1: Short Error On
            val overHeatError5 = if ((buf17 and 64u).toInt() != 0) 1 else 0 // 0: OverHeat Error Off, 1: OverHeat Error On
            val LHMode5 = if ((buf17 and 128u).toInt() != 0) 1 else 0 // 0: LL, 1: HH
            val buf18 = response[20] // SettingTemp
            val settingTemp5 = buf18.toInt()
            val buf19 = response[21]
            val settingStep5 = if ((buf19 and 1u).toInt() != 0) 1 else 0   // 설정 단계
            val currentTemp5 = if ((buf19 and 2u).toInt() != 0) 1 else 0 // 현재온도

            // rN6
            val buf20 = response[22]
            val buf21 = response[23]
            val rN6 = buf20.toInt()
            val powerStatus6 = if ((buf21 and 1u).toInt() != 0) 1 else 0 // 0: Power off, 1: Power On
            val relayStatus6 = if ((buf21 and 2u).toInt() != 0) 1 else 0 // 0: Relay Off, 1: Relay On
            val modeStatus6 = if ((buf21 and 4u).toInt() != 0) 1 else 0 // 0 : Time Mode, 1: Sensor Mode
            val lockStatus6 = if ((buf21 and 8u).toInt() != 0) 1 else 0 // 0: Lock Off, 1: Lock On
            val openError6 = if ((buf21 and 16u).toInt() != 0) 1 else 0 // 0: Open Error Off, 1: Open Error On
            val shortError6 = if ((buf21 and 32u).toInt() != 0) 1 else 0 // 0: Short Error Off, 1: Short Error On
            val overHeatError6 = if ((buf21 and 64u).toInt() != 0) 1 else 0 // 0: OverHeat Error Off, 1: OverHeat Error On
            val LHMode6 = if ((buf21 and 128u).toInt() != 0) 1 else 0 // 0: LL, 1: HH
            val buf22 = response[24] // SettingTemp
            val settingTemp6 = buf22.toInt()
            val buf23 = response[25]
            val settingStep6 = if ((buf23 and 1u).toInt() != 0) 1 else 0   // 설정 단계
            val currentTemp6 = if ((buf23 and 2u).toInt() != 0) 1 else 0 // 현재온도

            // rN7
            val buf24 = response[26]
            val buf25 = response[27]
            val rN7 = buf24.toInt()
            val powerStatus7 = if ((buf25 and 1u).toInt() != 0) 1 else 0 // 0: Power off, 1: Power On
            val relayStatus7 = if ((buf25 and 2u).toInt() != 0) 1 else 0 // 0: Relay Off, 1: Relay On
            val modeStatus7 = if ((buf25 and 4u).toInt() != 0) 1 else 0 // 0 : Time Mode, 1: Sensor Mode
            val lockStatus7 = if ((buf25 and 8u).toInt() != 0) 1 else 0 // 0: Lock Off, 1: Lock On
            val openError7 = if ((buf25 and 16u).toInt() != 0) 1 else 0 // 0: Open Error Off, 1: Open Error On
            val shortError7 = if ((buf25 and 32u).toInt() != 0) 1 else 0 // 0: Short Error Off, 1: Short Error On
            val overHeatError7 = if ((buf25 and 64u).toInt() != 0) 1 else 0 // 0: OverHeat Error Off, 1: OverHeat Error On
            val LHMode7 = if ((buf25 and 128u).toInt() != 0) 1 else 0 // 0: LL, 1: HH
            val buf26 = response[28] // SettingTemp
            val settingTemp7 = buf26.toInt()
            val buf27 = response[29]
            val settingStep7 = if ((buf27 and 1u).toInt() != 0) 1 else 0   // 설정 단계
            val currentTemp7 = if ((buf27 and 2u).toInt() != 0) 1 else 0 // 현재온도

            // rN8
            val buf28 = response[30]
            val buf29 = response[31]
            val rN8 = buf28.toInt()
            val powerStatus8 = if ((buf29 and 1u).toInt() != 0) 1 else 0 // 0: Power off, 1: Power On
            val relayStatus8 = if ((buf29 and 2u).toInt() != 0) 1 else 0 // 0: Relay Off, 1: Relay On
            val modeStatus8 = if ((buf29 and 4u).toInt() != 0) 1 else 0 // 0 : Time Mode, 1: Sensor Mode
            val lockStatus8 = if ((buf29 and 8u).toInt() != 0) 1 else 0 // 0: Lock Off, 1: Lock On
            val openError8 = if ((buf29 and 16u).toInt() != 0) 1 else 0 // 0: Open Error Off, 1: Open Error On
            val shortError8 = if ((buf29 and 32u).toInt() != 0) 1 else 0 // 0: Short Error Off, 1: Short Error On
            val overHeatError8 = if ((buf29 and 64u).toInt() != 0) 1 else 0 // 0: OverHeat Error Off, 1: OverHeat Error On
            val LHMode8 = if ((buf29 and 128u).toInt() != 0) 1 else 0 // 0: LL, 1: HH
            val buf30 = response[32] // SettingTemp
            val settingTemp8 = buf30.toInt()
            val buf31 = response[33]
            val settingStep8 = if ((buf31 and 1u).toInt() != 0) 1 else 0   // 설정 단계
            val currentTemp8 = if ((buf31 and 2u).toInt() != 0) 1 else 0 // 현재온도

            // rN9
            val buf32 = response[34]
            val buf33 = response[35]
            val rN9 = buf32.toInt()
            val powerStatus9 = if ((buf33 and 1u).toInt() != 0) 1 else 0 // 0: Power off, 1: Power On
            val relayStatus9 = if ((buf33 and 2u).toInt() != 0) 1 else 0 // 0: Relay Off, 1: Relay On
            val modeStatus9 = if ((buf33 and 4u).toInt() != 0) 1 else 0 // 0 : Time Mode, 1: Sensor Mode
            val lockStatus9 = if ((buf33 and 8u).toInt() != 0) 1 else 0 // 0: Lock Off, 1: Lock On
            val openError9 = if ((buf33 and 16u).toInt() != 0) 1 else 0 // 0: Open Error Off, 1: Open Error On
            val shortError9 = if ((buf33 and 32u).toInt() != 0) 1 else 0 // 0: Short Error Off, 1: Short Error On
            val overHeatError9 = if ((buf33 and 64u).toInt() != 0) 1 else 0 // 0: OverHeat Error Off, 1: OverHeat Error On
            val LHMode9 = if ((buf33 and 128u).toInt() != 0) 1 else 0 // 0: LL, 1: HH
            val buf34 = response[36] // SettingTemp
            val settingTemp9 = buf34.toInt()
            val buf35 = response[37]
            val settingStep9 = if ((buf35 and 1u).toInt() != 0) 1 else 0   // 설정 단계
            val currentTemp9 = if ((buf35 and 2u).toInt() != 0) 1 else 0 // 현재온도

            // rN10
            val buf36 = response[38]
            val buf37 = response[39]
            val rN10 = buf36.toInt()
            val powerStatus10 = if ((buf37 and 1u).toInt() != 0) 1 else 0 // 0: Power off, 1: Power On
            val relayStatus10 = if ((buf37 and 2u).toInt() != 0) 1 else 0 // 0: Relay Off, 1: Relay On
            val modeStatus10 = if ((buf37 and 4u).toInt() != 0) 1 else 0 // 0 : Time Mode, 1: Sensor Mode
            val lockStatus10 = if ((buf37 and 8u).toInt() != 0) 1 else 0 // 0: Lock Off, 1: Lock On
            val openError10 = if ((buf37 and 16u).toInt() != 0) 1 else 0 // 0: Open Error Off, 1: Open Error On
            val shortError10 = if ((buf37 and 32u).toInt() != 0) 1 else 0 // 0: Short Error Off, 1: Short Error On
            val overHeatError10 = if ((buf37 and 64u).toInt() != 0) 1 else 0 // 0: OverHeat Error Off, 1: OverHeat Error On
            val LHMode10 = if ((buf37 and 128u).toInt() != 0) 1 else 0 // 0: LL, 1: HH
            val buf38 = response[40] // SettingTemp
            val settingTemp10 = buf38.toInt()
            val buf39 = response[41]
            val settingStep10 = if ((buf39 and 1u).toInt() != 0) 1 else 0   // 설정 단계
            val currentTemp10 = if ((buf39 and 2u).toInt() != 0) 1 else 0 // 현재온도

            viewModel.updateScanData1(scannedIndex, rN1, powerStatus1, relayStatus1, modeStatus1, lockStatus1, openError1, shortError1, overHeatError1, LHMode1, settingTemp1, settingStep1, currentTemp1)
            viewModel.updateScanData1(scannedIndex, rN1, powerStatus1, relayStatus1, modeStatus1, lockStatus1, openError1, shortError1, overHeatError1, LHMode1, settingTemp1, settingStep1, currentTemp1)
            viewModel.updateScanData1(scannedIndex, rN1, powerStatus1, relayStatus1, modeStatus1, lockStatus1, openError1, shortError1, overHeatError1, LHMode1, settingTemp1, settingStep1, currentTemp1)
            viewModel.updateScanData1(scannedIndex, rN1, powerStatus1, relayStatus1, modeStatus1, lockStatus1, openError1, shortError1, overHeatError1, LHMode1, settingTemp1, settingStep1, currentTemp1)
            viewModel.updateScanData1(scannedIndex, rN1, powerStatus1, relayStatus1, modeStatus1, lockStatus1, openError1, shortError1, overHeatError1, LHMode1, settingTemp1, settingStep1, currentTemp1)
            viewModel.updateScanData1(scannedIndex, rN1, powerStatus1, relayStatus1, modeStatus1, lockStatus1, openError1, shortError1, overHeatError1, LHMode1, settingTemp1, settingStep1, currentTemp1)
            viewModel.updateScanData1(scannedIndex, rN1, powerStatus1, relayStatus1, modeStatus1, lockStatus1, openError1, shortError1, overHeatError1, LHMode1, settingTemp1, settingStep1, currentTemp1)
            viewModel.updateScanData1(scannedIndex, rN1, powerStatus1, relayStatus1, modeStatus1, lockStatus1, openError1, shortError1, overHeatError1, LHMode1, settingTemp1, settingStep1, currentTemp1)
            viewModel.updateScanData1(scannedIndex, rN1, powerStatus1, relayStatus1, modeStatus1, lockStatus1, openError1, shortError1, overHeatError1, LHMode1, settingTemp1, settingStep1, currentTemp1)
            viewModel.updateScanData1(scannedIndex, rN1, powerStatus1, relayStatus1, modeStatus1, lockStatus1, openError1, shortError1, overHeatError1, LHMode1, settingTemp1, settingStep1, currentTemp1)


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
