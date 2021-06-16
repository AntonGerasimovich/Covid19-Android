package com.example.covid19.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.covid19.R
import com.example.covid19.data.entity.CaseType
import com.example.covid19.data.entity.CaseType.Companion.dropdownContentCaseType
import com.example.covid19.data.entity.GraphDataModel
import com.example.covid19.data.entity.TimestampModel
import com.example.covid19.data.entity.TimestampModel.Companion.dropdownContentTimeStamp
import com.example.covid19.ui.overview.OverviewViewModel
import com.example.covid19.ui.theme.Blue
import com.example.covid19.ui.theme.DeepBlue
import com.example.covid19.ui.theme.Typography

@Composable
fun CovidHistoryGraph(
    modifier: Modifier = Modifier,
    overviewViewModel: OverviewViewModel
) {
    Card(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = RoundedCornerShape(10),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        val graphData = overviewViewModel.graphData.collectAsState()
        Column(modifier = modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .align(Start)
                    .padding(8.dp)
            ) {
                DropdownPicker(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    title = CaseType.NewCases().name,
                    dropdownContent = dropdownContentCaseType
                ) { overviewViewModel.onCaseTypePicked(CaseType.getCaseType(it)) }
                DropdownPicker(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    title = TimestampModel.AllTime().timestamp,
                    dropdownContent = dropdownContentTimeStamp
                ) { overviewViewModel.onTimestampPicked(TimestampModel.getTimestamp(it)) }
            }
            if (graphData.value.isNotEmpty()) {
                CovidCasesGraph(
                    modifier
                        .fillMaxWidth()
                        .height(150.dp), graphData.value
                )
            }
        }
    }
}

@Composable
fun CovidCasesGraph(
    modifier: Modifier = Modifier,
    graphData: List<GraphDataModel>
) {
    Box(modifier = modifier) {
        val shape = GenericShape { size, _ ->
            moveTo(0f, size.height)
            val maxElementHeight =
                (graphData.map { it.active }.maxOrNull()!! / size.height).times(100)
            var xStep = 0f
            var yStep = size.height
            for (value in graphData) {
                yStep = size.height - 100f * value.active.toFloat() / maxElementHeight
                lineTo(xStep, yStep)
                xStep += size.width / graphData.size.toFloat()
            }
            lineTo(xStep, yStep)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        Surface(shape = shape, color = Blue, modifier = Modifier.fillMaxSize()) {}
        Canvas(modifier = Modifier.fillMaxSize()) {
            val maxElementHeight =
                (graphData.map { it.active }.maxOrNull()!! / size.height).times(100)
            var xStep = 0f
            var yStep: Float = size.height
            var startPoint = Offset(
                0f,
                size.height - 100f * graphData.first().active.toFloat() / maxElementHeight
            )
            for (index in graphData.indices) {
                val average = takeAverageForSevenDays(index, graphData)
                yStep = size.height - 100f * average.toFloat() / maxElementHeight
                drawLine(
                    color = DeepBlue,
                    start = startPoint,
                    end = Offset(xStep, yStep),
                    strokeWidth = 4f
                )
                xStep += size.width / graphData.size.toFloat()
                startPoint = Offset(xStep - size.width / graphData.size.toFloat(), yStep)
            }
            drawLine(
                color = DeepBlue,
                start = startPoint,
                end = Offset(xStep, yStep),
                strokeWidth = 4f
            )
        }
    }
}

private fun takeAverageForSevenDays(index: Int, data: List<GraphDataModel>): Int {
    var n = 0
    var average = 0
    for (i in (index - 7)..index) {
        if (i >= 0) {
            average += data[i].active
            n++
        }
    }
    return average / n
}

@Composable
fun DropdownPicker(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    dropdownContent: ArrayList<Int>,
    onItemPicked: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(title) }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = { expanded = true })
                .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                .wrapContentSize()
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 4.dp)
                    .wrapContentSize(),
                text = stringResource(id = selectedItem),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                style = Typography.h6,
                color = MaterialTheme.colors.secondary
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_dropdown),
                contentDescription = "DropDown Menu",
                modifier = Modifier
                    .align(CenterVertically)
                    .padding(start = 4.dp, end = 8.dp)
                    .size(14.dp)
                    .clickable(onClick = { expanded = true }),
                tint = MaterialTheme.colors.primaryVariant
            )
            DropdownMenu(
                modifier = Modifier
                    .wrapContentSize()
                    .background(MaterialTheme.colors.background),
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                for (item in dropdownContent) {
                    DropdownMenuItem(
                        modifier = Modifier.background(MaterialTheme.colors.background),
                        onClick = {
                            expanded = false
                            selectedItem = item
                            onItemPicked(item)
                        }) {
                        Text(
                            text = stringResource(id = item),
                            style = Typography.body1,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ShowGraph() {
    //CovidHistoryGraph()
}