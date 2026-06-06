package org.example

import kotlinx.coroutines.runBlocking
import org.example.service.MarketDataService
import org.example.service.GeminiService
import org.example.service.HolidayCheckerService
import org.example.service.TelegramService
import org.example.service.HttpClientProvider

fun main() = runBlocking {

    val holidayChecker = HolidayCheckerService()

    println("🔍 Analyzing stock market schedule calendar...")
    if (!holidayChecker.isMarketOpenToday()) {
        println("🛑 Market is closed today (Weekend or Official NSE Holiday). Aborting agent execution.")
        return@runBlocking
    }

//    val geminiApiKey = "AQ.Ab8RN6I_3rpT6D5Ncvk7tgMVIH4UCn4rTrprjnRr1qEk7hiEsA"
//    val telegramBotToken = "8676201197:AAGt-MFq0FKACnq7T8LpKx9KDIDNj9jER6o"
//    val telegramChatId = "665838291"

    val geminiApiKey = System.getenv("GEMINI_API_KEY") ?: throw IllegalArgumentException("Missing GEMINI_API_KEY")
    val telegramBotToken = System.getenv("TELEGRAM_BOT_TOKEN") ?: throw IllegalArgumentException("Missing TELEGRAM_BOT_TOKEN")
    val telegramChatId = System.getenv("TELEGRAM_CHAT_ID") ?: throw IllegalArgumentException("Missing TELEGRAM_CHAT_ID")

    // Instantiate your modular components
    val marketDataService = MarketDataService()
    val geminiService = GeminiService()
    val telegramService = TelegramService()

    println("🚀 Running Modularized AI Market Agent...")

    // Execute data operations cleanly down the pipeline
    val rawData = marketDataService.fetchClosingMarketData()
    val conciseSummary = geminiService.askGeminiAI(rawData, geminiApiKey)
    telegramService.sendToTelegram(telegramBotToken, telegramChatId, conciseSummary)

    println("✅ Clean brief sent to your Telegram app.")

    // Clean up background sockets and engine resource pools
    HttpClientProvider.client.close()
}