package com.lab6.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lab6.data.entity.WeatherMain

@Composable
fun WeatherMainCustomView(weatherMain: WeatherMain, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Температура: ${weatherMain.temp} °C",
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Відчувається як: ${weatherMain.feels_like} °C",
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Вологість: ${weatherMain.humidity} %",
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Тиск: ${weatherMain.pressure} мм",
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun WeatherMainCustomViewPreview() {
    WeatherMainCustomView(
        weatherMain = WeatherMain(
            temp = 22.0,
            feels_like = 20.0,
            pressure = 760,
            humidity = 50
        )
    )
}
