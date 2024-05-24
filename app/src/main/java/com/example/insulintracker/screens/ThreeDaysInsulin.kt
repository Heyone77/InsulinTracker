
import android.icu.text.DecimalFormat
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


@Composable
fun ThreeDaysInsulinScreen() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = {
                if (it.all { char -> char.isDigit() || char == '.' }) {
                    input = it
                }
            },
            label = { Text("Введите что то там") },
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
            modifier = Modifier.align(Alignment.End)
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
    }
}

fun calculateResult(value: Double): String {
    val result = 100 / (value / 3)
    val decimalFormat = DecimalFormat("#")
    return decimalFormat.format(result)
}
