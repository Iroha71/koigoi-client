package com.iroha71.koigoi_client

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.iroha71.koigoi_client.navigation.AppNavHost
import com.iroha71.koigoi_client.ui.theme.KoigoiclientTheme

class MainActivity : ComponentActivity() {
    private val account: Auth0 by lazy {
        Auth0.getInstance(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )
    }
    private val manager: SecureCredentialsManager by lazy {
        SecureCredentialsManager(this, account, SharedPreferencesStorage(this))
    }

    private var credentials by mutableStateOf<Credentials?>(null)
    private var isLoading by mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        manager.getCredentials(object: Callback<Credentials, CredentialsManagerException> {
            override fun onSuccess(result: Credentials) {
                credentials = result
                isLoading = false
            }

            override fun onFailure(error: CredentialsManagerException) {
                Log.e(MainActivity::class.java.simpleName, "Failed credentials")
                isLoading = false
            }
        })

        setContent {
            KoigoiclientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        modifier = Modifier.padding(innerPadding),
                        isLoading = isLoading,
                        credentials = credentials,
                        onLogin = { screenHint -> login(screenHint) },
                        onLogout = { logout() },
                    )
                }
            }
        }
    }

    fun login(screenHint: String? = null) {
        WebAuthProvider.login(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .withScope("openid profile email offline_access")
            .withParameters(buildMap { screenHint?.let { put("screen_hint", it) } })
            .start(this, object : Callback<Credentials, AuthenticationException> {
                override fun onSuccess(result: Credentials) {
                    credentials = result
                    manager.saveCredentials(result)
                }

                override fun onFailure(error: AuthenticationException) {
                    Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun logout() {
        WebAuthProvider.logout(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .start(this, object : Callback<Void?, AuthenticationException> {
                override fun onSuccess(result: Void?) {
                    credentials = null
                    manager.clearCredentials()
                }

                override fun onFailure(error: AuthenticationException) {
                    TODO("Not yet implemented")
                }
            })
    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KoigoiclientTheme {
        Greeting("Android")
    }
}