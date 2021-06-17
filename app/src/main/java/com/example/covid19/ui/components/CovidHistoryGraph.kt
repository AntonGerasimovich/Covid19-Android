package com.example.covid19.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.covid19.R
import com.example.covid19.data.entity.CaseType
import com.example.covid19.data.entity.CaseType.Companion.dropdownContentCaseType
import com.example.covid19.data.entity.GraphDataModel
import com.example.covid19.data.entity.TimestampModel
import com.example.covid19.data.entity.TimestampModel.Companion.dropdownContentTimeStamp
import com.example.covid19.ui.overview.OverviewViewModel
import com.example.covid19.ui.theme.*
import com.example.covid19.utils.convertToNormalDate
import kotlin.math.roundToInt

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
        backgroundColor = MaterialTheme.colors.surface,
        border = BorderStroke(0.5.dp, DarkGrey),
        elevation = 0.dp
    ) {
        val graphData = overviewViewModel.graphData.collectAsState()
        var selectedPoint by remember { mutableStateOf(0) }
        if (graphData.value.isNotEmpty()) {
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
                CovidCasesGraph(
                    modifier
                        .fillMaxWidth()
                        .height(150.dp), graphData.value,
                    onPointSelected = { selectedPoint = it }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Start)
                        .padding(16.dp)
                ) {
                    val number =
                        if (selectedPoint in graphData.value.indices) graphData.value[selectedPoint].active else 0
                    val date =
                        if (selectedPoint in graphData.value.indices) convertToNormalDate(graphData.value[selectedPoint].date) else ""
                    GraphHint(
                        modifier = Modifier.weight(1f),
                        circleColor = GraphBlue,
                        text = stringResource(id = R.string.cases) + " $number"
                    )
                    GraphHint(
                        modifier = Modifier.weight(1f),
                        circleColor = GraphDeepBlue,
                        text = stringResource(id = R.string.average)
                    )
                    GraphHint(
                        modifier = Modifier.weight(1f),
                        circleColor = Green,
                        text = date
                    )
                }
            }
        }
    }
}

@Composable
fun GraphHint(modifier: Modifier = Modifier, circleColor: Color, text: String) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(end = 16.dp)
    ) {
        Canvas(
            modifier = Modifier
                .align(CenterVertically)
                .wrapContentSize()
                .padding(end = 8.dp)
        ) {
            drawCircle(color = circleColor, radius = 14f)
        }
        Text(
            modifier = Modifier.align(CenterVertically),
            text = text,
            style = Typography.subtitle2,
            color = DarkGrey,
            maxLines = 1
        )
    }
}

@Composable
fun CovidCasesGraph(
    modifier: Modifier = Modifier,
    graphData: List<GraphDataModel>,
    onPointSelected: (Int) -> Unit
) {
    Box(modifier = modifier) {
        var offsetX by remember { mutableStateOf(150f) }
        var xStep by remember { mutableStateOf(1f) }
        GraphFigure(graphData, onStepCalculated = {
            xStep = it
        })
        AverageGraphLine(graphData)
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .fillMaxHeight()
                .width(8.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consumeAllChanges()
                        offsetX += dragAmount.x
                        onPointSelected(((offsetX + (size.width / 2)) / xStep).toInt())
                    }
                }
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawPath(
                    path = Path().apply {
                        reset()
                        moveTo(size.width / 2, 0f)
                        lineTo(size.width / 2, size.height)
                        close()
                    },
                    color = DarkGrey,
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    )
                )
            }
        }
    }
}

@Composable
private fun GraphFigure(graphData: List<GraphDataModel>, onStepCalculated: (Float) -> Unit) {
    val shape = GenericShape { size, _ ->
        onStepCalculated(size.width / graphData.size.toFloat())
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
    Surface(shape = shape, color = GraphBlue, modifier = Modifier.fillMaxSize()) {}
}

@Composable
private fun AverageGraphLine(graphData: List<GraphDataModel>) {
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
            color = GraphDeepBlue,
            start = startPoint,
            end = Offset(xStep, yStep),
            strokeWidth = 4f
        )
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
        shape = RoundedCornerShape(40),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
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