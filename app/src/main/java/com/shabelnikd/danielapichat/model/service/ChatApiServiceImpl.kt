package com.shabelnikd.danielapichat.model.service

import com.shabelnikd.danielapichat.model.models.MessagesResponse
import com.shabelnikd.danielapichat.utils.safeApiCall
import org.koin.core.component.KoinComponent
import retrofit2.Response

class ChatApiServiceImpl(private val apiService: ChatApiService) : KoinComponent {
    suspend fun getChatById(chatId: Int): Result<Response<List<MessagesResponse>>> =
        safeApiCall(
            { apiService.getChatById(chatId) },
            "Error fetching getChatById"
        )

    suspend fun sendMessage(
        chatId: Int,
        senderId: Int,
        receiverId: Int,
        message: String
    ): Result<Response<MessagesResponse>> =
        safeApiCall(
            { apiService.sendMessage(chatId, senderId, receiverId, message) },
            "Error fetching send message"
        )

    suspend fun editMessage(
        chatId: Int,
        messageId: Int,
        newMessage: String
    ): Result<Response<MessagesResponse>> =
        safeApiCall(
            { apiService.editMessage(chatId, messageId, newMessage) },
            "Error fetching edit message"
        )

    suspend fun deleteMessage(
        chatId: Int,
        messageId: Int
    ): Result<Response<Unit>> =
        safeApiCall(
            { apiService.deleteMessage(chatId, messageId) },
            "Error fetching delete message"
        )
}