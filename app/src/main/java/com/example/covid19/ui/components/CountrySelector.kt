package com.example.covid19.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.covid19.R
import com.example.covid19.data.entity.Country
import com.example.covid19.ui.theme.*

@Composable
fun CountrySelector(
    modifier: Modifier = Modifier,
    defaultCountry: Country,
    countries: List<Country>,
    onCountrySelected: (Country) -> Unit = {}
) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    var selectedCountry: Country by remember { mutableStateOf(defaultCountry) }
    Card(
        shape = RoundedCornerShape(20),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = { expanded = true }),
        backgroundColor = White,
        border = BorderStroke(0.5.dp, Grey)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentHeight()
                .clickable(onClick = { expanded = true }),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = CenterVertically
        ) {
            Box {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "DropDown Menu",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(24.dp),
                    tint = Blue
                )
            }
            Text(
                modifier = Modifier
                    .align(CenterVertically)
                    .fillMaxWidth()
                    .weight(1f),
                text = selectedCountry.name,
                style = Typography.body1,
                color = Black
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dropdown),
                    contentDescription = "DropDown Menu",
                    modifier = Modifier
                        .align(CenterEnd)
                        .size(14.dp)
                        .clickable(onClick = { expanded = true }),
                    tint = Blue
                )
                DropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    for (country in countries) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            selectedCountry = country
                            onCountrySelected(country)
                        }) {
                            Text(
                                text = country.name,
                                style = Typography.body1,
                                color = Black
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCountrySelector() {
    CountrySelector(modifier = Modifier.padding(16.dp), Country("Belarus"), listOf(Country("Belarus"), Country("Russia")))
}