package org.example.models

import kotlinx.serialization.Serializable

@Serializable
data class TelegramPayload(
    val chat_id: String,
    val text: String
)