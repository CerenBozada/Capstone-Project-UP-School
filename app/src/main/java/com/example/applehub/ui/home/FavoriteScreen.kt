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
 import androidx.compose.material3.Text
 import androidx.compose.runtime.Composable
 import androidx.compose.runtime.collectAsState
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.res.painterResource
 import androidx.compose.ui.unit.dp
 import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
 import com.bumptech.glide.integration.compose.GlideImage
 import com.example.applehub.R
 import kotlinx.coroutines.flow.asStateFlow
@Composable

@OptIn(ExperimentalGlideComposeApi::class)
fun FavoriteScreen(favoriteViewModel:FavoriteViewModel,onProductClick: (Int) -> Unit) {
    val uiState = favoriteViewModel.uiState.collectAsState()


 when (val state = uiState.value) {
  is HomeState.Loading -> {
   Text(text = "Loading")
  }
  is HomeState.SuccessState -> {
   LazyVerticalGrid(
    columns = GridCells.Fixed(2),
   ) {
    items(state.products) {

     Column (modifier = androidx.compose.ui.Modifier.padding(8.dp)) {
      Card {
       Box(
        Modifier.clickable {
         onProductClick(it.id)
        }
       ){
        Image(painter = painterResource(id =  R.drawable.baseline_favorite_24), contentDescription = "Favorite",modifier = androidx.compose.ui.Modifier.padding(8.dp).align(androidx.compose.ui.Alignment.BottomEnd))
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

         Text(text = "${it.price.toString()}₺")
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
 }
}