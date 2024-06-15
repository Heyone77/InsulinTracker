package com.heysoft.insulintracker.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

data class ChatInfo(
    val title: String,
    val links: List<Pair<String, String>>
)

val chatCategories = listOf(
    ChatInfo("Библиотека", listOf("t.me/dialibrary" to "https://t.me/dialibrary")),
    ChatInfo("Общение", listOf(
        "t.me/diabet_help" to "https://t.me/diabet_help",
        "t.me/diateens" to "https://t.me/diateens",
        "Есть ещё секретный чат для общения самих подростков, провожает @Little_Donut" to "",
        "t.me/+riApGAIxwNU5N2Zi - чат Бориса Хомченко, ручка с шагом 0,1 для детей" to "https://t.me/+riApGAIxwNU5N2Zi",
        "t.me/diabetestype2 - 2 тип" to "https://t.me/diabetestype2",
        "t.me/dia_muscle - спорт" to "https://t.me/dia_muscle",
        "t.me/diabet_pregnancy - беременность" to "https://t.me/diabet_pregnancy",
        "t.me/gsd_gid - канал о ГСД с чатом" to "https://t.me/gsd_gid",
        "t.me/diagirls - только для женщин и девушек" to "https://t.me/diagirls"
    )),
    ChatInfo("Системы APS", listOf(
        "t.me/my_openaps" to "https://t.me/my_openaps",
        "t.me/freeapsx" to "https://t.me/freeapsx",
        "t.me/LoopAPS" to "https://t.me/LoopAPS",
        "t.me/androidapsgroup" to "https://t.me/androidapsgroup",
        "t.me/aiaps" to "https://t.me/aiaps",
        "t.me/oRangeRu" to "https://t.me/oRangeRu"
    )),
    ChatInfo("Блоги", listOf(
        "t.me/diabatya проверенные инструкции папы ребёнка с СД 1" to "https://t.me/diabatya",
        "t.me/cyber_dia" to "https://t.me/cyber_dia",
        "t.me/dia_dev" to "https://t.me/dia_dev",
        "t.me/diatalk_podcast" to "https://t.me/diatalk_podcast",
        "ЭНЦ t.me/neskuchno_o_diabete" to "https://t.me/neskuchno_o_diabete"
    )),
    ChatInfo("Мониторинги", listOf(
        "t.me/saharmonitorchat" to "https://t.me/saharmonitorchat",
        "t.me/diabet_belarus - белорусский чат по мониторингам" to "https://t.me/diabet_belarus",
        "t.me/xDrip_Russian" to "https://t.me/xDrip_Russian",
        "t.me/DiaboxApp" to "https://t.me/DiaboxApp",
        "t.me/LibreDEXomnipod" to "https://t.me/LibreDEXomnipod",
        "t.me/nightscoutRU" to "https://t.me/nightscoutRU",
        "t.me/diaprilozhenia - разные приложения" to "https://t.me/diaprilozhenia",
        "https://t.me/diabatya" to "https://t.me/diabatya",
        "Шприц-ручка с шагом 0.1ед. Бориса Хомченко" to "",
        "Автор и изобретатель шприц-ручки с шагом 0.1ед: https://t.me/+riApGAIxwNU5N2Zi" to "https://t.me/+riApGAIxwNU5N2Zi"
    )),
    ChatInfo("Помпы", listOf(
        "t.me/insulin_pumps - общий по помпам" to "https://t.me/insulin_pumps",
        "t.me/mon_640" to "https://t.me/mon_640"
    )),
    ChatInfo("Куплю/обменяю/продам", listOf(
        "🇷🇺 t.me/diabet_sale" to "https://t.me/diabet_sale",
        "🇷🇺 t.me/diaclub_ru - чат обмена и дарения, продажи в этом чате запрещены" to "https://t.me/diaclub_ru"
    )),
    ChatInfo("Льготы", listOf(
        "t.me/diaresursMGARDI" to "https://t.me/diaresursMGARDI",
        "t.me/diaresursmo - Московская область" to "https://t.me/diaresursmo",
        "t.me/dia_inv_18" to "https://t.me/dia_inv_18"
    )),
    ChatInfo("Чаты по регионам", listOf(
        "Беларусь - в чат добавляет @VENVeV (только для взрослых с СД 1)" to "",
        "Грузия t.me/diabet_ge" to "https://t.me/diabet_ge",
        "Казахстан t.me/joinchat/DjIo6z7JFQz5thdQ0SGIXA" to "https://t.me/joinchat/DjIo6z7JFQz5thdQ0SGIXA",
        "Астрахань t.me/Astrakhan_diabet" to "https://t.me/Astrakhan_diabet",
        "Башкортостан t.me/diabetsalebashkiria" to "https://t.me/diabetsalebashkiria",
        "Белгород - в чат добавляет @dia_domik" to "",
        "Волгоград t.me/diabet_34" to "https://t.me/diabet_34",
        "Воронеж t.me/DiaVORONEJ" to "https://t.me/DiaVORONEJ",
        "Дагестан t.me/joinchat/0RL_Com41AlhN2Fi" to "https://t.me/joinchat/0RL_Com41AlhN2Fi",
        "Екатеринбург t.me/diabet_ekb" to "https://t.me/diabet_ekb",
        "Иваново t.me/diabet_ivanovo" to "https://t.me/diabet_ivanovo",
        "Иркутск t.me/+4BMEo-6N0x4xOTgy" to "https://t.me/+4BMEo-6N0x4xOTgy",
        "Кавказ - в чат добавляет @Dr_Maks" to "",
        "Киров t.me/dia_kirov" to "https://t.me/dia_kirov",
        "Коми t.me/diabet11rus" to "https://t.me/diabet11rus",
        "Краснодар t.me/region93dia" to "https://t.me/region93dia",
        "Крым t.me/+9OaVTXvYoEA0Yjcy" to "https://t.me/+9OaVTXvYoEA0Yjcy",
        "Москва t.me/diabetmos" to "https://t.me/diabetmos",
        "Москва t.me/+2-M13r61bGpiZjdi" to "https://t.me/+2-M13r61bGpiZjdi",
        "Москва гл. эндокринолог t.me/diamoscow" to "https://t.me/diamoscow",
        "Нижний Новгород t.me/diabet_nn" to "https://t.me/diabet_nn",
        "Ростовская область - в чат добавляет @vlad_t1d" to "",
        "Самара t.me/dia163" to "https://t.me/dia163",
        "Саратов t.me/+u_VNDCYU2EpjMTZi" to "https://t.me/+u_VNDCYU2EpjMTZi",
        "Сахалин t.me/+7_PuNiL4JBthYTVi" to "https://t.me/+7_PuNiL4JBthYTVi",
        "СПб дети t.me/diakidspb" to "https://t.me/diakidspb",
        "СПб взрослые t.me/+QdIahrpgJyIxYzNi" to "https://t.me/+QdIahrpgJyIxYzNi",
        "Ставрополь t.me/+HiqCAV2hXBIwMmFi" to "https://t.me/+HiqCAV2hXBIwMmFi",
        "Татарстан t.me/dia_friends" to "https://t.me/dia_friends",
        "Ульяновск t.me/+lz3-EExX7DNlZTEy" to "https://t.me/+lz3-EExX7DNlZTEy",
        "Хабаровск t.me/diakhv" to "https://t.me/diakhv",
        "ХМАО t.me/+HwVNYHrbKFJhZjJi" to "https://t.me/+HwVNYHrbKFJhZjJi",
        "Якутия t.me/+ULxqgTofuf1kNTQy" to "https://t.me/+ULxqgTofuf1kNTQy"
    ))
)

@Composable
fun ChatsScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {
        Text(
            text = "Наполнение приложения информацией при поддержке чата ",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
        )
        Text(
            text = "https://t.me/dia_trio29",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface, textDecoration = TextDecoration.Underline),
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/dia_trio29"))
                context.startActivity(intent)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            items(chatCategories) { category ->
                ExpandableCard(
                    title = category.title,
                    content = {
                        category.links.forEach { (text, url) ->
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = if (url.isNotEmpty()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface,
                                    textDecoration = if (url.isNotEmpty()) TextDecoration.Underline else TextDecoration.None
                                ),
                                modifier = Modifier
                                    .clickable {
                                        if (url.isNotEmpty()) {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                            context.startActivity(intent)
                                        }
                                    }
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun ExpandableCard(
    title: String,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            if (expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    content()
                }
            }
        }
    }
}
