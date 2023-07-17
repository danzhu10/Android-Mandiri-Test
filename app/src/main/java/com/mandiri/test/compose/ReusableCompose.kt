package com.mandiri.test.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mandiri.test.ui.theme.TempoRed

@Composable
fun FailedComposable(
    errorMessage: String = "Terjadi Kesalahan. Silahkan coba lagi",
    retry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = errorMessage, textAlign = TextAlign.Center)
        SpacingH10(8.dp)
        Button(
            onClick = retry,
            colors = ButtonDefaults.buttonColors(containerColor = TempoRed)
        ) {
            Text(text = "Coba Lagi", color = Color.White)
        }
    }
}

@Composable
fun SpacingH10(value: Dp = 10.dp) {
    Spacer(modifier = Modifier.height(value))
}

@Composable
fun SpacingH5(value: Dp = 5.dp) {
    Spacer(modifier = Modifier.height(value))
}

@Composable
fun SpacingV5(value: Dp = 5.dp) {
    Spacer(modifier = Modifier.width(value))
}

@Composable
fun SpacingV10(value: Dp = 10.dp) {
    Spacer(modifier = Modifier.width(value))
}

@Composable
fun ErrorView(errorMessage: String, onBtnClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .requiredHeight(80.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            errorMessage, style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center, fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Button(
            onClick = onBtnClick
        ) {
            Text("Retry")
        }
    }
}