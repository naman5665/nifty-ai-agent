# 📈 AI Market Briefing Agent

An automated, serverless Kotlin-based AI agent that tracks the **NIFTY 50** index, analyzes market performance using the Google Gemini LLM engine, and sends a concise closing brief directly to a Telegram channel every weekday at the market close (**3:45 PM IST**).

Built on real-world software engineering principles, the system leverages a stateless **Guard-Condition architecture** and runs entirely inside **GitHub Actions** cloud containers, meaning it requires zero external hosting costs and executes even when your local machine is completely offline.

---

## 🏗️ System Architecture & Workflow

The agent operates as a linear, laser-focused worker pipeline. Instead of running continuous background memory loops, it uses an OS-level scheduler to fire up, validate rules, execute its data sync, and immediately spin down to conserve computing resources.

```text
  [GitHub Actions Cloud Runner] 
         │ (Fires at 15:45 IST / 10:15 UTC via Cron)
         ▼
 ┌────────────────────────┐
 │ HolidayCheckerService  │ ──► [Market Closed / Weekend] ──► 🛑 Safe Abort
 └────────────────────────┘
         │ (Market Open Confirmation)
         ▼
 ┌────────────────────────┐
 │   MarketDataService    │ ──► Fetches real-time closing data from Yahoo Finance
 └────────────────────────┘
         │ (Raw Financial Data Payload)
         ▼
 ┌────────────────────────┐
 │     GeminiService      │ ──► Generates contextual executive brief via Gemini API
 └────────────────────────┘
         │ (Clean Text Digest)
         ▼
 ┌────────────────────────┐
 │    TelegramService     │ ──► Dispatches real-time broadcast via Telegram Bot API
 └────────────────────────┘
         │
         ▼
  [Pipeline Termination]  ──► Closes Ktor engine connections & shuts down serverless node
