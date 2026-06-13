package org.example.service

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*

class GeminiService {
    private val httpClient = HttpClientProvider.client

    suspend fun askGeminiAI(marketRawData: String, geminiApiKey: String): String {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$geminiApiKey"

        val requestBody = JsonObject(mapOf(
            "contents" to JsonArray(listOf(JsonObject(mapOf(
                "parts" to JsonArray(listOf(JsonObject(mapOf(
                    "text" to JsonPrimitive(
                        "You are an expert stock market analyst. Summarize this Indian market data into an ultra-concise, raw plain text brief. \n\n" +
                                "CRITICAL INSTRUCTIONS:\n" +
                                "1. DO NOT USE ANY HTML TAGS (No <b>, <i>, <p>, etc.).\n" +
                                "2. DO NOT USE MARKDOWN (No asterisks ** or underscores _).\n" +
                                "3. Keep descriptions extremely short. Use only bullet points (•).\n\n" +
                                "EXACT STRUCTURE TO USE:\n" +
                                "MARKET WRAP\n" +
                                "• NIFTY 50 closed at [Price], down/up [Change].\n" +
                                "• Range: High [High] | Low [Low].\n\n" +
                                "KEY INSIGHTS\n" +
                                "• [One short bullet about resistance/support level]\n" +
                                "• [One short bullet about current market sentiment]\n\n" +
                                "OUTLOOK FOR TOMORROW\n" +
                                "• Support is at [level] | Resistance is at [level].\n" +
                                "• Strategy: [2-5 words strategy summary].\n\n" +
                                "Data: $marketRawData"
                    )
                ))))
            ))))
        ))

        return try {
            val response = httpClient.post(url) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            val responseBody = response.bodyAsText()
            val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject

            // Gemini returns {"error": {...}} on quota/auth failures — surface the real message
            val apiError = jsonResponse["error"]?.jsonObject
            if (apiError != null) {
                val code = apiError["code"]?.jsonPrimitive?.content ?: "unknown"
                val message = apiError["message"]?.jsonPrimitive?.content ?: "Unknown Gemini API error"
                return "Gemini API error ($code): $message"
            }

            jsonResponse["candidates"]?.jsonArray?.get(0)?.jsonObject?.get("content")
                ?.jsonObject?.get("parts")?.jsonArray?.get(0)?.jsonObject?.get("text")
                ?.jsonPrimitive?.content ?: "Gemini returned no candidates. Raw response: $responseBody"

        } catch (e: Exception) {
            "AI processing failed. Error: ${e.message}"
        }
    }
}