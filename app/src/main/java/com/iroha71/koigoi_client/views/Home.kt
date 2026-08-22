package com.iroha71.koigoi_client.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.auth0.android.result.Credentials
import com.iroha71.koigoi_client.ui.theme.KoigoiclientTheme

@Composable
fun Home(
    credentials: Credentials?,
    onLogout: () -> Unit,
) {
    Column {
        Text("Logged in as ${credentials?.user?.email}")
        Column {
            Text("sub: ${credentials?.user?.getId()}")
            Text("name: ${credentials?.user?.name}")
        }
        Button(onClick = onLogout) {
            Text("Log out")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    KoigoiclientTheme {
        Home(
            credentials = null,
            onLogout = {},
        )
    }
}
