package com.iroha71.koigoi_client.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.iroha71.koigoi_client.ui.theme.KoigoiclientTheme

@Composable
fun Landing(
    isLoading: Boolean,
    onLogin: (String?) -> Unit,
) {
    Column {
        if (isLoading) {
            Text("Loading...")
        } else {
            Button(onClick = { onLogin("signup") }) {
                Text("Sign up")
            }
            Button(onClick = { onLogin(null) }) {
                Text("Log In")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandingPreview() {
    KoigoiclientTheme {
        Landing(
            isLoading = false,
            onLogin = {},
        )
    }
}

