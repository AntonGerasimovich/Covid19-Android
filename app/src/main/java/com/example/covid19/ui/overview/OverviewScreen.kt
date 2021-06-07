package com.example.covid19.ui.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.covid19.R
import com.example.covid19.data.entity.Country
import com.example.covid19.data.entity.CovidCases
import com.example.covid19.ui.components.CountrySelector
import com.example.covid19.ui.components.CovidCasesRow
import com.example.covid19.ui.components.SpreadOfVirusMap
import com.example.covid19.ui.symptoms.HeaderAndBody
import com.example.covid19.ui.theme.Black
import com.example.covid19.ui.theme.DeepBlue
import com.example.covid19.ui.theme.Grey
import com.example.covid19.ui.theme.Typography
import com.google.android.gms.maps.model.LatLng

@Composable
fun OverviewScreen(
    onMenuClick: () -> Unit,
    onCaseUpdateDetailsClick: () -> Unit = {},
    onSpreadOfVirusDetailsClick: () -> Unit = {}
) {
    HeaderAndBody(
        onMenuClick = onMenuClick,
        backgroundImageRes = R.drawable.drcorona,
        titleRes = R.string.all_you_need_is_stay_home
    ) {
        OverviewBody(
            "March 28",
            onCaseUpdateDetailsClick = {
                onCaseUpdateDetailsClick()
            }, onCountrySelected = {
                // TODO make a request
            }, onSpreadOfVirusDetailsClick = {
                onSpreadOfVirusDetailsClick()
            }
        )
    }
}

@Composable
fun OverviewBody(
    date: String,
    onCaseUpdateDetailsClick: () -> Unit,
    onCountrySelected: (Country) -> Unit,
    onSpreadOfVirusDetailsClick: () -> Unit
) {
    CountrySelector(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        defaultCountry = Country("Belarus"),
        countries = listOf(Country("Belarus"), Country("Russia")),
        onCountrySelected = onCountrySelected
    )
    CaseUpdateSection(CovidCases(5327, 3467, 432), date, onCaseUpdateDetailsClick)
    SpreadOfVirusSection(onSpreadOfVirusDetailsClick)
}

@Composable
private fun SpreadOfVirusSection(onSpreadOfVirusDetailsClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = stringResource(id = R.string.spread_of_virus),
                    style = Typography.h6,
                    color = Black
                )
            }
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { onSpreadOfVirusDetailsClick() }
                    .align(Alignment.CenterEnd), text = stringResource(id = R.string.see_details),
                style = Typography.button,
                color = DeepBlue
            )
        }
        SpreadOfVirusMap(latLng = LatLng(56.0, 27.0))
    }
}

@Composable
private fun CaseUpdateSection(covidCases: CovidCases, date: String, onSeeDetailsClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        CaseUpdateTitle(date = date, onSeeDetailsClick = onSeeDetailsClick)
        CovidCasesRow(covidCases = covidCases)
    }
}

@Composable
fun CaseUpdateTitle(date: String, onSeeDetailsClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.CenterStart)
        ) {
            Text(
                modifier = Modifier.wrapContentSize(),
                text = stringResource(id = R.string.case_update),
                style = Typography.h6,
                color = Black
            )
            Text(
                modifier = Modifier.wrapContentSize(),
                text = stringResource(id = R.string.newest_update) + " $date",
                style = Typography.subtitle2,
                color = Grey
            )
        }
        Text(
            modifier = Modifier
                .wrapContentSize()
                .clickable {
                    onSeeDetailsClick()
                }
                .align(Alignment.CenterEnd), text = stringResource(id = R.string.see_details),
            style = Typography.button,
            color = DeepBlue
        )
    }
}

@Preview
@Composable
fun PreviewOverview() {
    OverviewScreen({})
}