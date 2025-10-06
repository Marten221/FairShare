package com.example.fairshare.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fairshare.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageScreen() {
    Scaffold(floatingActionButton = {
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "FairShare",
                fontSize = dimensionResource(R.dimen.title).value.sp,
                modifier = Modifier.padding(bottom = 82.dp)
            )

            Button(
                onClick = {  },
                modifier = Modifier.size(width = 200.dp, height = 60.dp)
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = dimensionResource(R.dimen.button_text).value.sp
                )
            }

            Button(
                onClick = {  },
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_l).value.dp).size(width = 200.dp, height = 60.dp)
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = dimensionResource(R.dimen.button_text).value.sp
                )
            }
        }
    }
}