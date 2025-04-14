package com.renri.geminiaifitness.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.renri.geminiaifitness.ui.navigation.Screen
import com.renri.geminiaifitness.ui.viewmodels.LoginViewModel
import com.renri.geminiaifitness.ui.viewmodels.ResultState

@Composable
fun RegistrationScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val registerState by loginViewModel.registerState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Register", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = user,
            onValueChange = { user = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Show loading state
        if (registerState is ResultState.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(8.dp))
        }

        Button(
            onClick = {
                when {
                    user.isBlank() || password.isBlank() || confirmPassword.isBlank() ->
                        errorMessage = "All fields must be filled"
                    password != confirmPassword ->
                        errorMessage = "Passwords do not match"
                    else -> {
                        errorMessage = ""
                        loginViewModel.register(user, password)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.navigate(Screen.Login.route) }
        ) {
            Text("Already have an account? Login here")
        }
    }

    // Navigate back on successful registration
    LaunchedEffect(registerState) {
        if (registerState is ResultState.Success) {
            navController.navigate(Screen.Login.route)
        } else if (registerState is ResultState.Error) {
            errorMessage = (registerState as ResultState.Error).message
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen(navController = rememberNavController())
}
