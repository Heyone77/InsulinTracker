package com.heysoft.insulintracker.utils

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.heysoft.insulintracker.network.ApiService
import com.heysoft.insulintracker.network.UserRequest
import com.heysoft.insulintracker.network.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

fun getInfoTextForScreen(screenName: String): String {
    return when (screenName) {
        "Расчёт УК" -> "УГЛЕВОДНЫЕ КОЭФФИЦИЕНТЫ (УК) – количество инсулина, необходимое для компенсации определенного количества углеводов. Это величина индивидуальная и отражает собственную потребность в инсулине каждого диабетика. \n" + "УК отличается в разное время суток. Обычно максимальное значение фиксируется на завтрак с постепенным снижением к ужину. Ранний завтрак, например, перед школой и поздний в выходной день пройдут с разными УК. Поэтому, подбирая компенсационную дозу, желательно принимать пищу в одно и то же время. \n" + "Для определения УК нужно: \n" + "\tТочно считать еду, взвешивать и учитывать все, что попадает в рот. \n" + "\tЗнать ФЧИ (расчет ФЧИ доступен в главном меню данного приложения). \n" + "Измерять СК на старте перед едой и на отработке болюса (через 4-5 часов)."
        "Перерасчет УК" -> "Описание принципа работы перерасчета на примере перерасчета УК на \"Ужин\":\n" + "Нам необходимо пересчитать запланированный УК на \"Ужин\" (УК предстоящего приема пищи). \n" + "По отработке \"Обеда\" мы видим явное изменение УК (в нашем случае - пришлось докармливать сверх заложенных в болюс углеводов)\n" + "Для перерасчета Ужина нам необходимо определить на сколько в % поменялась потребность на \"Обед\" и уменьшить на этот же % УК \"Ужина\" (предстоящего приема пищи).\n" + "Алгоритм действий:\n" + "1) В ячейку \"УК предыдущего приема пищи (сегодня)\" мы вносим новый изменившийся УК сегодняшнего обеда Для примера он у нас стал - 0.46\n" + "2) В ячейку \"УК предыдущего приема пищи (вчера)\" мы вносим УК вчерашнего обеда - например он у нас был - 0.52\n" + "В ячейку \"УК предстоящего приема пищи (план) мы вносим запланированный на Ужин УК - например он у нас по вчерашней отработке был равен - 0.32\n" + "После нажатия кнопки \"Рассчитать\" мы получим два результата:\n" + "Средний УК: 0.30\n" + "Перерасчет УК: 0.28\n" + "Их можно использовать в текущем приеме пищи. Если есть сомнения в изменении УК то воспользуетесь расчетным значением \"Средний УК\", он будет не сильно отличаться от планируемого, но в таком случае возможно потребуется подкормить, либо не выдавать отложенную еду (если таковая имеется)\n" + "ВАЖНО! \n" + "Вы должны хорошо понимать работу коэффициентов и как на них виляет время приема пиши и ее состав. В том числе  изменение коэффициента возможно и при наличии большего или меньшего количества белков и жиров в составе еды. Если два приема пищи (сегодня и вчера) отличаются по своему составу, то при перерасчете это необходимо учесть!"
        "ФЧИ" -> "Здесь могла быть ваша реклама"
        "О приложении" -> "Это информация о приложении и его назначении."
        else -> "Информация недоступна."
    }
}

fun calculateUk(
    stSk: Double, otrabotkaSk: Double, fchi: Double, doz: Double, carbs: Double, xe: Int
): Double {
    val skDifference = otrabotkaSk - stSk
    val skDifferenceRounded = String.format(Locale.US, "%.3f", skDifference).toDouble()
    Log.i("calculateUk", "SK Difference: $skDifferenceRounded")

    val divisionByFchi = skDifferenceRounded / fchi
    val divisionByFchiRounded = String.format(Locale.US, "%.3f", divisionByFchi).toDouble()
    Log.i("calculateUk", "Division by FCHI: $divisionByFchiRounded")

    val additionOfDoz = divisionByFchiRounded + doz
    val additionOfDozRounded = String.format(Locale.US, "%.3f", additionOfDoz).toDouble()
    Log.i("calculateUk", "Addition of Doz: $additionOfDozRounded")

    val carbsDivisionByXe = carbs / xe
    val carbsDivisionByXeRounded = String.format(Locale.US, "%.3f", carbsDivisionByXe).toDouble()
    Log.i("calculateUk", "Carbs Division by XE: $carbsDivisionByXeRounded")

    val uk = additionOfDozRounded / carbsDivisionByXeRounded
    val ukRounded = String.format(Locale.US, "%.3f", uk).toDouble()
    Log.i("calculateUk", "Calculated UK: $ukRounded")

    return ukRounded
}

fun String.toDoubleOrNullWithCommaSupport(): Double? {
    val normalized = this.replace(',', '.')
    return normalized.toDoubleOrNull()
}

fun getCurrentDate(): String {
    val dateFormatter = SimpleDateFormat("dd MMM yy", Locale.getDefault())
    return dateFormatter.format(Calendar.getInstance().time)
}

fun dateToUnix(date: String): Long {
    val dateFormatter = SimpleDateFormat("dd MMM yy", Locale.getDefault())
    return dateFormatter.parse(date)?.time ?: 0L
}

fun unixToDate(unixTime: Long): String {
    val dateFormatter = SimpleDateFormat("dd MMM yy", Locale.getDefault())
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

fun calculateTimeDiff(date: String, time: String): Long {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val dateTimeString = "$date $time"
    val eventDate = dateFormat.parse(dateTimeString)
    val currentTime = Date()
    return eventDate?.time?.minus(currentTime.time) ?: 0L
}

fun getDeviceUUID(context: Context): String {
    val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    return if (androidId != null && androidId.isNotEmpty()) {
        val uuid = UUID.nameUUIDFromBytes(androidId.toByteArray())
        uuid.toString()
    } else {
        UUID.randomUUID().toString() // fallback in case ANDROID_ID is not available
    }
}

fun addUser(apiService: ApiService, uuid: String) {
    val request = UserRequest(uuid)
    apiService.addUser(request).enqueue(object : Callback<UserResponse> {
        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
            if (response.isSuccessful) {
                Log.e("addUser", "Success: ${response.code()}")
            } else {
                Log.e("addUser", "Error: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            Log.e("addUser", "Request failed", t)
        }
    })
}