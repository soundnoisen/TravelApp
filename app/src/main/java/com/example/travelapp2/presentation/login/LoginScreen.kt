package com.example.travelapp2.presentation.login
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource

import androidx.compose.ui.unit.dp


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.travelapp2.presentation.common.CustomButton
import com.example.travelapp2.R
import com.example.travelapp2.presentation.common.EmailTextField
import com.example.travelapp2.presentation.common.PasswordTextField

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val errorMessage by viewModel.errorMessage.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        snapshotFlow { errorMessage }
            .collect { msg ->
                msg?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    viewModel.resetError()
                }
            }
    }


    if (loginSuccess) {
        LaunchedEffect(Unit) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .padding(top = 30.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Column(Modifier.fillMaxWidth()) {
            Text(
                text = "Вход",
                fontSize = 20.sp,
                color = Color.Black,
                fontFamily = Font(R.font.manrope_bold).toFontFamily(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(100.dp))

            EmailTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.testTag("email_field")
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                value = password,
                placeholderText = "Введите пароль",
                onValueChange = { password = it },
                modifier = Modifier.testTag("password_field")
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Еще нет аккаунта?",
                color = colorResource(id = R.color.main),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("register_text")
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        navController.navigate("registration")
                    },
                fontFamily = Font(R.font.inter_medium).toFontFamily(),
                textAlign = TextAlign.Center
            )
        }

        CustomButton(
            modifier = Modifier.testTag("login_button"),
            onClick = {
            viewModel.login(email, password, context)

        },
            buttonText = "Войти")
    }
}
