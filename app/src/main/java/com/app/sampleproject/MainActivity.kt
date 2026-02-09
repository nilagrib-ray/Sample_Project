package com.app.sampleproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import com.app.sampleproject.domain.repository.AuthRepository
import com.app.sampleproject.navigation.AppNavigation
import com.app.sampleproject.navigation.Screen
import com.app.sampleproject.ui.theme.SampleProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SampleProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val startDestination by produceState(initialValue = Screen.Login.route) {
                        value = if (authRepository.isLoggedIn()) {
                            Screen.Trips.route
                        } else {
                            Screen.Login.route
                        }
                    }

                    AppNavigation(startDestination = startDestination)
                }
            }
        }
    }
}