package com.heysoft.insulintracker

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

fun getInfoTextForScreen(screenName: String): String {
    return when (screenName) {
        "Расчёт УК" -> "Количество инсулина, необходимое для компенсации определенного количества углеводов. Это величина индивидуальная и отражает собственную потребность в инсулине каждого диабетика. \n" +
                "УК отличается в разное время суток. Обычно максимальное значение фиксируется на завтрак с постепенным снижением к ужину. Ранний завтрак, например, перед школой и поздний в выходной день пройдут с разными УК. Поэтому, подбирая компенсационную дозу, желательно принимать пищу в одно и то же время. \n" +
                "Для определения УК нужно: \n" +
                "\tТочно считать еду, взвешивать и учитывать все, что попадает в рот. \n" +
                "\tЗнать ФЧИ (расчет ФЧИ доступен в главном меню данного приложения). \n" +
                "Измерять СК на старте перед едой и на отработке болюса (через 4-5 часов)."

        "Перерасчет УК" -> "Перерасчет УК необходимо для того, чтобы скорректировать коэффициент на предстоящий прием пищи исходя из потребности (в случае ее изменения) опираясь на предыдущий прием пищи."
        "ФЧИ" -> "Здесь могла быть ваша реклама"
        "О приложении" -> "Это информация о приложении и его назначении."
        else -> "Информация недоступна."
    }
}

fun calculateUk(
    stSk: Double,
    otrabotkaSk: Double,
    fchi: Double,
    doz: Double,
    carbs: Double,
    xe: Int
): Double {
    val uk = ((stSk - otrabotkaSk) / fchi + doz) / (carbs / xe)
    return uk.roundToInt().toDouble()
}

fun getCurrentDate(): String {
    val dateFormatter = SimpleDateFormat("dd-MMM-yy", Locale.getDefault())
    return dateFormatter.format(Calendar.getInstance().time)
}

fun dateToUnix(date: String): Long {
    val dateFormatter = SimpleDateFormat("dd-MMM-yy", Locale.getDefault())
    return dateFormatter.parse(date)?.time ?: 0L
}

fun unixToDate(unixTime: Long): String {
    val dateFormatter = SimpleDateFormat("dd-MMM-yy", Locale.getDefault())
    return dateFormatter.format(Date(unixTime))
}

fun mealTimeToInt(mealTime: String): Int {
    return when (mealTime) {
        "Завтрак" -> 1
        "Второй завтрак" -> 2
        "Обед" -> 3
        "Полдник" -> 4
        "Ужин" -> 5
        "Поздний ужин" -> 6
        else -> 0
    }
}

fun intToMealTime(mealTimeInt: Int): String {
    return when (mealTimeInt) {
        1 -> "Завтрак"
        2 -> "Второй завтрак"
        3 -> "Обед"
        4 -> "Полдник"
        5 -> "Ужин"
        6 -> "Поздний ужин"
        else -> "Неизвестно"
    }
}
