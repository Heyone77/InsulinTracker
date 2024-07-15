package com.heysoft.insulintracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp, bottom = 21.dp, start = 21.dp, end = 21.dp),
        contentAlignment = Alignment.TopStart
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Данное приложение разработано для помощи в расчетах Углеводных коэффициентов в целях расчета доз болюсов. Все исходные данные, необходимые для расчета вносятся исключительно пользователем, что в итоге влияет на конечный результат. Программа не вмешивается во вносимые пользователем исходные сведения, в следствии чего, за все итоговые расчеты при помощи данного приложения ответственность несет исключительно пользователь.",
                fontSize = 19.sp,
                lineHeight = 28.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Justify
            )
            Text(
                text = "Все полученные данные по результатам расчетов/перерасчетов Углеводных коэффициентов должны применяться на практике исходя из текущего состояния пользователя, а также его текущей потребности в дозировках болюса.",
                fontSize = 19.sp,
                lineHeight = 28.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Justify
            )
            Text(
                text = "В случае возникновения спорных ситуаций по результатам расчетов программы, необходимо незамедлительно совершить перерасчет дозировки любым из известных вам способов вне приложения, для проверки корректности работы приложения, либо исправления внесенных пользователем исходных данных.",
                fontSize = 19.sp,
                lineHeight = 28.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Justify
            )
            Text(
                text = "Проверка адекватности Углеводных коэффициентов для расчета доз болюсов проводится при условиях:\n" +
                        "- близкого к целевым исходного показателя гликемии;\n" +
                        "- приема стандартного количества пищи, количество углеводов в которой легко просчитать;\n" +
                        "- обычной физической активности в течение 4 часов после еды;\n" +
                        "- отсутствие воспалительных заболеваний/стресса;\n" +
                        "- не в первые 4-6 часов после перенесенной гипогликемии.",
                fontSize = 19.sp,
                lineHeight = 28.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Justify
            )
        }
    }
}