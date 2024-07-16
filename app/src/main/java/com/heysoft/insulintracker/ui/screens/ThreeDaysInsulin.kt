package com.heysoft.insulintracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.heysoft.insulintracker.viewmodel.SharedViewModel
import java.util.Locale

@Composable
fun ThreeDaysInsulinScreen() {
    val sharedViewModel: SharedViewModel = hiltViewModel()
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "\uD83D\uDD34 ФЧИ (ФАКТОР ЧУВСТВИТЕЛЬНОСТИ К ИНСУЛИНУ) ИЛИ НА СКОЛЬКО 1 ЕДИНИЦА ИНСУЛИНА СНИЖАЕТ САХАР КРОВИ\n" +
                    "\uD83D\uDD34 Зачем нужно знать ФЧИ?\n" +
                    "⭕Для расчета углеводного коэффициента\n" +
                    "⭕Для коррекции высокого СК \n" +
                    "⭕Для снижения дозы пищевого инсулина, если СК низкий"
        )
        OutlinedTextField(
            value = input,
            onValueChange = {
                if (it.all { char -> char.isDigit() || char == '.' || char == ',' }) {
                    input = it.replace(',', '.')
                }
            },
            label = { Text("Кол-во инсулина за 3 дня (на еду)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val number = input.toDoubleOrNull()
                result = number?.let { calculateResult(it) } ?: "Неправильный ввод"
            },
            modifier = Modifier
                .align(Alignment.End)
                .fillMaxWidth()
        ) {
            Text("Вычислить")
        }
        Spacer(modifier = Modifier.height(16.dp))
        result?.let {
            Text(
                text = "Результат: $it",
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val number = result?.toDoubleOrNull()
                if (number != null) {
                    sharedViewModel.fchiValue.value = number
                }
            },
            modifier = Modifier
                .align(Alignment.End)
                .fillMaxWidth()
        ) {
            Text("Экспорт ФЧИ в расчёт УК")
        }
    }
}

fun calculateResult(value: Double): String {
    val result = 100 / (value / 3)
    return String.format(Locale.US, "%.2f", result)
}
