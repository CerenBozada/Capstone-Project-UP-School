package com.example.applehub.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.applehub.register
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(loginViewModel: LoginViewModel, navHostController: NavHostController, modifier: Modifier = Modifier) {
    val loginState = loginViewModel.uiState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    LaunchedEffect(key1 = loginState.value.isLoginSuccess) {
        if (loginState.value.isLoginSuccess) {
            navHostController.navigate("home")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
                title = {
                    Text("Kayıt Ol")
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)

        ) {
            val context = LocalContext.current
            OutlinedTextField(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = username,
                onValueChange = { username = it },
                label = { Text("Kullanıcı adı") }
            )
            OutlinedTextField(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = password,
                onValueChange = { password = it },
                label = { Text("Parola") }
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                onClick = {
                  loginViewModel.login(username,password)
                },
                enabled = username.isNotBlank() && password.isNotBlank()
            ) {
                Text("Giriş")
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                onClick = {
                  navHostController.navigate("register")
                }
            )
            {
                Text("Kayıt Ol")
            }
        }
    }
}

