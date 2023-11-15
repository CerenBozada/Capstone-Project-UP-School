package com.example.applehub.ui.cart
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.applehub.R
import com.example.applehub.ui.home.ShopSearchViewModel
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartviewModel: CartViewModel) {

    val uiState = cartviewModel.uiState.collectAsState()

        when (val state = uiState.value) {
            is CartState.Loading -> {
                Text(text = "Loading")
            }
            is CartState.SuccessState -> {
                Box(Modifier.fillMaxHeight().padding(vertical = 80.dp)) {
                    Column (Modifier.padding(16.dp)) {
                state.products.forEach {

                    Card() {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            GlideImage(
                                model = it.imageOne,
                                contentDescription = it.title,
                                modifier = Modifier.width(100.dp)
                            )
                            Column(
                                Modifier
                                    .weight(1f)
                                    .padding(horizontal = 16.dp)) {
                                Text(text = it.title)
                                Text(
                                    text = "${it.price.toString()}₺",
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                            IconButton(onClick = { cartviewModel.deleteFromCard(it.id) }) {
                                Image(
                                    painter = painterResource(id = R.drawable.baseline_delete_24),
                                    contentDescription = "Delete"
                                )

                            }


                        }

                    }
                    Spacer(modifier = androidx.compose.ui.Modifier.padding(8.dp))
                }
                    }
                    val totalPrice = state.products.sumOf { it.price }
                    Card(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()) {
                        Row (
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically)
                        {
                            Text(text = "${totalPrice.toString()}₺", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,fontSize = 20.sp)
                            ElevatedButton(onClick = {cartviewModel.confirmCart(totalPrice.toFloat())}) {
                                Text(text = "SEPETİ ONAYLA")
                            }
                        }
                    }
            }

        }
            is CartState.CheckOut -> {
                var creditCartNumber by remember { mutableStateOf("") }
                var creditCartName by remember { mutableStateOf("") }
                var creditCartDate by remember { mutableStateOf("") }
                var creditCartCvv by remember { mutableStateOf("") }
                // 1970 to 2024 array
                val years =  (2010..2030).toList()
                val months = (1..12).toList()

                val monthExpanded = remember { mutableStateOf(false) }
                val yearExpanded = remember { mutableStateOf(false) }
                Column (Modifier.padding(16.dp),verticalArrangement = Arrangement.spacedBy(8.dp)){
                    Card  (
                        Modifier
                            .padding()
                            .fillMaxWidth()) {
                        Column (Modifier.padding(16.dp)) {
                            Text(text = "Kart Bilgileri")
                            Text(text = "${creditCartNumber}")

                            Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "${creditCartName}")
                                Text(text = "${creditCartDate}")
                            }
                        }
                    }

                    TextField(value = creditCartNumber, onValueChange = { creditCartNumber = it }, label = { Text(text = "Kart Numarası") }, modifier = Modifier.fillMaxWidth())
                    TextField(value = creditCartName, onValueChange = { creditCartName = it }, label = { Text(text = "Kart Üzerindeki İsim") }, modifier = Modifier.fillMaxWidth())
                    Row (Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                        ExposedDropdownMenuBox(
                            modifier= Modifier.weight(1f),
                            expanded = monthExpanded.value,
                            onExpandedChange = { monthExpanded.value = it }) {
                            TextField(
                                value = creditCartDate,
                                onValueChange = { creditCartDate = it },
                                label = { Text(text = "Son Kullanma Tarihi") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                readOnly = true,

                                )
                            DropdownMenu(
                                expanded = monthExpanded.value,
                                onDismissRequest = { monthExpanded.value = false }) {
                                months.forEach { month ->
                                    DropdownMenuItem(
                                        text = { Text(month.toString()) },
                                        onClick = {
                                            creditCartDate = "${month}/"
                                            monthExpanded.value = false
                                        },
                                    )
                                }
                            }
                        }
                        Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
                        ExposedDropdownMenuBox(
                            modifier= Modifier.weight(1f),
                            expanded = yearExpanded.value,
                            onExpandedChange = { yearExpanded.value = it }) {
                            TextField(
                                value = creditCartDate,
                                onValueChange = { creditCartDate = it },
                                label = { Text(text = "Son Kullanma Tarihi") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                readOnly = true,

                                )
                            DropdownMenu(
                                expanded = yearExpanded.value,
                                onDismissRequest = { yearExpanded.value = false }) {
                                months.forEach { month ->
                                    DropdownMenuItem(
                                        text = { Text(month.toString()) },
                                        onClick = {
                                            creditCartDate = "${month}/"
                                            yearExpanded.value = false
                                        },
                                    )
                                }
                            }
                        }
                    }
                    TextField(value = creditCartCvv, onValueChange = { creditCartCvv = it }, label = { Text(text = "CVV") }, modifier = Modifier.fillMaxWidth())
                    Text(text = "Toplam ${state.totalPrice}", style = androidx.compose.ui.text.TextStyle(fontSize = 20.sp,fontWeight = androidx.compose.ui.text.font.FontWeight.Bold))

                    ElevatedButton(onClick = {cartviewModel.doneTransaction()}) {
                        Text(text = "ÖDE",modifier= Modifier.fillMaxWidth())
                    }

                }

            }

            is CartState.TransactionDone -> {
          Column (Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Center) {
              Text(
                  text = state.message,
                  style = androidx.compose.ui.text.TextStyle(
                      fontSize = 20.sp,
                      fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                  )
              )
              Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
              ElevatedButton(onClick = { cartviewModel.getCartItems() }) {
                  Text(text = "SEPETE DÖN", modifier = Modifier.fillMaxWidth())
              }
          }
            }


            else -> {
                Text(text = "Boş")
            }
        }

}

