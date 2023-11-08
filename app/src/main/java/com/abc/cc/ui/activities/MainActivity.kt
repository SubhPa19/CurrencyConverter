package com.abc.cc.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abc.cc.data.model.ExchangeRates
import com.abc.cc.data.vm.ConverterViewModel
import com.abc.cc.data.vm.UIEvent
import com.abc.cc.ui.theme.CurrencyConverterTheme
import com.abc.cc.util.convertRate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: ConverterViewModel by viewModels()
        viewModel.getExchangeRates()
        setContent {
            val dataLoaded by viewModel.dataLoaded.collectAsState()
            val currency by viewModel.exchangeRates.collectAsState()
            CurrencyConverterTheme {
                Surface {
                    DataLoadingScreen(currency)
                }
            }
        }
    }

    @Composable
    fun DataLoadingScreen(currency: UIEvent) {
        when (currency) {
            is UIEvent.ExchangeRates -> {
                val exchangeRates = currency.exchangeRates
                ConverterScreen(exchangeRates)
            }

            UIEvent.Empty -> {

            }

            is UIEvent.Error -> {
                Toast.makeText(
                    applicationContext,
                    "Something unexpected occurred!",
                    Toast.LENGTH_LONG
                ).show()
            }

            UIEvent.Loading -> {
                ProgressWithText()
            }
        }
    }

    @Composable
    private fun ProgressWithText() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp, 50.dp))
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Loading latest exchange Rates...")
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ConverterScreen(exchangeRates: List<ExchangeRates>) {

        var amountToConvert by remember { mutableStateOf(TextFieldValue("1")) }
        var expanded by remember { mutableStateOf(false) }
        var selectedCurrency by remember { mutableStateOf(exchangeRates.first()) }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(text = "Amount to convert") },
                value = amountToConvert,
                onValueChange = {
                    amountToConvert = it
                })
            Spacer(modifier = Modifier.height(30.dp))
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = { expanded = !expanded },
            ) {
                TextField(
                    readOnly = true,
                    value = selectedCurrency.name,
                    onValueChange = { },
                    label = { Text(text = "Select Currency") },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = {
                    expanded = false
                }) {
                    exchangeRates.forEach {
                        DropdownMenuItem(text = { Text(text = it.name + " - " + it.country) },
                            onClick = {
                                selectedCurrency = it
                                expanded = false
                            })
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            LazyVerticalGrid(verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                contentPadding = PaddingValues(
                    start = 22.dp, top = 16.dp, end = 22.dp, bottom = 16.dp
                ),
                columns = GridCells.Fixed(4),
                content = {
                    items(exchangeRates.size) { index ->
                        Card(
                            elevation = CardDefaults.cardElevation(3.dp),
                            shape = RectangleShape,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text(
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                text = convertRate(
                                    exchangeRates[index].exchangeRate,
                                    selectedCurrency.exchangeRate,
                                    amountToConvert.text.toInt()
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = exchangeRates[index].name,
                                modifier = Modifier
                                    .padding(3.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                })
        }
    }
}
