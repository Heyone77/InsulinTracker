
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import java.text.DecimalFormat


@Composable
fun RecountCarbsCountScreen() {
    var stal by remember { mutableStateOf("") }
    var byl by remember { mutableStateOf("") }
    var plan by remember { mutableStateOf("") }
    var pereraschetUK by remember { mutableStateOf<String?>(null) }
    var sredniyUK by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = stal,
            onValueChange = {
                if (it.all { char -> char.isDigit() || char == '.' }) {
                    stal = it
                }
            },
            label = { Text("УК сегодня (после приема пищи)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = byl,
            onValueChange = {
                if (it.all { char -> char.isDigit() || char == '.' }) {
                    byl = it
                }
            },
            label = { Text("Был (вчера)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = plan,
            onValueChange = {
                if (it.all { char -> char.isDigit() || char == '.' }) {
                    plan = it
                }
            },
            label = { Text("План (сегодня)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val stalValue = stal.toDoubleOrNull()
                val bylValue = byl.toDoubleOrNull()
                val planValue = plan.toDoubleOrNull()

                if (stalValue != null && bylValue != null && planValue != null) {
                    pereraschetUK = formatResult(calculatePereraschetUK(stalValue, bylValue, planValue))
                    sredniyUK = formatResult(calculateSredniyUK(stalValue, bylValue, planValue))
                } else {
                    pereraschetUK = "Invalid input"
                    sredniyUK = "Invalid input"
                }
            },
            modifier = Modifier.align(Alignment.End).fillMaxWidth()
        ) {
            Text("Рассчитать")
        }
        Spacer(modifier = Modifier.height(16.dp))
        pereraschetUK?.let {
            Text(
                text = "Перерасчет УК: $it",
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        sredniyUK?.let {
            Text(
                text = "Средний УК: $it",
                fontSize = 20.sp
            )
        }
    }
}

fun calculatePereraschetUK(stal: Double, byl: Double, plan: Double): Double {
    return stal / byl * plan
}

fun calculateSredniyUK(stal: Double, byl: Double, plan: Double): Double {
    val pereraschet = calculatePereraschetUK(stal, byl, plan)
    return (pereraschet + plan) / 2
}

fun formatResult(value: Double): String {
    val decimalFormat = DecimalFormat("#.##")
    return decimalFormat.format(value)
}
