package com.example.travelapp2.presentation.tour

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp2.data.models.ChatMessage
import com.example.travelapp2.data.models.UserProfileFirebase
import com.example.travelapp2.domain.repository.ChatRepository
import com.example.travelapp2.domain.repository.UserAuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val auth: FirebaseAuth,
    private val userAuthRepository: UserAuthRepository
) : ViewModel() {

    private val _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> = _messages

    fun listenToChat(chatId: String) {
        repository.listenToMessages(chatId) {
            _messages.postValue(it)
        }
    }

    fun sendMessage(chatId: String, text: String, user: UserProfileFirebase) {
        val message = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderId = user.userId,
            senderName = user.displayName,
            avatarUrl = user.photoUrl,
            text = text,
        )

        viewModelScope.launch {
            repository.sendMessage(chatId, message)
        }
    }
}
