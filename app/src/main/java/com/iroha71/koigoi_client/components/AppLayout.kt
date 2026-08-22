package com.iroha71.koigoi_client.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iroha71.koigoi_client.R
import com.iroha71.koigoi_client.ui.theme.KoigoiclientTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLayout(
    title: String,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(title) })
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_home),
                            contentDescription = "home"
                        )
                    },
                    label = { Text("Home") },
                    alwaysShowLabel = true,
                )
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppLayoutPreview() {
    KoigoiclientTheme {
        AppLayout(title = "Title") {
            Text("Content")
        }
    }
}