package io.github.hiztree.thebasics.core.api

import java.time.Instant
import java.time.temporal.ChronoUnit

class BasicTime {

    var days = 0L
        set(value) {
            if(value < 0)
                throw IllegalArgumentException("Days must be greater then 0.")
            field = value
        }

    var hours = 0L
        set(value) {
            if(value < 0)
                throw IllegalArgumentException("Hours must be greater then 0.")
            field = value
        }

    var minutes = 0L
        set(value) {
            if(value < 0)
                throw IllegalArgumentException("Minutes must be greater then 0.")
            field = value
        }

    var seconds = 0L
        set(value) {
            if(value < 0)
                throw IllegalArgumentException("Seconds must be greater then 0.")
            field = value
        }

    fun toInstant(): Instant = Instant.now()
            .plus(days, ChronoUnit.DAYS)
            .plus(hours, ChronoUnit.HOURS)
            .plus(minutes, ChronoUnit.MINUTES)
            .plus(seconds, ChronoUnit.SECONDS)


    fun isZero() = days <= 0 && hours <= 0 && minutes <= 0 && seconds <= 0

    override fun toString(): String {
        val sb = StringBuilder()

        when {
            days > 0 -> sb.append("${days}d")
            hours > 0 -> sb.append("${hours}h")
            minutes > 0 -> sb.append("${minutes}m")
            seconds > 0 -> sb.append("${seconds}s")
        }

        return sb.toString()
    }

    companion object {
        fun parseTime(input: String): BasicTime {
            var sb = StringBuilder()
            val bs = BasicTime()

            for (c in input.toCharArray()) {
                when (c) {
                    'd' -> {
                        bs.days = sb.toString().toLong()
                        sb = StringBuilder()
                    }
                    'h' -> {
                        bs.hours = sb.toString().toLong()
                        sb = StringBuilder()
                    }
                    'm' -> {
                        bs.minutes = sb.toString().toLong()
                        sb = StringBuilder()
                    }
                    's' -> {
                        bs.seconds = sb.toString().toLong()
                        sb = StringBuilder()
                    }
                    else -> {
                        sb.append(c)
                    }
                }
            }

            return bs
        }
    }
}