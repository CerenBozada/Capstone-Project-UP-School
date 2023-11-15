package com.example.applehub.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.applehub.R
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShopSearchScreen(shopViewModel: ShopSearchViewModel, onProductClick: (Int) -> Unit) {
    val uiState = shopViewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("") }


    LaunchedEffect(key1 = searchText) {
        shopViewModel.searchProducts(searchText)
    }




    Column(Modifier.padding(top=100.dp)) {

        TextField(value = searchText, onValueChange = { searchText = it }, label = { Text(text = "Search") }, modifier = Modifier.padding(8.dp).fillMaxWidth(),
            trailingIcon = {
                Image(painter = painterResource(id = R.drawable.baseline_search_24), contentDescription = "Search")
            }
        )



    when (val state = uiState.value) {
        is SearchState.Loading -> {
            Text(text = "Loading")
        }

        is SearchState.SuccessState -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
            ) {
                items(state.products) {

                    Column(modifier = androidx.compose.ui.Modifier.padding(8.dp)) {
                        Card {
                            Box(
                                Modifier.clickable {
                                    onProductClick(it.id)
                                }
                            ) {
                                IconButton(onClick = { shopViewModel.favoriteProduct(it.id) }, modifier = Modifier.padding(8.dp)
                                    .align(Alignment.BottomEnd)) {
                                Image(
                                    painter = painterResource(id =  if(it.isFavorite)  R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24 ),
                                    contentDescription = "Favorite",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .align(Alignment.BottomEnd)
                                )
                            }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    GlideImage(
                                        model = it.imageOne,
                                        contentDescription = it.title,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    )
                                    Text(text = it.title)
                                    HorizontalDivider()

                                    Text(text = "${it.price.toString()}₺")
                                }
                            }
                            Spacer(modifier = androidx.compose.ui.Modifier.padding(8.dp))
                        }
                    }
                }
            }
        }

        is SearchState.EmptyScreen -> {
            Text(text = "Empty ${state.failMessage}")
        }

        is SearchState.ShowPopUp -> {
            Text(text = "Error")
        }

        else -> {
            Text(text = "Error")

        }
    }
    }
}