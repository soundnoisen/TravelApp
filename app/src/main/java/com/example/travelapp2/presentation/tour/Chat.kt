package com.example.travelapp2.presentation.tour

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelapp2.R
import com.example.travelapp2.data.models.ChatMessage
import com.example.travelapp2.data.models.UserProfileFirebase
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun GroupChatScreen(
    chatId: String,
    viewModel: ChatViewModel,
    userProfile: UserProfileFirebase?,
    navController: NavHostController,
) {
    val messages by viewModel.messages.observeAsState(emptyList())
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(chatId) {
        viewModel.listenToChat(chatId)
    }

    LaunchedEffect(messages.size) {
        delay(200)
        listState.animateScrollToItem(messages.size)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.background))) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 144.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages) { msg ->
                    MessageBubble(msg,(msg.senderId == userProfile!!.userId), msg.senderId, navController)
                }
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = colorResource(id = R.color.hint))
        }


        Row(
            modifier = Modifier
                .padding(bottom = 86.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(colorResource(id = R.color.background)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StyledTextField2(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholderText = "Введите сообщение"
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                modifier = Modifier.background(colorResource(id = R.color.background)),
                onClick = {
                    if (input.isNotBlank()) {
                        viewModel.sendMessage(chatId, input, userProfile!!)
                        input = ""
                    }
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.round_send_24),
                    tint = colorResource(id = R.color.main),
                    modifier = Modifier.size(30.dp),
                    contentDescription = null
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledTextField2(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    var isHintDisplayed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(56.dp)
            .background(colorResource(id = R.color.background))
    ) {
        androidx.compose.material3.TextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            placeholder = {
                if (!isHintDisplayed) {
                    androidx.compose.material3.Text(
                        text = placeholderText,
                        color = colorResource(id = R.color.hint),
                        fontSize = 16.sp,
                        fontFamily = Font(R.font.inter_regular).toFontFamily()
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged { focusState ->
                    isHintDisplayed = focusState.isFocused
                },
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = Font(R.font.inter_regular).toFontFamily()
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}


@Composable
fun MessageBubble(message: ChatMessage, isFromUser: Boolean, userId: String, navController: NavHostController) {
    val bubbleColor = if (isFromUser) colorResource(id = R.color.main).copy(0.2f) else Color.White
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isFromUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isFromUser) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(message.avatarUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable {
                        navController.navigate("Profile/${userId}")
                    },
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .shadow(
                    elevation = if (!isFromUser) 4.dp else 0.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = Color.Black.copy(alpha = 0.2f),
                    spotColor = Color.Black.copy(alpha = 0.2f)
                )
                .background(bubbleColor, shape = RoundedCornerShape(12.dp))
                .padding(10.dp)
                .widthIn(max = 260.dp)

        ) {
            if (!isFromUser) {
                Text(
                    text = message.senderName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.main)
                )
            }
            Text(text = message.text)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = timeFormat.format(Date(message.timestamp)),
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

