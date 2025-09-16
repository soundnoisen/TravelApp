package com.example.travelapp2.presentation.registration

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.travelapp2.presentation.common.CustomButton
import com.example.travelapp2.R
import com.example.travelapp2.presentation.common.EmailTextField
import com.example.travelapp2.presentation.common.PasswordTextField

@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRepeat by remember { mutableStateOf("") }
    val errorMessage by viewModel.errorMessage.collectAsState()
    val registerSuccess by viewModel.registerSuccess.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.resetError()
        }
    }

    if (registerSuccess) {
        LaunchedEffect(Unit) {
            navController.navigate("login") {
                popUpTo("registration") { inclusive = true }
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

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Регистрация",
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
                modifier = Modifier.fillMaxWidth().testTag("email_field")
            )

            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                value = password,
                placeholderText = "Введите пароль",
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth().testTag("password_field")
            )

            Spacer(modifier = Modifier.height(8.dp))
            PasswordTextField(
                value = passwordRepeat,
                placeholderText = "Повторите пароль",
                onValueChange = { passwordRepeat = it },
                modifier = Modifier.fillMaxWidth().testTag("password_field_repeat")
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Уже есть аккаунт?",
                color = colorResource(id = R.color.main),
                modifier = Modifier
                    .fillMaxWidth().testTag("login_text")
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { navController.navigate("login") },
                textAlign = TextAlign.Center
            )
        }

        CustomButton(onClick = { viewModel.register(email, password, passwordRepeat) }, buttonText = "Зарегистрироваться", modifier = Modifier.testTag("registration_button"))  }
}

