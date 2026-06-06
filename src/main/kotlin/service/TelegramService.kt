package org.example.service

import io.ktor.client.request.*
import io.ktor.http.*
import org.example.models.TelegramPayload

class TelegramService {
    private val httpClient = HttpClientProvider.client

    suspend fun sendToTelegram(botToken: String, chatId: String, messageText: String) {
        val url = "https://api.telegram.org/bot$botToken/sendMessage"
        val payload = TelegramPayload(chat_id = chatId, text = messageText)

        try {
            httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(payload)
            }
        } catch (e: Exception) {
            println("Failed to send telegram alert: ${e.message}")
        }
    }
}