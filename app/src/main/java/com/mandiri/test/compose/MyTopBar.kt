package com.mandiri.test.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mandiri.test.ui.theme.TempoRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarNormal(
    title: String,
    icon: ImageVector = Icons.Default.ArrowBack,
    navController: NavHostController,
    useBack:Boolean = true
) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = TempoRed),
        navigationIcon = {
            if (useBack)
            IconButton(
                onClick = {
                    navController.navigateUp()
                },
            ) {
                Icon(
                    tint = Color.White,
                    imageVector = icon,
                    contentDescription = ""
                )
            }
        },
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    color = Color.White,
                    text = title,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        })
}