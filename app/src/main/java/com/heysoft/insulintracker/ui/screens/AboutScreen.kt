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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heysoft.insulintracker.R

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
            AboutText(text = stringResource(R.string.about_text_1))
            AboutText(text = stringResource(R.string.about_text_2))
            AboutText(text = stringResource(R.string.about_text_3))
            AboutText(
                text = stringResource(R.string.about_text_4) +
                        stringResource(R.string.about_text_5) +
                        stringResource(R.string.about_text_6) +
                        stringResource(R.string.about_text_7) +
                        stringResource(R.string.about_text_8) +
                        stringResource(R.string.about_text_9)
            )
        }
    }
}

@Composable
fun AboutText(text: String) {
    Text(
        text = text,
        fontSize = 19.sp,
        lineHeight = 28.sp,
        modifier = Modifier.padding(bottom = 16.dp),
        textAlign = TextAlign.Justify
    )
}