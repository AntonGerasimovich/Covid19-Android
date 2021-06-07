package com.example.covid19.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.covid19.R

@Composable
fun TopTabBar(
    onMenuClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(TabHeight)
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        Row(
            Modifier
                .background(Color.Transparent)
                .padding(end = TabHeight / 2),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.wrapContentSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "Menu",
                    modifier = Modifier.clickable(onClick = onMenuClick)
                )
            }
        }
    }
}

private val TabHeight = 56.dp