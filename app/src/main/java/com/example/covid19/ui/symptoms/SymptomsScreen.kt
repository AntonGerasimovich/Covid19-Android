package com.example.covid19.ui.symptoms

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.covid19.R
import com.example.covid19.ui.components.CardRounded10
import com.example.covid19.ui.components.CardRounded20
import com.example.covid19.ui.components.CurvedShape
import com.example.covid19.ui.theme.*

@Composable
fun SymptomsScreen(
    onMenuClick: () -> Unit = {}
) {
    HeaderAndBody(
        onMenuClick = onMenuClick,
        backgroundImageRes = R.drawable.coronadr,
        titleRes = R.string.get_to_know_about_covid19
    ) {
        SymptomsBody()
    }
}

@Composable
fun HeaderAndBody(
    onMenuClick: () -> Unit,
    @DrawableRes backgroundImageRes: Int,
    @StringRes titleRes: Int,
    body: @Composable () -> Unit
) {
    val gradient = Brush.linearGradient(0f to Blue, 400f to DeepBlue)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(brush = gradient)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 70.dp, end = 16.dp),
            text = stringResource(id = titleRes),
            style = Typography.h5,
            color = White
        )
        Image(
            painter = painterResource(id = backgroundImageRes),
            modifier = Modifier
                .offset(60.dp, 50.dp)
                .wrapContentSize(),
            contentDescription = null
        )
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.virus),
                    modifier = Modifier
                        .align(Center)
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .height(200.dp),
                    contentDescription = null
                )
            }
            CurvedShape(
                modifier = Modifier
                    .height(400.dp * 0.2f)
                    .fillMaxWidth()
            )
            Surface(modifier = Modifier.fillMaxSize(), color = MilkWhite) {
                Column {
                    body()
                }
            }
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_menu),
            contentDescription = "Menu",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .clickable { onMenuClick() }
                .padding(8.dp)
                .wrapContentSize(),
            tint = White
        )
    }
}

@Composable
fun SymptomsBody() {
    SymptomsSection()
    PreventionSection(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
}

@Composable
fun PreventionSection(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            modifier = modifier.padding(top = 32.dp),
            text = stringResource(id = R.string.prevention),
            style = Typography.h6,
            color = Black
        )
        PreventionCard(
            modifier = modifier,
            imageRes = R.drawable.wear_mask,
            title = R.string.wear_mask,
            description = R.string.wear_mask_description
        )
        PreventionCard(
            modifier = modifier,
            imageRes = R.drawable.wash_hands,
            title = R.string.wash_hands,
            description = R.string.wash_hands_description
        )
    }
}

@Composable
fun PreventionCard(
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int,
    @StringRes title: Int,
    @StringRes description: Int
) {
    Box(
        modifier = modifier
            .height(150.dp)
            .fillMaxWidth()
    ) {
        CardRounded20(
            modifier = Modifier
                .height(134.dp)
                .align(BottomCenter)
                .fillMaxWidth(),
            elevation = 0.dp
        ) {
            Box(modifier = Modifier.matchParentSize()) {
                Column(modifier = Modifier.padding(start = 135.dp, top = 16.dp)) {
                    Text(
                        text = stringResource(id = title),
                        style = Typography.h6,
                        color = Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = stringResource(id = description),
                        style = Typography.body1,
                        color = Grey,
                        fontSize = 12.sp
                    )
                }
                Icon(
                    modifier = Modifier
                        .align(BottomEnd)
                        .padding(16.dp),
                    painter = painterResource(id = R.drawable.forward),
                    contentDescription = null
                )
            }
        }
        Image(
            modifier = Modifier
                .size(150.dp)
                .align(CenterStart),
            painter = painterResource(id = imageRes),
            contentDescription = null
        )
    }
}

@Composable
fun SymptomsSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            text = stringResource(id = R.string.symptoms),
            style = Typography.h6,
            color = Black
        )
        Row(
            modifier = modifier
                .wrapContentHeight()
                .horizontalScroll(rememberScrollState())
        ) {
            SymptomCard(
                modifier = Modifier.padding(start = 16.dp),
                symptomRes = R.string.headache,
                symptomIconRes = R.drawable.headache
            )
            SymptomCard(
                modifier = Modifier.padding(start = 16.dp),
                symptomRes = R.string.cough,
                symptomIconRes = R.drawable.caugh
            )
            SymptomCard(
                modifier = Modifier.padding(start = 16.dp),
                symptomRes = R.string.fever,
                symptomIconRes = R.drawable.fever
            )
        }
    }
}

@Composable
fun SymptomCard(
    modifier: Modifier = Modifier,
    @StringRes symptomRes: Int,
    @DrawableRes symptomIconRes: Int
) {
    CardRounded10(modifier = modifier.wrapContentSize(), elevation = 0.5.dp) {
        Column(modifier = Modifier
            .padding(8.dp)
            .padding(start = 8.dp, end = 8.dp)) {
            Box {
                Image(
                    modifier = Modifier
                        .align(Center)
                        .matchParentSize(),
                    painter = painterResource(id = R.drawable.shape_small),
                    contentDescription = null
                )
                Image(
                    modifier = Modifier
                        .align(Center)
                        .size(80.dp),
                    painter = painterResource(id = symptomIconRes),
                    contentDescription = null
                )
            }
            Text(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(top = 8.dp),
                text = stringResource(id = symptomRes),
                style = Typography.body1,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = Black
            )
        }
    }
}

@Preview
@Composable
fun PreviewSymptoms() {
    SymptomsScreen()
}