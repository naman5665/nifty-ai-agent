package org.example.service

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

class HolidayCheckerService {
    // Official 2026 National Stock Exchange (NSE) Equity Market Holiday Registry
    private val nseHolidays2026 = setOf(
        LocalDate.of(2026, 1, 15),  // Municipal Election Maharashtra
        LocalDate.of(2026, 1, 26),  // Republic Day
        LocalDate.of(2026, 3, 3),   // Holi
        LocalDate.of(2026, 3, 26),  // Shri Ram Navami
        LocalDate.of(2026, 3, 31),  // Shri Mahavir Jayanti
        LocalDate.of(2026, 4, 3),   // Good Friday
        LocalDate.of(2026, 4, 14),  // Dr. Baba Saheb Ambedkar Jayanti
        LocalDate.of(2026, 5, 1),   // Maharashtra Day
        LocalDate.of(2026, 5, 28),  // Bakri Id
        LocalDate.of(2026, 6, 26),  // Muharram
        LocalDate.of(2026, 9, 14),  // Ganesh Chaturthi
        LocalDate.of(2026, 10, 2),  // Mahatma Gandhi Jayanti
        LocalDate.of(2026, 10, 20), // Dussehra
        LocalDate.of(2026, 11, 10), // Diwali-Balipratipada
        LocalDate.of(2026, 11, 24), // Prakash Gurpurb Sri Guru Nanak Dev
        LocalDate.of(2026, 12, 25)  // Christmas
    )

    fun isMarketOpenToday(): Boolean {
        // Enforce evaluation inside Indian Standard Time (IST) regardless of where host server runs
        val today = LocalDate.now(ZoneId.of("Asia/Kolkata"))
        val dayOfWeek = today.dayOfWeek

        // 1. Immediately abort if it's Saturday or Sunday
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return false
        }

        // 2. Abort if today matches a designated market holiday
        if (nseHolidays2026.contains(today)) {
            return false
        }

        return true
    }
}