package com.lab6.ui.screens.current

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lab6.ui.components.WeatherMainCustomView
import org.koin.androidx.compose.getViewModel
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun WeatherScreen(
    viewModel: WeatherScreenViewModel = getViewModel()
) {
    val weatherResponseState = viewModel.weatherResponseStateFlow.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf(viewModel.cities.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Поточна погода",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            color = Color.Black
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Button(
                onClick = { expanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Місто: $selectedCity",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                viewModel.cities.forEach { city ->
                    DropdownMenuItem(
                        onClick = {
                            selectedCity = city
                            expanded = false
                            viewModel.fetchWeather(city)
                        },
                        text = { Text(city, fontSize = 16.sp) }
                    )
                }
            }
        }

        Text(
            text = "Місто: ${weatherResponseState.value?.name ?: "Невідомо"}",
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )

        weatherResponseState.value?.main?.let { weatherMain ->
            WeatherMainCustomView(weatherMain = weatherMain)
        }
    }
}
