package com.lab6.ui.screens.forecast

import androidx.compose.ui.platform.LocalContext
import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lab6.ui.components.WeatherMainCustomView
import org.koin.androidx.compose.getViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight

@Composable
fun WeatherForecastScreen(
    viewModel: WeatherForecastScreenViewModel = getViewModel()
) {
    val context = LocalContext.current
    val weatherForecastResponseState = viewModel.weatherForecastResponseStateFlow.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf(viewModel.cities.first()) }
    var selectedDate by remember { mutableStateOf<Date?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Прогноз погоди",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
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
                            viewModel.fetchWeatherForecast(city)
                        },
                        text = { Text(city, fontSize = 16.sp) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val calendar = Calendar.getInstance()
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        selectedDate = Calendar.getInstance().apply {
                            set(year, month, day)
                        }.time
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = selectedDate?.let {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
                } ?: "Виберіть дату",
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        weatherForecastResponseState.value?.list?.let { weatherForecastList ->
            val filteredForecasts = selectedDate?.let { date ->
                val selectedDateString =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
                weatherForecastList.filter {
                    SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(Date(it.dt * 1000)) == selectedDateString
                }
            } ?: weatherForecastList

            LazyColumn {
                items(filteredForecasts) { weatherForecast ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Дата: ${
                                SimpleDateFormat(
                                    "yyyy-MM-dd",
                                    Locale.getDefault()
                                ).format(Date(weatherForecast.dt * 1000))
                            }",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "Час: ${
                                SimpleDateFormat("HH:mm", Locale.getDefault()).format(
                                    Date(weatherForecast.dt * 1000)
                                )
                            }",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    WeatherMainCustomView(weatherMain = weatherForecast.main)
                }
            }
        }
    }
}
