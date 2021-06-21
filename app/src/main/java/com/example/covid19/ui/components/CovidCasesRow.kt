package com.example.covid19.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.covid19.R
import com.example.covid19.data.entity.CovidCasesModel
import com.example.covid19.ui.theme.*

@Composable
fun CovidCasesRow(
    covidCases: CovidCasesModel
) {
    CardRounded20(
        elevation = 0.5.dp,
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier.padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            ) {
                CustomCircle(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    circleColor = LightOrange,
                    ringColor = Orange
                )
                CustomCircle(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    circleColor = LightRed,
                    ringColor = Red
                )
                CustomCircle(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    circleColor = LightGreen,
                    ringColor = Green
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .align(CenterHorizontally)
            ) {
                val infected = covidCases.infected
                val cured = covidCases.recovered
                val dead = covidCases.dead
                CovidNumber(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp, end = 4.dp), color = Orange, number = infected
                )
                CovidNumber(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp, end = 4.dp), color = Red, number = dead
                )
                CovidNumber(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp, end = 8.dp), color = Green, number = cured
                )
            }
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .align(CenterHorizontally)
            ) {
                val modifier = Modifier.weight(1f)
                Text(
                    modifier = modifier,
                    style = Typography.body1,
                    color = MaterialTheme.colors.secondaryVariant,
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.infected)
                )
                Text(
                    modifier = modifier,
                    style = Typography.body1,
                    color = MaterialTheme.colors.secondaryVariant,
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.dead)
                )
                Text(
                    modifier = modifier,
                    style = Typography.body1,
                    color = MaterialTheme.colors.secondaryVariant,
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.recovered)
                )
            }
        }
    }
}

@Composable
fun CovidNumber(
    modifier: Modifier = Modifier,
    textSize: Int = 28,
    color: Color,
    number: Int
) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        fontSize = textSize.sp,
        style = TextStyle(color = color),
        text = adjustNumber(number)
    )
}

@Composable
fun CustomCircle(
    modifier: Modifier = Modifier,
    circleColor: Color,
    ringColor: Color
) {
    Box(modifier = modifier.wrapContentSize()) {
        Canvas(modifier = Modifier
            .size(25.dp)
            .align(Center), onDraw = {
            drawCircle(color = circleColor)
        })
        Canvas(modifier = Modifier
            .size(12.dp)
            .align(Center), onDraw = {
            drawCircle(color = ringColor)
        })
        Canvas(modifier = Modifier
            .size(8.dp)
            .align(Center), onDraw = {
            drawCircle(color = circleColor)
        })
    }
}

@Preview
@Composable
fun ShowCovidCases() {
    //CovidCasesRow(covidCases = CovidCases(356534, 421, 545545454))
}

fun adjustNumber(number: Int): String = when (number) {
    in (0..999) -> number.toString()
    in (1000..9999) -> "${number / 1000}.${number.toString()[1]}k"
    in (10000..99999) -> "${number / 1000}.${number.toString()[2]}k"
    in (100000..999999) -> "${number / 1000}.${number.toString()[3]}k"
    in (1000000..9999999) -> "${number / 1000000}.${number.toString()[1]}m"
    in (10000000..99999999) -> "${number / 1000000}.${number.toString()[2]}m"
    in (100000000..999999999) -> "${number / 1000000}.${number.toString()[3]}m"
    else -> "0"
}