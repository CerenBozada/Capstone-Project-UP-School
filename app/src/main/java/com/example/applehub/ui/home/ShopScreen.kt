package com.example.applehub.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.applehub.R
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(shopViewModel: ShopViewModel, onProductClick: (Int) -> Unit) {
    val uiState = shopViewModel.uiState.collectAsState()
    val categories = shopViewModel.categoryState.asStateFlow().value

    val salesState = shopViewModel.salesState.collectAsState()

    var expanded by remember { mutableStateOf(false) }


    var selectedOptionText by remember {
        mutableStateOf("Kategori")
    }

    LaunchedEffect(key1 = selectedOptionText) {
        if (selectedOptionText == "Kategori") {
            return@LaunchedEffect
        } else {
            shopViewModel.getProductByCategory(selectedOptionText)
        }
        shopViewModel.getProductByCategory(selectedOptionText)
    }

Column {
    ExposedDropdownMenuBox(expanded =expanded , onExpandedChange = { expanded = it },
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .padding(8.dp),

    ) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text("Kategori") },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),

        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            categories.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }

    }
    val scrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    Column (
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(verticalScrollState)
    ){


    Text(text = "İndirimli Ürünler", modifier = androidx.compose.ui.Modifier.padding(8.dp),
        style = androidx.compose.ui.text.TextStyle(fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold))
    Row(modifier = androidx.compose.ui.Modifier
        .padding(8.dp)
        .height(
            280.dp
        )
        .horizontalScroll(scrollState)) {
        salesState.value.forEach {
            Card(modifier = androidx.compose.ui.Modifier
                .width(200.dp)

                .padding(8.dp)
                .clickable {
                    onProductClick(it.id)
                }
            ) {
                Column(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    GlideImage(
                        model = it.imageOne,
                        contentDescription = it.title,
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )


                    Text(text = "${it.price.toString()}₺",

                        style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),


                        modifier = androidx.compose.ui.Modifier

                            .padding(16.dp))
                }
            }
        }
    }


    when (val state = uiState.value) {
        is HomeState.Loading -> {
            Text(text = "Loading")
        }
        is HomeState.SuccessState -> {
            Column(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(8.dp),


            ) {
                state.products.forEach {

                 Column (Modifier.padding(bottom = 8.dp)){
                     Card {
                         Box(
                             Modifier.clickable {
                                 onProductClick(it.id)
                             }
                         ){
                             IconButton(onClick = { shopViewModel.favoriteProduct(it.id) }, modifier = Modifier.padding(8.dp)
                                 .align(androidx.compose.ui.Alignment.BottomEnd)) {
                                 Image(painter = painterResource(id =  if(it.isFavorite)  R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24 ), contentDescription = "Favorite",modifier = androidx.compose.ui.Modifier
                                     )
                             }

                         Column(
                             modifier = androidx.compose.ui.Modifier
                                 .fillMaxWidth()
                                 .padding(16.dp)
                         ) {
                             GlideImage(
                                 model = it.imageOne,
                                 contentDescription = it.title,
                                    modifier = androidx.compose.ui.Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                             )
                             Text(text = it.title)
                             HorizontalDivider()

                             Text(text = "${it.price.toString()}₺",

                                 style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),)
                         }
                     }
                     Spacer(modifier = androidx.compose.ui.Modifier.padding(8.dp))
                 }
                }
                }
            }
        }
        is HomeState.EmptyScreen -> {
            Text(text = "Empty ${state.failMessage}")
        }
        is HomeState.ShowPopUp -> {
            Text(text = "Error")
        }

        else -> {
            Text(text = "Error")
        }
    }
    }
}
}