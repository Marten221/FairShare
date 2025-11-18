package com.example.fairshare.ui.screens

import android.util.Patterns
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fairshare.R
import com.example.fairshare.ui.viewmodels.AuthState
import com.example.fairshare.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageScreen(
    onSignInSuccess: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val vm: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val state by vm.state.collectAsState()

    var showSignIn by rememberSaveable { mutableStateOf(false) }
    var showSignUp by rememberSaveable { mutableStateOf(false) }
    var errorMsg by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(state) {
        when (state) {
            is AuthState.Success -> {
                showSignIn = false
                showSignUp = false
                vm.reset()
                onSignInSuccess() // navigates to "groups"
            }
            is AuthState.Error -> {
                errorMsg = (state as AuthState.Error).message
                vm.reset()
            }
            else -> {}
        }
    }

    Scaffold() { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.home_title),
                fontSize = dimensionResource(R.dimen.title).value.sp,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.home_title_bottom).value.dp)
            )

            Button(
                onClick = { showSignIn = true },
                modifier = Modifier.size(width = dimensionResource(R.dimen.button_width).value.dp, height = dimensionResource(R.dimen.button_height).value.dp)
            ) {
                Text(
                    text = stringResource(R.string.home_sign_in),
                    fontSize = dimensionResource(R.dimen.button_text).value.sp
                )
            }

            Button(
                onClick = { showSignUp = true },
                modifier = Modifier
                    .padding(top = dimensionResource(R.dimen.spacing_l).value.dp)
                    .size(width = dimensionResource(R.dimen.button_width).value.dp, height = dimensionResource(R.dimen.button_height).value.dp)
            ) {
                Text(
                    text = stringResource(R.string.home_sign_up),
                    fontSize = dimensionResource(R.dimen.button_text).value.sp
                )
            }
            TextButton(
                onClick = onToggleTheme,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_l).value.dp)
            ) {
                Text(
                    text = if (isDarkTheme) "Switch to light mode" else "Switch to dark mode",
                    fontSize = 14.sp
                )
            }
        }
    }

    if (showSignIn) {
        SignInDialog(
            onDismiss = { showSignIn = false},
            onSubmit = { email, password ->
                vm.login(email, password)
            }
        )
    }

    if (showSignUp) {
        SignUpDialog(
            onDismiss = { showSignUp = false },
            onSubmit = { email, password -> vm.register(email, password) }
        )
    }

    if (errorMsg != null) {
        AlertDialog(
            onDismissRequest = { errorMsg = null },
            title = { Text(stringResource(R.string.sign_in_failed))},
            text = { Text(errorMsg!!) },
            confirmButton = {
                TextButton(onClick = { errorMsg = null }) { Text("OK") }
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

    val isEmailValid by remember(email) {
        derivedStateOf { Patterns.EMAIL_ADDRESS.matcher(email).matches() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.auth_title_sign_in)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md))) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.auth_label_email)) },
                    singleLine = true,
                    isError = email.isNotBlank() && !isEmailValid,
                    supportingText = {
                        if (email.isNotBlank() && !isEmailValid) {
                            Text(stringResource(R.string.auth_email_error))
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.auth_label_password)) },
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
                enabled = isEmailValid && password.isNotBlank()
            ) {
                Text(stringResource(R.string.auth_continue))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.auth_cancel)) }
        }
    )
}

@Composable
private fun SignUpDialog(
    onDismiss: () -> Unit,
    onSubmit: (email: String, password: String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val isEmailValid by remember(email) {
        derivedStateOf { Patterns.EMAIL_ADDRESS.matcher(email).matches() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.auth_title_sign_up)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_md))) {
                OutlinedTextField(
                    value = email, onValueChange = { email = it },
                    label = { Text(stringResource(R.string.auth_label_email)) },
                    singleLine = true,
                    isError = email.isNotBlank() && !isEmailValid,
                    supportingText = {
                        if (email.isNotBlank() && !isEmailValid) {
                            Text(stringResource(R.string.auth_email_error))
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password, onValueChange = { password = it },
                    label = { Text(stringResource(R.string.auth_label_password)) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSubmit(email, password) }, enabled = isEmailValid && password.isNotBlank()) {
                Text(stringResource(R.string.auth_continue))
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.auth_cancel)) } }
    )
}
