package com.withpeace.withpeace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.feature.home.navigation.HOME_ROUTE
import com.withpeace.withpeace.feature.login.navigation.LOGIN_ROUTE
import com.withpeace.withpeace.feature.registerpost.navigation.REGISTER_POST_ROUTE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var splashScreen: SplashScreen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // System Bar에 가려지는 뷰 영역을 개발자가 제어하겠다.
        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
            if(savedInstanceState==null) delay(2000L) //처음 앱 켰을때만 2초기다림, 화면회전에는 기다리면 안됨
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.isLogin.collect { isLogin ->
                    when (isLogin) {
                        true -> composeStart(REGISTER_POST_ROUTE)
                        false -> composeStart(LOGIN_ROUTE)
                        else -> {} // StateFlow의 상태를 Null로 설정함으로서, 로그인 상태가 업데이트 된 이후로 화면을 보여주도록 하기위함
                    }
                  splashScreen.setKeepOnScreenCondition { false }
                }
            }
        }
    }
    private fun ComponentActivity.composeStart(startDestination: String) {
        setContent {
            WithpeaceTheme {
                WithpeaceApp(startDestination = startDestination)
            }
        }
    }
}
