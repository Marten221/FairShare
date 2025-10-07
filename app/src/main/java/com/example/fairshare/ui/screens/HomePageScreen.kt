package com.example.fairshare.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fairshare.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageScreen(
    onSignInSuccess: () -> Unit
) {
    var showSignIn by rememberSaveable { mutableStateOf(false) }

    Scaffold() { padding ->
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
                onClick = { showSignIn = true },
                modifier = Modifier.size(width = 200.dp, height = 60.dp)
            ) {
                Text(
                    text = "Sign In",
                    fontSize = dimensionResource(R.dimen.button_text).value.sp
                )
            }

            Button(
                onClick = {  },
                modifier = Modifier
                    .padding(top = dimensionResource(R.dimen.spacing_l).value.dp)
                    .size(width = 200.dp, height = 60.dp)
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = dimensionResource(R.dimen.button_text).value.sp
                )
            }
        }
    }

    if (showSignIn) {
        SignInDialog(
            onDismiss = { showSignIn = false},
            onSubmit = {email, password ->
                //no auth logic for now
                showSignIn = false
                onSignInSuccess()
            }
        )
    }
}

@Composable
private fun SignInDialog(
    onDismiss: () -> Unit,
    onSubmit: (email: String, password: String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sign In") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md))) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSubmit(email, password) },
                enabled = email.isNotBlank() && password.isNotBlank()
            ) {
                Text("Continue")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}