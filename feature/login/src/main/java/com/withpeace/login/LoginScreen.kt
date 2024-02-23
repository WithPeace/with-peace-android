package com.withpeace.login

import GoogleLogin
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Composable
fun LoginScreen(){
    Surface(modifier = Modifier.fillMaxSize()) {
        val coroutineScope = rememberCoroutineScope()
        val activity = LocalContext.current
        Box{
            Button(onClick = {
                GoogleLogin().googleLogin(coroutineScope,activity as Activity) {
                    Toast.makeText(activity, "success", Toast.LENGTH_LONG).show()
                    Log.d("test", "googleSuccess")
                }
            }) {
                Text(text = "Login")
            }
        }
    }
}

@Preview(widthDp = 400, heightDp = 900)
@Composable
fun LoginScreenPreview(){
    LoginScreen()
}