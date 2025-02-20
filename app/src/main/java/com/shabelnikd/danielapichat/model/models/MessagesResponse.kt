package com.shabelnikd.danielapichat.model.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MessagesResponse(
    @SerialName("chatId")
    val chatId: Int? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("receiverId")
    val receiverId: String? = null,
    @SerialName("senderId")
    val senderId: String? = null,
    @SerialName("timestamp")
    val timestamp: String? = null
)
