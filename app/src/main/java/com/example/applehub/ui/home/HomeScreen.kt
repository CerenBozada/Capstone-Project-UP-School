package com.example.applehub.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.applehub.R
import com.example.applehub.Register
import com.example.applehub.ui.cart.CartScreen
import com.example.applehub.ui.cart.CartViewModel
import com.example.applehub.ui.login.LoginPage
import com.example.applehub.ui.login.LoginViewModel
import com.example.applehub.ui.theme.AppleHubTheme
import dagger.hilt.android.AndroidEntryPoint


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(mainNavController: NavHostController) {
    val homeNavController = rememberNavController()
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    Scaffold(

        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
                title = {
                    Text("Apple Hub")
                }
            )
        },
        bottomBar = {
            NavigationBar(){
                NavigationBarItem(
                    selected = navBackStackEntry?.destination?.route == "shop",
                    onClick = { homeNavController.navigate("shop") },
                    icon = { Image(painter = painterResource(id = R.drawable.baseline_home_24), contentDescription = null) },
                    label = { Text("Anasayfa") }


                )
                NavigationBarItem(selected = navBackStackEntry?.destination?.route == "shopSearch",
                    onClick = { homeNavController.navigate("shopSearch") },
                    icon = { Image(painter = painterResource(id = R.drawable.baseline_search_24), contentDescription = null) },
                    label = { Text("Ara") }

                )
                NavigationBarItem(
                    selected = navBackStackEntry?.destination?.route == "favorite",
                    onClick = { homeNavController.navigate("favorite") },
                    icon = { Image(painter = painterResource(id = R.drawable.baseline_favorite_24), contentDescription = null) },
                    label = { Text("Favoriler") }

                )
                NavigationBarItem(
                    selected = navBackStackEntry?.destination?.route == "cart",
                    onClick = { homeNavController.navigate("cart") },
                    icon = { Image(painter = painterResource(id = R.drawable.baseline_credit_card_24), contentDescription = null) },
                    label = { Text("Sepet") }

                )
            }
        },

    ){
        NavHost(navController = homeNavController, startDestination = "shop") {
            composable("shop") {

                val shopViewModel: ShopViewModel = hiltViewModel()
                ShopScreen(shopViewModel, onProductClick = {
                    mainNavController.navigate("detail/$it")
                })
            }

            composable("shopSearch") {
                val shopSearchViewModel: ShopSearchViewModel = hiltViewModel()
                ShopSearchScreen(shopSearchViewModel, onProductClick = {
                    mainNavController.navigate("detail/$it")
                })
            }

            composable("favorite") {
                val favoriteViewModel: FavoriteViewModel = hiltViewModel()
                FavoriteScreen(favoriteViewModel, onProductClick = {
                    mainNavController.navigate("detail/$it")
                })
            }

            composable("cart") {
                val cartViewModel: CartViewModel = hiltViewModel()
                CartScreen(cartViewModel)
            }

        }
    }
}
