package com.indelible.fellowship

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.WindowCompat
import com.indelible.fellowship.ui.screen.maincontent.MainFragment
import com.indelible.fellowship.core.model.UserStatus
import com.indelible.fellowship.ui.screen.maincontent.MainViewModel
import com.indelible.fellowship.ui.theme.FellowshipTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val starDestination = viewModel.getStarDestination()
        enableEdgeToEdge()

        setContentView(
            ComposeView(this).apply {
                consumeWindowInsets = false
                setContent {
                    FellowshipTheme { MainFragment(starDestination) }
                }
            }
        )
    }

    override fun onResume() {
        viewModel.setUserStatusToFireBase(UserStatus.ONLINE)
        Log.d("MainActivity", "onResume: onResume is in")
        super.onResume()
    }

    override fun onPause() {
        viewModel.setUserStatusToFireBase(UserStatus.OFFLINE)
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.setUserStatusToFireBase(UserStatus.OFFLINE)
        super.onDestroy()
    }
}

