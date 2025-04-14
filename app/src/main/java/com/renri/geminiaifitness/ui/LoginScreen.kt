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
import androidx.compose.runtime.collectAsState





@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val loginState by loginViewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)

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

        Spacer(modifier = Modifier.height(16.dp))

        // Show error message if any
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Show loading state
        if (loginState is ResultState.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(8.dp))
        }

        Button(
            onClick = {
                if (user.isBlank() || password.isBlank()) {
                    errorMessage = "User and password cannot be empty"
                } else {
                    errorMessage = ""
                    loginViewModel.login(user, password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.navigate(Screen.Registration.route) }
        ) {
            Text("Don't have an account? Register here")
        }
    }

    // React to login result
    LaunchedEffect(loginState) {
        when (loginState) {
            is ResultState.Success -> {
                navController.navigate(Screen.Main.route)
            }
            is ResultState.Error -> {
                errorMessage = (loginState as ResultState.Error).message
            }
            else -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}
