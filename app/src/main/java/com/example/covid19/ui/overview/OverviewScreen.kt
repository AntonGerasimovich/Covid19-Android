package com.example.covid19.ui.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.covid19.R
import com.example.covid19.data.entity.CountryModel
import com.example.covid19.data.entity.CovidCasesModel
import com.example.covid19.ui.components.CountrySelector
import com.example.covid19.ui.components.CovidCasesRow
import com.example.covid19.ui.components.CovidHistoryGraph
import com.example.covid19.ui.components.SpreadOfVirusMap
import com.example.covid19.ui.symptoms.HeaderAndBody
import com.example.covid19.ui.theme.Typography
import com.example.covid19.utils.convertToNormalDate

@Composable
fun OverviewScreen(
    overviewViewModel: OverviewViewModel,
    onMenuClick: () -> Unit,
    onCaseUpdateDetailsClick: () -> Unit = {},
    onSpreadOfVirusDetailsClick: () -> Unit = {}
) {
    val covidCases = overviewViewModel.covidCases.collectAsState()
    val countries = overviewViewModel.countries.collectAsState()
    val selectedCountry = overviewViewModel.selectedCountry.collectAsState()
    val isDarkTheme = overviewViewModel.isDarkTheme.collectAsState()
    HeaderAndBody(
        isDarkTheme = isDarkTheme.value,
        onMenuClick = onMenuClick,
        onThemeSwitchClick = {
            overviewViewModel.changeAppTheme(isDarkTheme.value)
        },
        backgroundImageRes = R.drawable.drcorona,
        titleRes = R.string.all_you_need_is_stay_home
    ) {
        OverviewBody(
            overviewViewModel = overviewViewModel,
            selectedCountry = selectedCountry.value,
            countries = countries.value,
            covidCases = covidCases.value,
            onCaseUpdateDetailsClick = {
                onCaseUpdateDetailsClick()
            }, onCountrySelected = {
                overviewViewModel.getCovidCases(it)
            }, onSpreadOfVirusDetailsClick = {
                onSpreadOfVirusDetailsClick()
            }
        )
    }
}

@Composable
fun OverviewBody(
    overviewViewModel: OverviewViewModel,
    selectedCountry: CountryModel,
    countries: List<CountryModel>,
    covidCases: CovidCasesModel,
    onCaseUpdateDetailsClick: () -> Unit,
    onCountrySelected: (CountryModel) -> Unit,
    onSpreadOfVirusDetailsClick: () -> Unit
) {
    CountrySelector(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
        defaultSelectedCountry = selectedCountry,
        countries = countries,
        onCountrySelected = onCountrySelected
    )
    CaseUpdateSection(
        modifier = Modifier.padding(bottom = 16.dp),
        covidCases = covidCases,
        date = convertToNormalDate(covidCases.date),
        onSeeDetailsClick = onCaseUpdateDetailsClick
    )
    SpreadOfVirusSection(
        countryName = selectedCountry.name,
        onSpreadOfVirusDetailsClick = onSpreadOfVirusDetailsClick
    )
    CovidHistoryGraph(modifier = Modifier.padding(16.dp), overviewViewModel = overviewViewModel)
}

@Composable
private fun SpreadOfVirusSection(
    modifier: Modifier = Modifier,
    countryName: String,
    onSpreadOfVirusDetailsClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
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
                    color = MaterialTheme.colors.secondary
                )
            }
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { onSpreadOfVirusDetailsClick() }
                    .align(Alignment.CenterEnd), text = stringResource(id = R.string.see_details),
                style = Typography.button,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp
            )
        }
        SpreadOfVirusMap(countryName = countryName)
    }
}

@Composable
private fun CaseUpdateSection(
    modifier: Modifier = Modifier,
    covidCases: CovidCasesModel,
    date: String,
    onSeeDetailsClick: () -> Unit
) {
    Column(
        modifier = modifier
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
                color = MaterialTheme.colors.secondary
            )
            Text(
                modifier = Modifier.wrapContentSize(),
                text = stringResource(id = R.string.newest_update) + " $date",
                style = Typography.subtitle2,
                color = MaterialTheme.colors.secondary
            )
        }
        Text(
            modifier = Modifier
                .wrapContentSize()
                .clickable {
                    onSeeDetailsClick()
                }
                .align(Alignment.BottomEnd), text = stringResource(id = R.string.see_details),
            style = Typography.button,
            color = MaterialTheme.colors.primary,
            fontSize = 12.sp
        )
    }
}

@Preview
@Composable
fun PreviewOverview() {
    //OverviewScreen({})
}