package com.example.applehub

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.applehub.ui.cart.CartScreen
import com.example.applehub.ui.cart.CartViewModel
import com.example.applehub.ui.detail.DetailScreen
import com.example.applehub.ui.detail.DetailViewModel
import com.example.applehub.ui.home.HomeScreen
import com.example.applehub.ui.login.LoginPage
import com.example.applehub.ui.login.LoginViewModel
import com.example.applehub.ui.theme.AppleHubTheme
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppleHubTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main()
                }
            }
        }
    }
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("register") { Register(navController) }
        composable("login") {

            val loginViewModel = hiltViewModel<LoginViewModel>()


            LoginPage(loginViewModel,navController)
        }
        composable("home") {
            HomeScreen(
            navController
        ) }
        composable("detail/{id}") {
                backStackEntry ->

            val detailViewModel = hiltViewModel<DetailViewModel>()

            DetailScreen(detailViewModel,backStackEntry.arguments?.getString("id")?.toInt() ?: 0)

        }




    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

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

        ){
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
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Parolayı tekrarla") }
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                onClick = {
                    coroutineScope.launch {
                        try {
                            val res = register(username,password,confirmPassword)

                            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                            navController.navigate("home")

                        }catch (e: Exception) {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        }

                    }
                          },
                enabled = username.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
            ) {
                Text("Kayıt")
            }
        }
    }
}
suspend fun register(username: String, password: String, confirmPassword: String): String? {
    val auth= FirebaseAuth.getInstance();
    val result = auth.createUserWithEmailAndPassword(username,password).await()
    return result.user?.uid
}
