package com.example.applehub.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(detailViewModel: DetailViewModel, id: Int) {
    val uiState = detailViewModel.uiState.collectAsState()
    LaunchedEffect(key1 = Unit) {
        detailViewModel.getProductDetail(id)
    }
    when (val state = uiState.value) {
        is DetailState.Loading -> {
            Text(text = "Loading")
        }
        is DetailState.SuccessState -> {
            Box() {
                Column {
                    GlideImage(
                        model = state.product.imageOne,
                        contentDescription = state.product.title
                    )
                    Text(text = state.product.title)
                    Text(text = state.product.description)
                    Text(text = state.product.price.toString())
                }
                Card(Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
                    Row (Modifier.fillMaxWidth().padding(16.dp),horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically)
                    {
                        Text(text = "${state.product.price.toString()}₺", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,fontSize = 20.sp)
                        ElevatedButton(onClick = { detailViewModel.addToCart(state.product.id)}) {
                            Text(text = "SEPETE EKLE")
                        }
                    }
                }
            }
        }
        is DetailState.EmptyScreen -> {
            Text(text = state.failMessage)
        }
        is DetailState.ShowPopUp -> {
            Text(text = state.errorMessage)
        }
    }

}