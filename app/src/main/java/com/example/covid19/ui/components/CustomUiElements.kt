package com.example.covid19.ui.components

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.covid19.ui.theme.MilkWhite
import com.example.covid19.ui.theme.White

@Composable
fun CardRounded10(
    modifier: Modifier = Modifier,
    backgroundColor: Color = White,
    elevation: Dp = 2.dp,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = backgroundColor,
        elevation = elevation,
        content = content
    )
}

@Composable
fun CardRounded20(
    modifier: Modifier = Modifier,
    backgroundColor: Color = White,
    elevation: Dp = 2.dp,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        backgroundColor = backgroundColor,
        elevation = elevation,
        content = content
    )
}

@Composable
fun CurvedShape(
    modifier: Modifier = Modifier
) {
    val curvedShape = GenericShape { size, _ ->
        quadraticBezierTo(
            size.width * 0.5f,
            size.height,
            size.width,
            0f
        )
        lineTo(size.width, size.height)
        lineTo(0f, size.height)
        lineTo(0f, 0f)
        close()
    }

    Surface(
        shape = curvedShape,
        color = MilkWhite,
        modifier = modifier
    ) { }
}